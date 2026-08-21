package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.RandomUUIDVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class RandomUUIDVariableTest {

	@Test
	public void validateDefaults() {
		final Validator validator = new Validator();

		final RandomUUIDVariable randomUUIDVariable = RandomUUIDVariable.builder()
				.name("MyRandomUUID")
				.build();
		final Validation validation = validator.validate(randomUUIDVariable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
		assertFalse(randomUUIDVariable.isUpperCase());
		assertFalse(randomUUIDVariable.isPredictable());
	}
}
