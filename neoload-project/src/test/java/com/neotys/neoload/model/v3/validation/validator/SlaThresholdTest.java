package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KeyPerformanceIndicator;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class SlaThresholdTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();
	

	private static final String CONSTRAINTS_SLA_THRESHOLD_KEY_PERFORMANCE_INDICATOR_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'key_performance_indicator': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_KEY_PERFORMANCE_INDICATOR_NULL = sb.toString();
	}
	
	private static final String CONSTRAINTS_SLA_THRESHOLD_CONDITIONS_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'conditions': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_CONDITIONS_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_THRESHOLD_RELATIONSHIP_KPI_AND_SCOPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid key performance indicator usage (AVG_REQUEST_RESP_TIME can not be used with the scope ON_INTERVAL).").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_RELATIONSHIP_KPI_AND_SCOPE = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_THRESHOLD_UNIQUE_CONDITION_SEVERITY;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must contain only unique severities.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_UNIQUE_CONDITION_SEVERITY = sb.toString();
	}

	@Test
	public void validateKeyPerformanceIndicator() {
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
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_KEY_PERFORMANCE_INDICATOR_NULL, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.keyPerformanceIndicator(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.scope(Scope.ON_INTERVAL)
				.build();
		validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_RELATIONSHIP_KPI_AND_SCOPE, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.keyPerformanceIndicator(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME)
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
				.keyPerformanceIndicator(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME)
				.build();
		Validation validation = validator.validate(threshold, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_CONDITIONS_NULL, validation.getMessage().get());	

		threshold = SlaThreshold.builder()
				.keyPerformanceIndicator(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME)
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
				.keyPerformanceIndicator(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME)
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
				.keyPerformanceIndicator(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME)
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
}
