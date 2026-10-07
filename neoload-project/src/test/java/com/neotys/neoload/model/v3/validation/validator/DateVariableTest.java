package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.DateVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class DateVariableTest {

	@Test
	public void validateMissingStartDate() {
		final Validator validator = new Validator();

		final DateVariable variable = DateVariable.builder()
				.name("MyDate")
				.build();

		final Validation validation = validator.validate(variable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("start_date"));
	}

	@Test
	public void validateStartDateNotMatchingPattern() {
		final Validator validator = new Validator();

		final DateVariable variable = DateVariable.builder()
				.name("MyDate")
				.startDate("31-13-2020")
				.pattern("dd/MM/yyyy HH:mm:ss")
				.build();

		final Validation validation = validator.validate(variable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("The date is not valid according to the pattern"));
	}

	@Test
	public void validateSpecialPatternsSkipStartDateFormatCheck() {
		final Validator validator = new Validator();

		final DateVariable variable = DateVariable.builder()
				.name("MyDate")
				.pattern("currentTimeMillis")
				.startDate("1690000000000")
				.build();

		assertTrue(validator.validate(variable, NeoLoad.class).isValid());
	}

	@Test
	public void validateMissingStartDateWithNullPattern() {
		final Validator validator = new Validator();

		final DateVariable variable = DateVariable.builder()
				.name("MyDate")
				.pattern(null)
				.build();

		final Validation validation = validator.validate(variable, NeoLoad.class);
		assertFalse(validation.isValid());
	}
}
