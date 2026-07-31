package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.VariableModifier;

public class VariableModifierValidatorTest {

	@Test
	public void nullIsValid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(null, null));
	}

	@Test
	public void categoryPredefined_modeNextValue_isValid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.PREDEFINED)
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.variableName("myVar")
				.build(), null));
	}

	@Test
	public void categoryPredefined_modeNextValue_valueIsPresent_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.PREDEFINED)
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.variableName("myVar")
				.value("myValue")
				.build(), null));
	}

	@Test
	public void categoryPredefined_modeAddSharedQueueValue_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.PREDEFINED)
				.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
				.variableName("myVar")
				.build(), null));
	}

	@Test
	public void categorySharedQueue_modeAddSharedQueueValue_isValid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
				.variableName("mySharedVar")
				.value("${localVar}")
				.build(), null));
	}

	@Test
	public void categorySharedQueue_modeAddSharedQueueValue_valueIsNull_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
				.variableName("mySharedVar")
				.build(), null));
	}

	@Test
	public void categorySharedQueue_modePollSharedQueue_WithValue_isValid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
				.variableName("mySharedVar")
				.value("localVar")
				.build(), null));
	}

	@Test
	public void categorySharedQueue_modePollSharedQueue_valueIsNull_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
				.variableName("mySharedVar")
				.build(), null));
	}

	@Test
	public void categorySharedQueue_modeNextValue_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.variableName("mySharedVar")
				.value("${localVar}")
				.build(), null));
	}

	@Test
	public void categoryDefault_modeNextValue_isValid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.variableName("myVar")
				.build(), null));
	}

	@Test
	public void categoryDefault_modeAddSharedQueueValue_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
				.variableName("myVar")
				.build(), null));
	}

	@Test
	public void categoryDefault_modePollSharedQueue_isInvalid() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
				.variableName("myVar")
				.build(), null));
	}
}