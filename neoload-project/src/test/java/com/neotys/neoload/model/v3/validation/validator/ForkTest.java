package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Fork;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class ForkTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static final String CONSTRAINTS_FORK_STEPS= "Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR +
                "Violation 1 - Incorrect value for 'steps': missing value or value is empty." + LINE_SEPARATOR;


	@Test
	public void validateFork_StepsIsNull_mustBeInvalid() {
		final Validator validator = new Validator();

		Fork fork = Fork.builder().build();
		Validation validation = validator.validate(fork, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_FORK_STEPS, validation.getMessage().get());
	}

	@Test
	public void validateFork_StepsIsNotEmpty_mustBeValid() {
		final Validator validator = new Validator();
		Fork fork = Fork.builder()
				.addSteps(Delay.builder().value("1000").build())
				.build();
		Validation validation = validator.validate(fork, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
