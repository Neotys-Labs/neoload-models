package com.neotys.neoload.model.v3.validation.validator;


import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.variable.ListVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class ListVariableTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_COLUMN_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'column_names': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_COLUMN_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_VALUES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'values': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUES = sb.toString();
	}

	@Test
	public void validateColumnNames() {
		final Validator validator = new Validator();

		final ListVariable withoutColumnNames = ListVariable.builder()
				.name("MyList")
				.addValues(newArrayList("Paris", "France"))
				.build();
		final Validation validation = validator.validate(withoutColumnNames, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_COLUMN_NAMES, validation.getMessage().get());
	}

	@Test
	public void validateValues() {
		final Validator validator = new Validator();

		final ListVariable withoutValues = ListVariable.builder()
				.name("MyList")
				.addColumnNames("city", "country")
				.build();
		final Validation validation = validator.validate(withoutValues, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUES, validation.getMessage().get());
	}

	@Test
	public void validateRequired() {
		final Validator validator = new Validator();

		final ListVariable listVariable = ListVariable.builder()
				.name("MyList")
				.addColumnNames("city", "country")
				.addValues(newArrayList("Paris", "France"), newArrayList("London", "UK"))
				.build();
		final Validation validation = validator.validate(listVariable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
