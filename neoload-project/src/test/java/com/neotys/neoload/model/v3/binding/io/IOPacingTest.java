package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Case;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.Pacing;
import com.neotys.neoload.model.v3.project.userpath.PacingConstant;
import com.neotys.neoload.model.v3.project.userpath.PacingRandom;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.Switch;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class IOPacingTest extends AbstractIOElementsTest {

	private static Container withPacing(final Pacing pacing) {
		return Container.builder().pacing(pacing).addSteps(Delay.builder().value("100").build()).build();
	}

	private static Project buildProjectWithPacingEverywhere(final Pacing pacing) {
		final Container transaction = Container.builder()
				.name("MyTransaction")
				.pacing(pacing)
				.addSteps(Delay.builder().value("100").build())
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
						.addSteps(Delay.builder().value("100").build())
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
				.init(Container.builder().name("init").pacing(pacing).addSteps(Delay.builder().value("100").build()).build())
				.actions(actions)
				.end(Container.builder().name("end").pacing(pacing).addSteps(Delay.builder().value("100").build()).build())
				.build();

		return Project.builder().name("MyProject").addUserPaths(userPath).build();
	}

	// The "if" step's Condition/Match round-trip is a pre-existing gap (see IORoundTripTest's
	// exclusion of test-if-*), unrelated to pacing, so it is verified for reading only, separately
	// from the other container types above which are covered by IORoundTripTest as well.
	private static Project buildProjectWithPacingOnIfThenElse(final Pacing pacing) {
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
		final Project expectedProject = buildProjectWithPacingEverywhere(PacingConstant.builder().value("30s").build());
		assertNotNull(expectedProject);

		read("test-pacing-constant", expectedProject);
	}

	@Test
	public void readRandomPacingOnEveryContainerType() throws IOException {
		final Project expectedProject = buildProjectWithPacingEverywhere(
				PacingRandom.builder().min("1s").max("3s").build());
		assertNotNull(expectedProject);

		read("test-pacing-random", expectedProject);
	}

	@Test
	public void readConstantPacingOnIfThenElse() throws IOException {
		final Project expectedProject = buildProjectWithPacingOnIfThenElse(PacingConstant.builder().value("30s").build());
		assertNotNull(expectedProject);

		read("test-pacing-if-constant", expectedProject);
	}

	@Test
	public void readRandomPacingOnIfThenElse() throws IOException {
		final Project expectedProject = buildProjectWithPacingOnIfThenElse(
				PacingRandom.builder().min("1s").max("3s").build());
		assertNotNull(expectedProject);

		read("test-pacing-if-random", expectedProject);
	}

	@Test
	public void readRandomPacingWithoutMinDefaultsToZero() throws IOException {
		final Project expectedProject = IOHelper.buildProject(
				Container.builder().name("actions")
						.pacing(PacingRandom.builder().max("3s").build())
						.addSteps(Delay.builder().value("100").build())
						.build());
		assertNotNull(expectedProject);

		read("test-pacing-random-without-min", expectedProject);
	}

	@Test
	public void readConstantPacingAsVariablePassesThroughUnchanged() throws IOException {
		final Step transaction = Container.builder().name("MyTransaction")
				.pacing(PacingConstant.builder().value("${my_pacing}").build())
				.addSteps(Delay.builder().value("100").build())
				.build();
		final Project expectedProject = IOHelper.buildProject(transaction);
		assertNotNull(expectedProject);

		read("test-pacing-variable-constant", expectedProject);
	}

	@Test
	public void readRandomPacingBoundsAsVariablesPassThroughUnchanged() throws IOException {
		final Project expectedProject = IOHelper.buildProject(
				Container.builder().name("actions")
						.pacing(PacingRandom.builder().min("${min_pacing}").max("${max_pacing}").build())
						.addSteps(Delay.builder().value("100").build())
						.build());
		assertNotNull(expectedProject);

		read("test-pacing-variable-random", expectedProject);
	}

	@Test
	public void writeAbsentPacingProducesNoPacingKey() throws IOException {
		final Project project = IOHelper.buildProject(
				Container.builder().name("actions")
						.addSteps(Delay.builder().value("100").build())
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
	public void readPacingValuesRejectedByThePattern() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-pacing-invalid-values", "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());

		final String message = validation.getMessage().get();
		assertTrue(message, message.contains("Violation Number: 3."));
	}

	@Test
	public void readMixedConstantAndRangePacingFailsDeserialization() {
		final IO io = new IO();
		final File file = getFile("test-pacing-mixed", "yaml");
		try {
			io.read(file);
			fail("Expected reading a 'pacing' mixing a constant value with range bounds to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("pacing"));
		}
	}
}
