package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.variable.SqlVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class SqlVariableTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_URL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'url': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_URL = sb.toString();
	}

	private static final String CONSTRAINTS_QUERY;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'query': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_QUERY = sb.toString();
	}

	@Test
	public void validateUrl() {
		final Validator validator = new Validator();

		final SqlVariable withoutUrl = SqlVariable.builder()
				.name("MySql")
				.query("SELECT username, email FROM users")
				.build();
		final Validation validation = validator.validate(withoutUrl, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_URL, validation.getMessage().get());
	}

	@Test
	public void validateQuery() {
		final Validator validator = new Validator();

		final SqlVariable withoutQuery = SqlVariable.builder()
				.name("MySql")
				.url("jdbc:mysql://localhost:3306/mydb")
				.build();
		final Validation validation = validator.validate(withoutQuery, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_QUERY, validation.getMessage().get());
	}

	@Test
	public void validateDefaults() {
		final Validator validator = new Validator();

		final SqlVariable sqlVariable = SqlVariable.builder()
				.name("MySql")
				.url("jdbc:mysql://localhost:3306/mydb")
				.query("SELECT username, email FROM users")
				.build();
		final Validation validation = validator.validate(sqlVariable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
		assertFalse(sqlVariable.getDriver().isPresent());
		assertFalse(sqlVariable.getLogin().isPresent());
		assertFalse(sqlVariable.getPassword().isPresent());
		assertTrue(sqlVariable.getColumnNames().isEmpty());
		assertEquals(Variable.ChangePolicy.EACH_ITERATION, sqlVariable.getChangePolicy());
		assertEquals(Variable.Scope.GLOBAL, sqlVariable.getScope());
		assertEquals(Variable.Order.ANY, sqlVariable.getOrder());
		assertEquals(Variable.OutOfValue.CYCLE, sqlVariable.getOutOfValue());
	}
}
