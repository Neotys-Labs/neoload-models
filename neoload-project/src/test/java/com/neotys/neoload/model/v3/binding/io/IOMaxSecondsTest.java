package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;

import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * A second-based duration read from YAML is stored by NeoLoad as an int number of milliseconds: above 2147483
 * seconds it is rejected, the violation naming the field, the value and the allowed range.
 */
public class IOMaxSecondsTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String OVER_MAX = "2147484";
	private static final String OVER_MAX_VALUE = "596h31m24s";
	private static final String RANGE = "must be between 1 and 2147483 seconds.";
	private static final String SCENARIO = "project.scenarios[0]";

	private static Validation validate(final String yaml) throws IOException {
		return VALIDATOR.validate(new IO().read(yaml), NeoLoad.class);
	}

	@Test
	public void rampupWrittenInHoursAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.CONSTANT_RAMPUP.yaml("600h"));

		assertViolations(validation, invalidValue("600h", SCENARIO + ".populations[0].constant_load.rampup", RANGE));
	}

	@Test
	public void rampupAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.CONSTANT_RAMPUP.yaml(OVER_MAX));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".populations[0].constant_load.rampup", RANGE));
	}

	@Test
	public void incrementRampupAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.RAMPUP_INCREMENT_RAMPUP.yaml(OVER_MAX));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".populations[0].rampup_load.increment_rampup", RANGE));
	}

	@Test
	public void stepRampupAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.PEAKS_STEP_RAMPUP.yaml(OVER_MAX));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".populations[0].peaks_load.step_rampup", RANGE));
	}

	@Test
	public void startAfterAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: " + OVER_MAX + "\n"));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".populations[0].constant_load.start_after",
				"must be between 1 and 2147483 seconds or must be an existing population."));
	}

	@Test
	public void stopAfterAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      stop_after: " + OVER_MAX + "\n"));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".populations[0].constant_load.stop_after",
				"must be between 1 and 2147483 seconds or must be current_iteration."));
	}

	@Test
	public void monitoringBeforeAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.MONITORING_BEFORE.yaml(OVER_MAX));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".monitoring.before", RANGE));
	}

	@Test
	public void monitoringAfterAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.MONITORING_AFTER.yaml(OVER_MAX));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".monitoring.after", RANGE));
	}

	@Test
	public void rendezvousTimeoutAboveMaxIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.RENDEZVOUS_TIMEOUT.yaml(OVER_MAX));

		assertViolations(validation, invalidValue(OVER_MAX_VALUE, SCENARIO + ".rendezvous_policies[0].timeout", RANGE));
	}

	@Test
	public void zeroMonitoringBeforeIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.MONITORING_BEFORE.yaml("0"));

		assertViolations(validation, invalidValue("0", SCENARIO + ".monitoring.before", "must be greater than or equal to 1 second."));
	}

	@Test
	public void zeroMonitoringAfterIsRejected() throws IOException {
		final Validation validation = validate(DurationFields.MONITORING_AFTER.yaml("0"));

		assertViolations(validation, invalidValue("0", SCENARIO + ".monitoring.after", "must be greater than or equal to 1 second."));
	}
}
