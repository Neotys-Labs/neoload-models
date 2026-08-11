package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class CaughtExceptionsValidatorTest {

	@Test
	public void caughtExceptionsAbsent_isValid() {
		final CaughtExceptionsValidator validator = new CaughtExceptionsValidator();
		assertTrue(validator.isValid(TryCatch.builder()
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("500").build())
						.build())
				.build(), null));
	}

	@Test
	public void caughtExceptionsNonEmpty_isValid() {
		final CaughtExceptionsValidator validator = new CaughtExceptionsValidator();
		assertTrue(validator.isValid(TryCatch.builder()
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("500").build())
						.build())
				.caughtExceptions(Arrays.asList(TryCatch.CaughtException.errors))
				.build(), null));
	}

	@Test
	public void caughtExceptionsEmpty_isInvalid() {
		final CaughtExceptionsValidator validator = new CaughtExceptionsValidator();
		assertFalse(validator.isValid(TryCatch.builder()
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("500").build())
						.build())
				.caughtExceptions(Collections.emptyList())
				.build(), null));
	}
}
