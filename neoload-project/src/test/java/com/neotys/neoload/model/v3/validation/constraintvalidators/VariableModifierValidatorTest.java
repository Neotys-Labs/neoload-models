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
	public void definedWithVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.DEFINED)
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.variableName("myVar")
				.build(), null));
	}

	@Test
	public void definedWithoutVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.DEFINED)
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.build(), null));
	}

	@Test
	public void sharedWithSharedVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.mode(VariableModifier.Mode.ADD_VALUE)
				.sharedVariableName("mySharedVar")
				.srcVariableName("${localVar}")
				.build(), null));
	}

	@Test
	public void sharedWithoutSharedVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.mode(VariableModifier.Mode.ADD_VALUE)
				.srcVariableName("${localVar}")
				.build(), null));
	}

	@Test
	public void addValueWithSrcVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.mode(VariableModifier.Mode.ADD_VALUE)
				.sharedVariableName("mySharedVar")
				.srcVariableName("${localVar}")
				.build(), null));
	}

	@Test
	public void addValueWithoutSrcVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.mode(VariableModifier.Mode.ADD_VALUE)
				.sharedVariableName("mySharedVar")
				.build(), null));
	}

	@Test
	public void getValueWithDestVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.mode(VariableModifier.Mode.GET_VALUE)
				.sharedVariableName("mySharedVar")
				.destVariableName("localVar")
				.build(), null));
	}

	@Test
	public void getValueWithoutDestVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.mode(VariableModifier.Mode.GET_VALUE)
				.sharedVariableName("mySharedVar")
				.build(), null));
	}

	@Test
	public void defaultCategoryWithVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertTrue(validator.isValid(VariableModifier.builder()
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.variableName("myVar")
				.build(), null));
	}

	@Test
	public void defaultCategoryWithoutVariableName() {
		final VariableModifierValidator validator = new VariableModifierValidator();
		assertFalse(validator.isValid(VariableModifier.builder()
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.build(), null));
	}
}