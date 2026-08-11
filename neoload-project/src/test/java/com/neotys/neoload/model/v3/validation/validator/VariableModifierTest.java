package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.*;

import com.neotys.neoload.model.v3.project.userpath.VariableModifier;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class VariableModifierTest {

	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static final String VIOLATION_PREFIX = "Data Model is invalid. Violation Number: ";

	private static final String INCORRECT_VALUE_VARIABLE_MODIFIER =
			" - Incorrect value for '': invalid variable_modifier usage(If category equals \"predefined\" then mode must" +
					" be \"next_value\" or \"init_value\" and value must not be set. If category is \"shared_queue\" " +
					"then mode must be \"add_shared_queue_value\" or \"poll_shared_queue\" and value cannot be empty.)"
					+ LINE_SEPARATOR;

	private static final String INCORRECT_VALUE_VARIABLE_NAME =
			" - Incorrect value for 'variable_name': missing value or value is empty." + LINE_SEPARATOR;

	private static final String SINGLE_INVALID_VARIABLE_NAME =
			VIOLATION_PREFIX + "1." + LINE_SEPARATOR + "Violation 1" + INCORRECT_VALUE_VARIABLE_NAME;

	private static final String SINGLE_INVALID_VARIABLE_MODIFIER_USAGE
			= VIOLATION_PREFIX + "1." + LINE_SEPARATOR + "Violation 1" + INCORRECT_VALUE_VARIABLE_MODIFIER;

	private static final String BOTH_INVALID
			= VIOLATION_PREFIX + "2." +  LINE_SEPARATOR
			+ "Violation 1" + INCORRECT_VALUE_VARIABLE_MODIFIER
			+ "Violation 2" + INCORRECT_VALUE_VARIABLE_NAME;


	// --- variable_name ---

	@Test
	public void categoryDefault_modeDefault_variableNameMissing_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder().build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_NAME, validation.getMessage().orElse(""));
	}

	@Test
	public void categoryDefault_ModeAddSharedQueueValue_variableNameMissing_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(BOTH_INVALID, validation.getMessage().orElse(""));
	}

	// --- predefined category ---

	@Test
	public void categoryPredefined_ModeNextValue_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.PREDEFINED)
						.mode(VariableModifier.Mode.NEXT_VALUE)
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void categoryPredefined_modeInitValue_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.PREDEFINED)
						.mode(VariableModifier.Mode.INIT_VALUE)
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void categoryPredefined_ModeNextValue_valueIsPresent_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.PREDEFINED)
						.mode(VariableModifier.Mode.NEXT_VALUE)
						.value("SomeValue")
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categoryPredefined_modeInitValue_valueIsPresent_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.PREDEFINED)
						.mode(VariableModifier.Mode.INIT_VALUE)
						.value("SomeValue")
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categoryPredefined_addSharedQueueValue_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.PREDEFINED)
						.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
						.value("SomeValue")
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categoryPredefined_pollSharedQueue_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.PREDEFINED)
						.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
						.value("SomeValue")
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	// --- default category (no category set, defaults to predefined) ---

	@Test
	public void validate_defaultCategory_ModeNextValue_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.mode(VariableModifier.Mode.NEXT_VALUE)
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validate_defaultCategory_modeInitValue_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.mode(VariableModifier.Mode.INIT_VALUE)
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validate_defaultCategory_defaultMode_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	// --- shared_queue category ---

	@Test
	public void categorySharedQueue_addValue_withPlainString_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
						.value("HelloWorld")
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void categorySharedQueue_addValue_withVariableReference_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
						.value("${MyVariable}")
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void categorySharedQueue_addValue_valueIsEmpty_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categorySharedQueue_addValue_valueIsBlank_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
						.value("   ")
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categorySharedQueue_pollSharedQueue_withValue_isValid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
						.value("MyVariable")
						.build(),
				NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void categorySharedQueue_pollSharedQueue_valueIsEmpty_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categorySharedQueue_ModeNextValue_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.NEXT_VALUE)
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

	@Test
	public void categorySharedQueue_modeInitValue_isInvalid() {
		final Validation validation = new Validator().validate(
				VariableModifier.builder()
						.variableName("MyVariable")
						.category(VariableModifier.Category.SHARED_QUEUE)
						.mode(VariableModifier.Mode.INIT_VALUE)
						.build(),
				NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(SINGLE_INVALID_VARIABLE_MODIFIER_USAGE, validation.getMessage().orElse(""));
	}

}
