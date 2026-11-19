package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.MISSING_VALUE;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.time.Duration;
import org.junit.Test;

/**
 * A violation on a number or a duration names the rejected value, as written in the YAML; a violation on any
 * other field names only the field.
 */
public class IOViolationValueTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String CONSTANT_LOAD = "project.scenarios[0].populations[0].constant_load";

	private static Validation validate(final String yaml) throws IOException {
		return VALIDATOR.validate(new IO().read(yaml), NeoLoad.class);
	}

	private static Validation validate(final Project project) {
		return VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	@Test
	public void numberViolationNamesTheFieldAndTheRejectedValue() throws IOException {
		final Validation validation = validate(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 0\n"));

		assertViolations(validation, invalidValue("0", CONSTANT_LOAD + ".users", "must be greater than or equal to 1 user."));
	}

	@Test
	public void negativeDurationViolationNamesTheValueWithALeadingMinus() {
		final ConstantLoadPolicy policy = ConstantLoadPolicy.builder().users(1)
				.stopAfter(StopAfter.builder().type(StopAfter.Type.TIME).value(Duration.ofSeconds(-5)).build()).build();
		final Project project = Project.builder().name("P").addScenarios(Scenario.builder().name("S")
				.addPopulations(PopulationPolicy.builder().name("C").loadPolicy(policy).build()).build()).build();

		assertViolations(validate(project), invalidValue("-5s", CONSTANT_LOAD + ".stop_after", "must be between 1 and 2147483 seconds or must be current_iteration."));
	}

	@Test
	public void compositeViolationNamesTheValueItWraps() throws IOException {
		final Validation validation = validate(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: 0\n"));

		assertViolations(validation, invalidValue("0", CONSTANT_LOAD + ".start_after", "must be between 1 and 2147483 seconds or must be an existing population."));
	}

	@Test
	public void textViolationNamesOnlyTheField() throws IOException {
		final Validation validation = validate(DurationFields.actions("    - delay: 1s\n").replace("- name: U", "- name: \"\""));

		assertViolations(validation, invalidValue("project.user_paths[0].name", MISSING_VALUE));
	}
}
