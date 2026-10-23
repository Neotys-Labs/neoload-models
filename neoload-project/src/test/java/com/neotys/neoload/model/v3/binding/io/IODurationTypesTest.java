package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidDurationRead;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationVariable;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Durations read from the v3 as-code YAML: millisecond-based fields are a {@link DurationOrVariable}, and an
 * invalid text fails the read.
 */
public class IODurationTypesTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String MILLISECONDS_ACTIONS = DurationFields.actions(
			"    - delay: %1$s\n    - think_time: %1$s\n"
					+ "    - transaction:\n        name: T\n        pacing: %1$s\n        steps:\n        - delay: 1s\n"
					+ "    - web_page:\n        think_time: %1$s\n        steps:\n        - request:\n            url: http://host:80/\n"
					+ DurationFields.waitUntil("        timeout: %1$s\n"));

	private static Project read(final String yaml) throws IOException {
		return new IO().read(yaml).getProject();
	}

	private static Validation validate(final Project project) {
		return VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	private static String write(final Project project) throws IOException {
		return new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);
	}

	private static List<Step> steps(final Project project) {
		return project.getUserPaths().get(0).getActions().getSteps();
	}

	private static DurationOrVariable valueOf(final Step step) {
		return ((DurationConstant) ((Delay) step).getDuration()).getValue();
	}

	private static DurationOrVariable millis(final long milliseconds) {
		return DurationLiteral.builder().value(Duration.ofMillis(milliseconds)).build();
	}

	private static DurationOrVariable variable(final String reference) {
		return DurationVariable.builder().value(reference).build();
	}

	@Test
	public void plainNumbersOnMillisecondFieldsAreMilliseconds() throws IOException {
		final Project project = read(String.format(MILLISECONDS_ACTIONS, "1500"));
		final List<Step> steps = steps(project);

		assertEquals(millis(1500), valueOf(steps.get(0)));
		assertEquals(millis(1500), valueOf(steps.get(1)));
		assertEquals(DurationConstant.builder().value(millis(1500)).build(), ((Container) steps.get(2)).getPacing().get());
		assertEquals(DurationConstant.builder().value(millis(1500)).build(), ((WebPage) steps.get(3)).getThinkTime().get());
		assertEquals(millis(1500), ((WaitUntil) steps.get(4)).getTimeout());
		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
	}

	@Test
	public void millisecondFieldsAcceptOrderedUnitsAndRejectMisorderedUnitsAtRead() throws IOException {
		assertEquals(millis(3723004), valueOf(steps(read(DurationFields.THINK_TIME.yaml("1h2m3s4ms"))).get(0)));
		assertEquals(millis(250), valueOf(steps(read(DurationFields.THINK_TIME.yaml("250ms"))).get(0)));
		for (final String value : new String[] {"1s1h", "5ms1s"}) {
			assertInvalidDurationRead(() -> read(DurationFields.THINK_TIME.yaml(value)), value, "think_time");
		}
	}

	@Test
	public void waitUntilTimeoutAcceptsAndRejectsWhatStepThinkTimeDoes() {
		for (final String value : new String[] {"1500", "1s500ms", "2m", "${timeout}", "abc", "5x", "-1", "1s1h", "99999999999999999999"}) {
			final Object thinkTime = readOrFailure(DurationFields.THINK_TIME, value);
			final Object waitUntil = readOrFailure(DurationFields.WAIT_UNTIL_TIMEOUT, value);

			if (thinkTime instanceof JsonMappingException) {
				assertTrue(value, waitUntil instanceof JsonMappingException);
				assertTrue(value, ((JsonMappingException) waitUntil).getMessage().contains("'" + value + "'"));
				assertTrue(value, ((JsonMappingException) waitUntil).getMessage().contains("[\"timeout\"]"));
			} else {
				assertEquals(value, thinkTime, waitUntil);
			}
		}
	}

	private static Object readOrFailure(final DurationFields field, final String value) {
		try {
			final Step step = steps(read(field.yaml(value))).get(0);
			return step instanceof WaitUntil ? ((WaitUntil) step).getTimeout() : valueOf(step);
		} catch (final JsonMappingException e) {
			return e;
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void variableReferencesRoundTripUnchangedOnMillisecondFields() throws IOException {
		final String yaml = DurationFields.actions("    - delay: ${d}\n    - think_time: ${t}\n"
				+ "    - transaction:\n        name: T\n        pacing: ${p}\n        steps:\n        - delay: 1s\n"
				+ "    - web_page:\n        think_time: ${w}\n        steps:\n        - request:\n            url: http://host:80/\n"
				+ DurationFields.waitUntil("        timeout: ${u}\n"));

		final Project project = read(yaml);
		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
		final String written = write(project);
		final Project reopened = read(written);

		for (final Project read : Arrays.asList(project, reopened)) {
			final List<Step> steps = steps(read);
			assertEquals(variable("${d}"), valueOf(steps.get(0)));
			assertEquals(variable("${t}"), valueOf(steps.get(1)));
			assertEquals(variable("${p}"), ((DurationConstant) ((Container) steps.get(2)).getPacing().get()).getValue());
			assertEquals(variable("${w}"), ((DurationConstant) ((WebPage) steps.get(3)).getThinkTime().get()).getValue());
			assertEquals(variable("${u}"), ((WaitUntil) steps.get(4)).getTimeout());
		}
		for (final String reference : new String[] {"${d}", "${t}", "${p}", "${w}", "${u}"}) {
			assertTrue(written, written.contains(reference));
		}
	}

	@Test
	public void invalidDurationsFailTheReadAndAreNeverReplacedByZero() {
		assertInvalidDurationRead(() -> read(DurationFields.DELAY.yaml("99999999999999999999")), "99999999999999999999", "delay");
		assertInvalidDurationRead(() -> read(DurationFields.WEB_PAGE_THINK_TIME.yaml("abc")), "abc", "think_time");
	}
}
