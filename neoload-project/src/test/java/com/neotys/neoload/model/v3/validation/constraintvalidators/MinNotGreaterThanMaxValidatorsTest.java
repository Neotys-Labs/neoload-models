package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.Durations;
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
		assertTrue(validator.isValid(DurationRandom.builder().min(Durations.of("1000")).max(Durations.of("3000")).build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().min(Durations.of("3000")).max(Durations.of("3000")).build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().max(Durations.of("0")).build(), null));
		assertFalse(validator.isValid(DurationRandom.builder().min(Durations.of("3000")).max(Durations.of("1000")).build(), null));

		assertTrue(validator.isValid(DurationRandom.builder().min(Durations.of("${min_pacing}")).max(Durations.of("1000")).build(), null));
		assertTrue(validator.isValid(DurationRandom.builder().min(Durations.of("5000")).max(Durations.of("${max_pacing}")).build(), null));
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
