package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class SlaProfileTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();
	
	private static final String CONSTRAINTS_SLA_PROFILE_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_PROFILE_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_PROFILE_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_PROFILE_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_PROFILE_THRESHOLDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'thresholds': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_PROFILE_THRESHOLDS = sb.toString();
	}
	
	private static final String CONSTRAINTS_SLA_PROFILE_THRESHOLDS_UNIQUE_KPI_AND_SCOPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'thresholds': invalid KPI thresholds usage (must contain only unique KPI/scopes).").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_PROFILE_THRESHOLDS_UNIQUE_KPI_AND_SCOPE = sb.toString();
	}

	private static final String CONSTRAINTS_SLA_PROFILE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 3.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'sla_profiles[0].name': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'sla_profiles[0].thresholds[0].conditions[0].severity': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 3 - Incorrect value for 'sla_profiles[0].thresholds[0].kpi': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SLA_PROFILE = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		SlaProfile profile = SlaProfile.builder()
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build())
				.build();
		Validation validation = validator.validate(profile, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_PROFILE_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		profile = SlaProfile.builder()
				.name("")
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build())
				.build();
		validation = validator.validate(profile, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_PROFILE_NAME_BLANK, validation.getMessage().get());	

		profile = SlaProfile.builder()
				.name(" 	\r\t\n")
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build())
				.build();
		validation = validator.validate(profile, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_PROFILE_NAME_BLANK, validation.getMessage().get());	

		profile = SlaProfile.builder()
				.name("MySlaProfile")
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build())
				.build();
		validation = validator.validate(profile, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateThresholds() {
		final Validator validator = new Validator();
		
		SlaProfile profile = SlaProfile.builder()
				.name("MySlaProfile")
				.build();
		Validation validation = validator.validate(profile, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_PROFILE_THRESHOLDS, validation.getMessage().get());	

		profile = SlaProfile.builder()
				.name("MySlaProfile")
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build())
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.FAIL)
								.operator(Operator.GREATER_THAN)
								.value(2.0)
								.build())
						.build())
				.build();
		validation = validator.validate(profile, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_PROFILE_THRESHOLDS_UNIQUE_KPI_AND_SCOPE, validation.getMessage().get());	

		profile = SlaProfile.builder()
				.name("MySlaProfile")
				.addThresholds(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build())
				.build();
		validation = validator.validate(profile, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validateInDepth() {
		final Validator validator = new Validator();
		
		final SlaProfile profile = SlaProfile.builder()
				.addThresholds(SlaThreshold.builder()
						.addConditions(SlaThresholdCondition.builder()
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.scope(Scope.PER_INTERVAL)
						.build())
				.build();
        
        final Project project = Project.builder()
        		.name("MyProject")
        		.addSlaProfiles(profile)
        		.build();
        
		Validation validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SLA_PROFILE, validation.getMessage().get());	
	}	
}
