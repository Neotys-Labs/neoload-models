package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import org.junit.Test;

public class MinNotGreaterThanMaxValidatorTest {

	private static RandomNumberVariable randomNumber(final int min, final int max) {
		return RandomNumberVariable.builder().name("MyRandomNumber").min(min).max(max).build();
	}

	@Test
	public void isValid() {
		final MinNotGreaterThanMaxValidator validator = new MinNotGreaterThanMaxValidator();

		assertTrue(validator.isValid(null, null));

		assertTrue(validator.isValid(randomNumber(-10, 10), null));
		assertTrue(validator.isValid(randomNumber(10, 10), null));
		assertFalse(validator.isValid(randomNumber(11, 10), null));

		assertTrue(validator.isValid(DurationRandom.builder().max("0").build(), null));
		assertFalse(validator.isValid(DurationRandom.builder().min("3000").max("1000").build(), null));
	}

	@Test
	public void isValidWhenABoundCannotBeCompared() {
		final MinNotGreaterThanMaxValidator validator = new MinNotGreaterThanMaxValidator();

		assertTrue(validator.isValid(DurationRandom.builder().min("${min_pacing}").max("1000").build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().min("5000").max("abc").build(), null));
		assertTrue(validator.isValid(RampupLoadPolicy.builder()
				.minUsers(10)
				.incrementUsers(1)
				.incrementEvery(LoadDuration.builder().value(1).type(LoadDuration.Type.TIME).build())
				.build(), null));
	}
}
