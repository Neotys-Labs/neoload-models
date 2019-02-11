package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdCheck.UsageType;


public class SlaThresholdValidatorTest {
	@Test
	public void isValid() {
		SlaThresholdValidator validator = new SlaThresholdValidator();
		validator.usage = UsageType.CHECK_RELATIONSHIP_KPI_AND_SCOPE;
		assertTrue(validator.isValid(null, null));		
		assertTrue(validator.isValid(SlaThreshold.builder().build(), null));

		validator = new SlaThresholdValidator();
		validator.usage = UsageType.CHECK_UNIQUE_CONDITION_SEVERITY;
		assertTrue(validator.isValid(null, null));		
		assertTrue(validator.isValid(SlaThreshold.builder().build(), null));
		assertTrue(validator.isValid(SlaThreshold.builder().addConditions(SlaThresholdCondition.builder().build()).build(),	null));
	}
}
