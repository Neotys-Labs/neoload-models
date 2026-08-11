package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class TryCatchTest {

	@Test
	public void validateTryIsRequired() {
		final Validator validator = new Validator();

		final TryCatch tryCatchWithoutTry = TryCatch.builder().build();
		final Validation invalidValidation = validator.validate(tryCatchWithoutTry, NeoLoad.class);
		assertFalse(invalidValidation.isValid());

		final TryCatch tryCatchWithTry = TryCatch.builder()
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("1000").build())
						.build())
				.build();
		final Validation validValidation = validator.validate(tryCatchWithTry, NeoLoad.class);
		assertTrue(validValidation.isValid());
	}

	@Test
	public void validateCaughtExceptionsMustNotBeEmptyWhenPresent() {
		final Validator validator = new Validator();

		final TryCatch tryCatchWithoutCaughtExceptions = TryCatch.builder()
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("1000").build())
						.build())
				.build();
		assertTrue(validator.validate(tryCatchWithoutCaughtExceptions, NeoLoad.class).isValid());

		final TryCatch tryCatchWithCaughtExceptions = TryCatch.builder()
				.caughtExceptions(Arrays.asList(TryCatch.CaughtException.errors))
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("1000").build())
						.build())
				.build();
		assertTrue(validator.validate(tryCatchWithCaughtExceptions, NeoLoad.class).isValid());

		final TryCatch tryCatchWithEmptyCaughtExceptions = TryCatch.builder()
				.caughtExceptions(Collections.emptyList())
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("1000").build())
						.build())
				.build();
		final Validation invalidValidation = validator.validate(tryCatchWithEmptyCaughtExceptions, NeoLoad.class);
		assertFalse(invalidValidation.isValid());
	}
}