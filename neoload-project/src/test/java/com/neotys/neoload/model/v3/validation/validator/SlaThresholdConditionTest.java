package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class SlaThresholdConditionTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();
	

	private static final String CONSTRAINTS_SLA_THRESHOLD_CONDITION_SEVERITY_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'severity': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_CONDITION_SEVERITY_NULL = sb.toString();
	}
	
	private static final String CONSTRAINTS_SLA_THRESHOLD_CONDITION_OPERATOR_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'operator': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_CONDITION_OPERATOR_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_THRESHOLD_CONDITION_VALUE_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'value': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_CONDITION_VALUE_NULL = sb.toString();
	}
	
	private static final String CONSTRAINTS_SLA_THRESHOLD_CONDITION_VALUE_NEGATIVE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'value': must be greater than or equal to 0.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_THRESHOLD_CONDITION_VALUE_NEGATIVE = sb.toString();
	}


	@Test
	public void validateSeverity() {
		final Validator validator = new Validator();
		
		SlaThresholdCondition condition = SlaThresholdCondition.builder()
				.operator(Operator.GREATER_THAN)
				.value(1.0)
				.build();
		Validation validation = validator.validate(condition, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_CONDITION_SEVERITY_NULL, validation.getMessage().get());	

		condition = SlaThresholdCondition.builder()
				.severity(Severity.WARN)
				.operator(Operator.GREATER_THAN)
				.value(1.0)
				.build();
		validation = validator.validate(condition, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateOperator() {
		final Validator validator = new Validator();
		
		SlaThresholdCondition condition = SlaThresholdCondition.builder()
				.severity(Severity.WARN)
				.value(1.0)
				.build();
		Validation validation = validator.validate(condition, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_CONDITION_OPERATOR_NULL, validation.getMessage().get());	

		condition = SlaThresholdCondition.builder()
				.severity(Severity.WARN)
				.operator(Operator.GREATER_THAN)
				.value(1.0)
				.build();
		validation = validator.validate(condition, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		SlaThresholdCondition condition = SlaThresholdCondition.builder()
				.severity(Severity.WARN)
				.operator(Operator.GREATER_THAN)
				.build();
		Validation validation = validator.validate(condition, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_CONDITION_VALUE_NULL, validation.getMessage().get());	

		condition = SlaThresholdCondition.builder()
				.severity(Severity.WARN)
				.operator(Operator.GREATER_THAN)
				.value(-1.0)
				.build();
		validation = validator.validate(condition, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_THRESHOLD_CONDITION_VALUE_NEGATIVE, validation.getMessage().get());	

		condition = SlaThresholdCondition.builder()
				.severity(Severity.WARN)
				.operator(Operator.GREATER_THAN)
				.value(1.0)
				.build();
		validation = validator.validate(condition, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
