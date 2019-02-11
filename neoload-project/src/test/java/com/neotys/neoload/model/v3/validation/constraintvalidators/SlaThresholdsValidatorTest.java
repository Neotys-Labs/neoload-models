package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdsCheck.UsageType;


public class SlaThresholdsValidatorTest {
	@Test
	public void isValid() {
		SlaThresholdsValidator validator = new SlaThresholdsValidator();
		validator.usage = UsageType.CHECK_UNIQUE_KPI_AND_SCOPE;
		assertTrue(validator.isValid(null, null));		
		assertTrue(validator.isValid(Collections.emptyList(), null));

		validator = new SlaThresholdsValidator();
		validator.usage = UsageType.CHECK_LIST_OF_KPIS_FROM_ELEMENT;
		validator.from = SlaProfile.class;
		assertTrue(validator.isValid(null, null));		
		assertTrue(validator.isValid(Collections.emptyList(), null));
	}
}
