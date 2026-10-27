package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.IterationLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import java.time.Duration;
import org.junit.Test;

public class DurationValidatorTest {
	@Test
	public void isValid() {
		assertFalse(new DurationValidator().isValid(null, null));
		
		
		
		assertFalse(new DurationValidator().isValid(TimeLoadDuration.builder().value(Duration.ofSeconds(-1)).build(), null));
		assertFalse(new DurationValidator().isValid(IterationLoadDuration.builder().count(-1).build(), null));
		
		assertFalse(new DurationValidator().isValid(TimeLoadDuration.builder().value(Duration.ofSeconds(0)).build(), null));
		assertFalse(new DurationValidator().isValid(IterationLoadDuration.builder().count(0).build(), null));
		
		assertTrue(new DurationValidator().isValid(TimeLoadDuration.builder().value(Duration.ofSeconds(1)).build(), null));
		assertTrue(new DurationValidator().isValid(IterationLoadDuration.builder().count(1).build(), null));
	}
}
