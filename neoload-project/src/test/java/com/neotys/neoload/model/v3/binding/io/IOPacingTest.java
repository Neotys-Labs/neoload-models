package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.MIN_GREATER_THAN_MAX;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.VALUE_COMBINED_WITH_BOUNDS;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidDurationRead;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertReadError;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class IOPacingTest extends AbstractIOElementsTest {

	private static Container withPacing(final Duration pacing) {
		return Container.builder().pacing(pacing).addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build()).build();
	}

	private static Project buildProjectWithPacingEverywhere(final Duration pacing) {
		final Container transaction = Container.builder()
				.name("MyTransaction")
				.pacing(pacing)
				.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build())
				.build();

		final TryCatch tryCatch = TryCatch.builder()
				.getTry(withPacing(pacing))
				.getCatch(withPacing(pacing))
				.build();

		final Switch switchStep = Switch.builder()
				.value("${MySwitchVar}")
				.addCases(Case.builder()
						.value("0")
						.isBreak(true)
						.pacing(pacing)
						.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build())
						.build())
				.getDefault(withPacing(pacing))
				.build();

		final Container actions = Container.builder()
				.name("actions")
				.pacing(pacing)
				.addSteps(transaction, tryCatch, switchStep)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.init(Container.builder().name("init").pacing(pacing).addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build()).build())
				.actions(actions)
				.end(Container.builder().name("end").pacing(pacing).addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build()).build())
				.build();

		return Project.builder().name("MyProject").addUserPaths(userPath).build();
	}

	// The "if" step's Condition/Match round-trip is a pre-existing gap (see IORoundTripTest's
	// exclusion of test-if-*), unrelated to pacing, so it is verified for reading only, separately
	// from the other container types above which are covered by IORoundTripTest as well.
	private static Project buildProjectWithPacingOnIfThenElse(final Duration pacing) {
		final If ifStep = If.builder()
				.addConditions(Condition.builder()
						.operand1("${MyVar}")
						.operator(Condition.Operator.EQUALS)
						.operand2("x")
						.build())
				.then(withPacing(pacing))
				.getElse(withPacing(pacing))
				.build();

		return IOHelper.buildProject(ifStep);
	}

	@Test
	public void readConstantPacingOnEveryContainerType() throws IOException {
		final Project expectedProject = buildProjectWithPacingEverywhere(DurationConstant.builder().value(Durations.of("30000")).build());
		assertNotNull(expectedProject);

		read("test-pacing-constant", expectedProject);
	}

	@Test
	public void readRandomPacingOnEveryContainerType() throws IOException {
		final Project expectedProject = buildProjectWithPacingEverywhere(
				DurationRandom.builder().min(Durations.of("1000")).max(Durations.of("3000")).build());
		assertNotNull(expectedProject);

		read("test-pacing-random", expectedProject);
	}

	@Test
	public void readConstantPacingOnIfThenElse() throws IOException {
		final Project expectedProject = buildProjectWithPacingOnIfThenElse(DurationConstant.builder().value(Durations.of("30000")).build());
		assertNotNull(expectedProject);

		read("test-pacing-if-constant", expectedProject);
	}

	@Test
	public void readRandomPacingOnIfThenElse() throws IOException {
		final Project expectedProject = buildProjectWithPacingOnIfThenElse(
				DurationRandom.builder().min(Durations.of("1000")).max(Durations.of("3000")).build());
		assertNotNull(expectedProject);

		read("test-pacing-if-random", expectedProject);
	}

	@Test
	public void readRandomPacingWithoutMinDefaultsToZero() throws IOException {
		final Project expectedProject = IOHelper.buildProject(
				Container.builder().name("actions")
						.pacing(DurationRandom.builder().max(Durations.of("3000")).build())
						.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build())
						.build());
		assertNotNull(expectedProject);

		read("test-pacing-random-without-min", expectedProject);
	}

	@Test
	public void readConstantPacingAsVariablePassesThroughUnchanged() throws IOException {
		final Step transaction = Container.builder().name("MyTransaction")
				.pacing(DurationConstant.builder().value(Durations.of("${my_pacing}")).build())
				.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build())
				.build();
		final Project expectedProject = IOHelper.buildProject(transaction);
		assertNotNull(expectedProject);

		read("test-pacing-variable-constant", expectedProject);
	}

	@Test
	public void readRandomPacingBoundsAsVariablesPassThroughUnchanged() throws IOException {
		final Project expectedProject = IOHelper.buildProject(
				Container.builder().name("actions")
						.pacing(DurationRandom.builder().min(Durations.of("${min_pacing}")).max(Durations.of("${max_pacing}")).build())
						.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build())
						.build());
		assertNotNull(expectedProject);

		read("test-pacing-variable-random", expectedProject);
	}

	@Test
	public void writeAbsentPacingProducesNoPacingKey() throws IOException {
		final Project project = IOHelper.buildProject(
				Container.builder().name("actions")
						.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("100")).build()).build())
						.build());

		final String yaml = new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);
		assertFalse(yaml, yaml.contains("pacing"));

		final String json = new IO().write(ProjectDescriptor.builder().project(project).build(), Format.JSON);
		assertFalse(json, json.contains("pacing"));
	}

	@Test
	public void readPacingValuesAcceptedByThePattern() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-pacing-valid-values", "yaml"));
		assertNotNull(descriptor);

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void readPacingValuesRejectedAtRead() {
		for (final String value : new String[] {"abc", "-5s", "1.5s", "\"1.5\"", "1m1h", "1s1s", "1s5"}) {
			assertInvalidDurationRead(() -> readPacing(value), value.replace("\"", ""), "pacing");
		}
	}

	@Test
	public void readDurationsWithSpacesBetweenUnitsAndWriteThemWithoutSpaces() throws IOException {
		final Project expectedProject = IOHelper.buildProject(
				Container.builder().name("actions")
						.pacing(DurationRandom.builder().min(Durations.of("90000")).max(Durations.of("3725000")).build())
						.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("5415000")).build()).build())
						.build());
		assertNotNull(expectedProject);

		read("test-duration-spaces-between-units", expectedProject);

		final String yaml = new IO().write(ProjectDescriptor.builder().project(expectedProject).build(), Format.YAML);
		assertTrue(yaml, yaml.contains("1h30m15s"));
		assertTrue(yaml, yaml.contains("1h2m5s"));
		assertFalse(yaml, yaml.contains("1h 30m"));
	}

	@Test
	public void readDurationRandomRangesWhereMinIsNotGreaterThanMaxAreAccepted() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-pacing-random-valid-ranges", "yaml"));
		assertNotNull(descriptor);

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void readDurationRandomRangesWhereMinIsGreaterThanMaxAreRejected() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-pacing-random-invalid-ranges", "yaml"));

		assertViolations(new Validator().validate(descriptor, NeoLoad.class),
				invalidValue("project.user_paths[0].actions.steps[0].pacing", MIN_GREATER_THAN_MAX),
				invalidValue("project.user_paths[0].actions.steps[1].pacing", MIN_GREATER_THAN_MAX),
				invalidValue("project.user_paths[0].actions.steps[2].pacing", MIN_GREATER_THAN_MAX));
	}

	@Test
	public void readMixedConstantAndRangePacingFailsDeserialization() {
		final IO io = new IO();
		final File file = getFile("test-pacing-mixed", "yaml");
		try {
			io.read(file);
			fail("Expected reading a 'pacing' mixing a constant value with range bounds to fail");
		} catch (final IOException e) {
			assertReadError(e, VALUE_COMBINED_WITH_BOUNDS, "pacing");
		}
	}

	private static Project readPacing(final String pacingYaml) throws IOException {
		return new IO().read("name: P\nuser_paths:\n- name: U\n  actions:\n    pacing: " + pacingYaml + "\n    steps:\n    - delay: 1s\n").getProject();
	}

	private static Duration pacingOf(final Project project) {
		return project.getUserPaths().get(0).getActions().getPacing().get();
	}

	private static Validation validate(final Project project) {
		return new Validator().validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	@Test
	public void readPacingFormsAreNormalizedToMilliseconds() throws IOException {
		assertEquals(DurationConstant.builder().value(Durations.of("2000")).build(), pacingOf(readPacing("2s")));
		assertEquals(DurationConstant.builder().value(Durations.of("2000")).build(), pacingOf(readPacing("{ value: 2s }")));
		assertEquals(DurationRandom.builder().min(Durations.of("1000")).max(Durations.of("3000")).build(), pacingOf(readPacing("{ min: 1s, max: 3s }")));
		assertEquals(DurationConstant.builder().value(Durations.of("${myPacing}")).build(), pacingOf(readPacing("${myPacing}")));
		assertEquals(DurationConstant.builder().value(Durations.of("3601000")).build(), pacingOf(readPacing("1h 1s")));
	}

	@Test
	public void readPlainNumberPacingAsMillisecondsAndWriteItBack() throws IOException {
		final Project project = readPacing("1500");

		assertEquals(DurationConstant.builder().value(Durations.of("1500")).build(), pacingOf(project));
		final String yaml = new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);
		assertTrue(yaml, yaml.contains("pacing: 1s500ms"));
	}

	@Test
	public void readInvalidPacingDurationsFailTheRead() {
		assertInvalidDurationRead(() -> readPacing("abc"), "abc", "pacing");
		assertInvalidDurationRead(() -> readPacing("5x"), "5x", "pacing");
		assertInvalidDurationRead(() -> readPacing("{ value: abc }"), "abc", "pacing");
		assertInvalidDurationRead(() -> readPacing("{ min: abc, max: 5s }"), "abc", "pacing");
		assertInvalidDurationRead(() -> readPacing("{ max: 1.5.2 }"), "1.5.2", "pacing");
	}

	@Test
	public void readPacingWithOnlyMaxIsValidWithEmptyMin() throws IOException {
		final Project project = readPacing("{ max: 5s }");

		assertEquals(DurationRandom.builder().max(Durations.of("5000")).build(), pacingOf(project));
		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
	}

	@Test
	public void readPacingMinGreaterThanMaxIsRejected() throws IOException {
		for (final String pacing : new String[] {"{ min: 10s, max: 5s }", "{ min: 1s, max: 999 }"}) {
			assertViolations(validate(readPacing(pacing)), invalidValue("project.user_paths[0].actions.pacing", MIN_GREATER_THAN_MAX));
		}
	}

	@Test
	public void readPacingMinEqualToMaxIsAccepted() throws IOException {
		for (final String pacing : new String[] {"{ min: 5s, max: 5s }", "{ min: 1s, max: 1000 }", "{ max: 0 }"}) {
			final Validation validation = validate(readPacing(pacing));
			assertTrue(validation.getMessage().orElse(""), validation.isValid());
		}
	}

	@Test
	public void readPacingValueCombinedWithBoundIsRejected() {
		for (final String bound : new String[] {"min", "max"}) {
			try {
				readPacing("{ value: 2s, " + bound + ": 3s }");
				fail("Expected 'value' combined with '" + bound + "' to fail");
			} catch (final IOException e) {
				assertReadError(e, VALUE_COMBINED_WITH_BOUNDS, "pacing");
			}
		}
	}
}
