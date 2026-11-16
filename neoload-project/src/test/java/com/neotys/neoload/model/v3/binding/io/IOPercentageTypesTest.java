package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidPercentageRead;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.userpath.PercentageOrVariable;
import com.neotys.neoload.model.v3.project.userpath.PercentageVariable;
import com.neotys.neoload.model.v3.project.userpath.Percentages;
import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * Percentages of the User Path think time read from and written to the v3 as-code YAML: an integer with an
 * optional {@code %} or a variable is a {@link PercentageOrVariable}, an invalid text fails the read, and the
 * bounds are left to the validation.
 */
public class IOPercentageTypesTest {
	private static final String FACTOR_MESSAGE = "must be a non-negative integer percentage (e.g. 150%) or a variable.";
	private static final String RANDOM_MESSAGE = "must be a positive integer percentage (e.g. 10%) or a variable.";

	private static String userPathWith(final String thinkTime) {
		return "name: MyProject\nuser_paths:\n- name: MyUserPath\n  think_time:\n    " + thinkTime + "\n  actions:\n    steps:\n    - delay: 1s\n";
	}

	private static UserPathThinkTime readThinkTime(final String thinkTime) throws IOException {
		return new IO().read(userPathWith(thinkTime)).getProject().getUserPaths().get(0).getThinkTime().get();
	}

	private static Validation validate(final String thinkTime) throws IOException {
		return new Validator().validate(new IO().read(userPathWith(thinkTime)), NeoLoad.class);
	}

	@Test
	public void percentSignIsOptionalWhenReading() throws IOException {
		assertEquals(Percentages.of("150"), readThinkTime("factor: 150%").getFactor().get());
		assertEquals(Percentages.of("150"), readThinkTime("factor: 150").getFactor().get());
		assertEquals(Percentages.of("0"), readThinkTime("factor: 0").getFactor().get());
		assertEquals(Percentages.of("50"), readThinkTime("factor: \"50%\"").getFactor().get());
		assertEquals(Percentages.of("10"), readThinkTime("random: 10%").getRandom().get());
		assertEquals(Percentages.of("10"), readThinkTime("random: 10").getRandom().get());
	}

	@Test
	public void variableIsReadAsIs() throws IOException {
		assertEquals(PercentageVariable.builder().value("${my_factor}").build(), readThinkTime("factor: \"${my_factor}\"").getFactor().get());
		assertEquals(PercentageVariable.builder().value("${my_random}").build(), readThinkTime("random: \"${my_random}\"").getRandom().get());
	}

	@Test
	public void invalidFactorFailsTheReadNamingTheRawValueAndTheField() {
		for (final String value : new String[] {"1.5", "\"${my_factor}%\"", "10%%", "abc"}) {
			assertInvalidPercentageRead(() -> new IO().read(userPathWith("factor: " + value)), value.replace("\"", ""), "factor");
		}
	}

	@Test
	public void invalidRandomFailsTheReadNamingTheRawValueAndTheField() {
		for (final String value : new String[] {"1.5", "\"${my_random}%\"", "10%%", "abc"}) {
			assertInvalidPercentageRead(() -> new IO().read(userPathWith("random: " + value)), value.replace("\"", ""), "random");
		}
	}

	@Test
	public void negativeFactorIsReadAndRejectedByTheValidation() throws IOException {
		assertEquals(Percentages.of("-10"), readThinkTime("factor: -10%").getFactor().get());
		assertViolations(validate("factor: -10%"), invalidValue("project.user_paths[0].think_time.factor", FACTOR_MESSAGE));
		assertViolations(validate("factor: -1"), invalidValue("project.user_paths[0].think_time.factor", FACTOR_MESSAGE));
	}

	@Test
	public void zeroOrNegativeRandomIsReadAndRejectedByTheValidation() throws IOException {
		assertEquals(Percentages.of("0"), readThinkTime("random: 0%").getRandom().get());
		assertViolations(validate("random: 0%"), invalidValue("project.user_paths[0].think_time.random", RANDOM_MESSAGE));
		assertViolations(validate("random: -1"), invalidValue("project.user_paths[0].think_time.random", RANDOM_MESSAGE));
	}

	@Test
	public void boundsAreNotCheckedOnAVariable() throws IOException {
		assertTrue(validate("factor: \"${my_factor}\"").isValid());
		assertTrue(validate("random: \"${my_random}\"").isValid());
	}

	@Test
	public void zeroFactorAndOneRandomAreWithinTheBounds() throws IOException {
		assertTrue(validate("factor: 0").isValid());
		assertTrue(validate("random: 1%").isValid());
	}

	@Test
	public void valueReadWithoutPercentSignIsNormalisedOnWrite() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(userPathWith("factor: 150\n    random: 10"));

		final String written = new IO().write(descriptor, Format.JSON);

		assertTrue(written, written.contains("\"think_time\":{\"factor\":\"150%\",\"random\":\"10%\"}"));
	}

	@Test
	public void variableIsWrittenAsIs() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(userPathWith("factor: \"${my_factor}\"\n    random: \"${my_random}\""));

		final String written = new IO().write(descriptor, Format.JSON);

		assertTrue(written, written.contains("\"think_time\":{\"factor\":\"${my_factor}\",\"random\":\"${my_random}\"}"));
	}
}
