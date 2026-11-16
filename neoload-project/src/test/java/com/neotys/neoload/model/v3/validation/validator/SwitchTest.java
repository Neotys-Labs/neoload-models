package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Case;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import com.neotys.neoload.model.v3.project.userpath.Switch;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class SwitchTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static Case aCase(final String value) {
		return Case.builder().value(value).isBreak(true)
				.addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("1000")).build()).build()).build();
	}

	@Test
	public void validateUniqueCaseValues() {
		final Switch aSwitch = Switch.builder().value("x").addCases(aCase("1"), aCase("2")).build();
		assertTrue(new Validator().validate(aSwitch, NeoLoad.class).isValid());
	}

	@Test
	public void validateCaseValueWithCharactersForbiddenInNames() {
		final Switch aSwitch = Switch.builder().value("x").addCases(aCase("a#b"), aCase("$")).build();
		assertTrue(new Validator().validate(aSwitch, NeoLoad.class).isValid());
	}

	@Test
	public void validateLongCaseValue() {
		final Switch aSwitch = Switch.builder().value("x").addCases(aCase("v".repeat(150))).build();
		assertTrue(new Validator().validate(aSwitch, NeoLoad.class).isValid());
	}

	@Test
	public void validateDuplicateCaseValues() {
		final Switch aSwitch = Switch.builder().value("x").addCases(aCase("1"), aCase("2"), aCase("1")).build();
		assertEquals("Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR
				+ "Violation 1 - Incorrect value for 'cases': must contain only unique values." + LINE_SEPARATOR,
				new Validator().validate(aSwitch, NeoLoad.class).getMessage().get());
	}
}
