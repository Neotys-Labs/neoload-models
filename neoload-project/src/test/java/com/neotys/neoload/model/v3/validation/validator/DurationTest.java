package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.IterationLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
import org.junit.Test;

public class DurationTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_VALUE_MIN;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE_MIN = sb.toString();
	}

	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		LoadDuration duration = TimeLoadDuration.builder().build();
		Validation validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = IterationLoadDuration.builder().build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = TimeLoadDuration.builder().value(Duration.ofSeconds(-10)).build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = IterationLoadDuration.builder().count(-10).build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = TimeLoadDuration.builder().value(Duration.ofSeconds(0)).build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = IterationLoadDuration.builder().count(0).build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = TimeLoadDuration.builder().value(Duration.ofSeconds(1)).build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		duration = IterationLoadDuration.builder().count(1).build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	
}
