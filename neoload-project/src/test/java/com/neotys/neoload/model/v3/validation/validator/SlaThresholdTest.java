package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class SlaThresholdTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();
	

	private static final String CONSTRAINTS_SLA_THRESHOLD_KPI_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'kpi': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_KPI_NULL = sb.toString();
	}
	
	private static final String CONSTRAINTS_SLA_THRESHOLD_CONDITIONS_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'conditions': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_CONDITIONS_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_THRESHOLD_RELATIONSHIP_KPI_AND_SCOPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid KPI threshold usage (AVG_REQUEST_RESP_TIME can not be used with the scope: PER_INTERVAL).").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_RELATIONSHIP_KPI_AND_SCOPE = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_THRESHOLD_UNIQUE_CONDITION_SEVERITY;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid KPI threshold usage (must contain only unique severities).").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_UNIQUE_CONDITION_SEVERITY = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_THRESHOLD_PERCENT_OUT_OF_RANGE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value '%s' for 'percent': must be between 1 and 99.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_PERCENT_OUT_OF_RANGE = sb.toString();
	}

	@Test
	public void validateKpi() {
		final Validator validator = new Validator();
		
		SlaThreshold threshold = SlaThreshold.builder()
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.build();
		Validation validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_KPI_NULL, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.scope(Scope.PER_INTERVAL)
				.build();
		validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_RELATIONSHIP_KPI_AND_SCOPE, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.build();
		validation = validator.validate(threshold, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateConditions() {
		final Validator validator = new Validator();
		
		SlaThreshold threshold = SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.build();
		Validation validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_CONDITIONS_NULL, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(2.0)
						.build())
				.build();
		validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_UNIQUE_CONDITION_SEVERITY, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.FAIL)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.FAIL)
						.operator(Operator.GREATER_THAN)
						.value(2.0)
						.build())
				.build();
		validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_UNIQUE_CONDITION_SEVERITY, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.FAIL)
						.operator(Operator.GREATER_THAN)
						.value(2.0)
						.build())
				.build();
		validation = validator.validate(threshold, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	private static SlaThreshold percentile(final int percent) {
		return SlaThreshold.builder()
				.kpi(KPI.PERC_TRANSACTION_RESP_TIME)
				.percent(percent)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.build();
	}

	@Test
	public void validatePercent() {
		final Validator validator = new Validator();

		assertTrue(validator.validate(percentile(1), NeoLoad.class).isValid());
		assertTrue(validator.validate(percentile(99), NeoLoad.class).isValid());

		Validation validation = validator.validate(percentile(0), NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_PERCENT_OUT_OF_RANGE.replace("%s", "0"), validation.getMessage().get());

		validation = validator.validate(percentile(100), NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_PERCENT_OUT_OF_RANGE.replace("%s", "100"), validation.getMessage().get());
	}
}
