package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import org.junit.Test;

public class DurationSignValidatorsTest {

	private static DurationConstant constant(final String millisecondsOrVariable) {
		return DurationConstant.builder().value(Durations.of(millisecondsOrVariable)).build();
	}

	@Test
	public void nonZeroDurationRejectsOnlyAConstantLiteralZero() {
		final NonZeroDurationValidator validator = new NonZeroDurationValidator();

		assertTrue(validator.isValid(null, null));
		assertFalse(validator.isValid(constant("0"), null));
		assertTrue(validator.isValid(constant("1"), null));
		assertTrue(validator.isValid(constant("${myVar}"), null));
		assertTrue(validator.isValid(DurationRandom.builder().max(Durations.of("0")).build(), null));
	}

	@Test
	public void positiveDurationOrVariableRejectsZeroAndNegativeLiterals() {
		final PositiveDurationOrVariableValidator validator = new PositiveDurationOrVariableValidator();

		assertTrue(validator.isValid(null, null));
		assertFalse(validator.isValid(Durations.of("0"), null));
		assertFalse(validator.isValid(Durations.of("-1"), null));
		assertTrue(validator.isValid(Durations.of("1"), null));
		assertTrue(validator.isValid(Durations.of("${myVar}"), null));
	}

	@Test
	public void positiveOrZeroDurationOrVariableRejectsNegativeLiterals() {
		final PositiveOrZeroDurationOrVariableValidator validator = new PositiveOrZeroDurationOrVariableValidator();

		assertTrue(validator.isValid(null, null));
		assertFalse(validator.isValid(Durations.of("-1"), null));
		assertTrue(validator.isValid(Durations.of("0"), null));
		assertTrue(validator.isValid(Durations.of("${myVar}"), null));
	}

	@Test
	public void nonZeroNumberRejectsZero() {
		final NonZeroValidator validator = new NonZeroValidator();

		assertTrue(validator.isValid(null, null));
		assertFalse(validator.isValid(0, null));
		assertTrue(validator.isValid(1, null));
		assertTrue(validator.isValid(-1, null));
	}
}
