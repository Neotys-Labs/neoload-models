package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Condition.Operator;
import com.neotys.neoload.model.v3.project.userpath.Match;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class WaitUntilTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static final String CONSTRAINTS_NAME_REQUIRED = "Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR +
				"Violation 1 - Incorrect value for 'name': missing value or value is empty." + LINE_SEPARATOR;

	private static final String CONSTRAINTS_CONDITIONS_REQUIRED = "Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR +
				"Violation 1 - Incorrect value for 'conditions': missing value or value is empty." + LINE_SEPARATOR;

	private static final String CONSTRAINTS_CONDITION_OPERATOR_REQUIRED = "Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR +
				"Violation 1 - Incorrect value for 'conditions[0].operator': missing value or value is empty." + LINE_SEPARATOR;

	private static final String CONSTRAINTS_TIMEOUT_PATTERN = "Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR +
				"Violation 1 - Incorrect value for 'timeout': must match \"(\\d+|\\$\\{\\w+\\})\"" + LINE_SEPARATOR;

	private static Condition validCondition() {
		return Condition.builder()
				.operand1("${myVariable}")
				.operator(Operator.EQUALS)
				.operand2("expected")
				.build();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();

		WaitUntil waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.build();
		Validation validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		waitUntil = WaitUntil.builder()
				.name("")
				.addConditions(validCondition())
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_NAME_REQUIRED, validation.getMessage().get());

		waitUntil = WaitUntil.builder()
				.name(" \t\r\n")
				.addConditions(validCondition())
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_NAME_REQUIRED, validation.getMessage().get());

		waitUntil = WaitUntil.builder()
				.name("my_wait_until")
				.addConditions(validCondition())
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validateConditions() {
		final Validator validator = new Validator();

		WaitUntil waitUntil = WaitUntil.builder()
				.build();
		Validation validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONDITIONS_REQUIRED, validation.getMessage().get());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validateConditionOperator() {
		final Validator validator = new Validator();

		WaitUntil waitUntil = WaitUntil.builder()
				.addConditions(Condition.builder()
						.operand1("${myVariable}")
						.operand2("expected")
						.build())
				.build();
		Validation validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONDITION_OPERATOR_REQUIRED, validation.getMessage().get());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validateTimeout() {
		final Validator validator = new Validator();

		WaitUntil waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.build();
		Validation validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.timeout("30000")
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.timeout("${myTimeout}")
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.timeout("-30000")
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TIMEOUT_PATTERN, validation.getMessage().get());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.timeout("30 seconds")
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TIMEOUT_PATTERN, validation.getMessage().get());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.timeout("")
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TIMEOUT_PATTERN, validation.getMessage().get());
	}

	@Test
	public void validateMatch() {
		final Validator validator = new Validator();

		WaitUntil waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.match(Match.ALL)
				.build();
		Validation validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		waitUntil = WaitUntil.builder()
				.addConditions(validCondition())
				.match(Match.ANY)
				.build();
		validation = validator.validate(waitUntil, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
