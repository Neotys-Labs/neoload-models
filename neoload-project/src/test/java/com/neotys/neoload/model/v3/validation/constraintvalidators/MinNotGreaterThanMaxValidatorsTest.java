package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import com.neotys.neoload.model.v3.project.variable.RandomStringVariable;
import org.junit.Test;

public class MinNotGreaterThanMaxValidatorsTest {

	private static RampupLoadPolicy.Builder rampupLoad(final int minUsers) {
		return RampupLoadPolicy.builder()
				.minUsers(minUsers)
				.incrementUsers(1)
				.incrementEvery(LoadDuration.builder().value(1).type(LoadDuration.Type.TIME).build());
	}

	@Test
	public void durationRandom() {
		final DurationRandomMinNotGreaterThanMaxValidator validator = new DurationRandomMinNotGreaterThanMaxValidator();

		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid(DurationRandom.builder().min("1000").max("3000").build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().min("3000").max("3000").build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().max("0").build(), null));
		assertFalse(validator.isValid(DurationRandom.builder().min("3000").max("1000").build(), null));

		assertTrue(validator.isValid(DurationRandom.builder().min("${min_pacing}").max("1000").build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().min("5000").max("abc").build(), null));
	}

	@Test
	public void rampupLoadPolicy() {
		final RampupLoadPolicyMinNotGreaterThanMaxValidator validator = new RampupLoadPolicyMinNotGreaterThanMaxValidator();

		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid(rampupLoad(10).maxUsers(10).build(), null));
		assertTrue(validator.isValid(rampupLoad(10).build(), null));
		assertFalse(validator.isValid(rampupLoad(10).maxUsers(5).build(), null));
	}

	@Test
	public void randomNumberVariable() {
		final RandomNumberVariableMinNotGreaterThanMaxValidator validator = new RandomNumberVariableMinNotGreaterThanMaxValidator();

		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid(RandomNumberVariable.builder().name("MyRandomNumber").min(-10).max(10).build(), null));
		assertTrue(validator.isValid(RandomNumberVariable.builder().name("MyRandomNumber").min(10).max(10).build(), null));
		assertFalse(validator.isValid(RandomNumberVariable.builder().name("MyRandomNumber").min(11).max(10).build(), null));
	}

	@Test
	public void randomStringVariable() {
		final RandomStringVariableMinNotGreaterThanMaxValidator validator = new RandomStringVariableMinNotGreaterThanMaxValidator();

		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid(RandomStringVariable.builder().name("MyRandomString").minLength(10).maxLength(10).build(), null));
		assertFalse(validator.isValid(RandomStringVariable.builder().name("MyRandomString").minLength(20).maxLength(10).build(), null));
	}
}
