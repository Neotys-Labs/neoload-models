package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
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
}