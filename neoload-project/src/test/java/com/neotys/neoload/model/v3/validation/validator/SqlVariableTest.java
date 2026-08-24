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

	private static final String CONSTRAINTS_UNKNOWN_DRIVER;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': The driver is not recognized from the entered URL.").append(LINE_SEPARATOR);
		CONSTRAINTS_UNKNOWN_DRIVER = sb.toString();
	}

	private static final String CONSTRAINTS_DRIVER_MISMATCH;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': The entered driver does not match the entered URL.").append(LINE_SEPARATOR);
		CONSTRAINTS_DRIVER_MISMATCH = sb.toString();
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

	@Test
	public void validateDriverIsRequiredWhenTheUrlIsNotRecognized() {
		final Validator validator = new Validator();

		final SqlVariable withoutDriver = SqlVariable.builder()
				.name("MySql")
				.url("jdbc:acme:localhost/mydb")
				.query("SELECT username, email FROM users")
				.build();
		final Validation validation = validator.validate(withoutDriver, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_UNKNOWN_DRIVER, validation.getMessage().get());
	}

	@Test
	public void validateDriverOfAnotherDatabase() {
		final Validator validator = new Validator();

		final SqlVariable postgresDriverOnMySqlUrl = SqlVariable.builder()
				.name("MySql")
				.driver("org.postgresql.Driver")
				.url("jdbc:mysql://localhost:3306/mydb")
				.query("SELECT username, email FROM users")
				.build();
		final Validation validation = validator.validate(postgresDriverOnMySqlUrl, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DRIVER_MISMATCH, validation.getMessage().get());
	}

	@Test
	public void validateEveryKnownDatabaseUrlWithoutDriver() {
		final Validator validator = new Validator();

		final String[] urls = {
				"jdbc:mysql://localhost:3306/mydb",
				"jdbc:postgresql://localhost:5432/mydb",
				"jdbc:db2://localhost:50000/mydb",
				"jdbc:oracle:thin:@localhost:1521:mydb",
				"jdbc:sqlserver://localhost:1433;databaseName=mydb"
		};
		for (final String url : urls) {
			final SqlVariable sqlVariable = SqlVariable.builder()
					.name("MySql")
					.url(url)
					.query("SELECT username, email FROM users")
					.build();
			assertTrue(url, validator.validate(sqlVariable, NeoLoad.class).isValid());
		}
	}

	@Test
	public void validateDriverNeoLoadDoesNotKnow() {
		final Validator validator = new Validator();

		final SqlVariable legacyMySqlDriver = SqlVariable.builder()
				.name("MySql")
				.driver("com.mysql.jdbc.Driver")
				.url("jdbc:mysql://localhost:3306/mydb")
				.query("SELECT username, email FROM users")
				.build();
		assertTrue(validator.validate(legacyMySqlDriver, NeoLoad.class).isValid());

		final SqlVariable customDriver = SqlVariable.builder()
				.name("MySql")
				.driver("com.acme.jdbc.Driver")
				.url("jdbc:acme:localhost/mydb")
				.query("SELECT username, email FROM users")
				.build();
		assertTrue(validator.validate(customDriver, NeoLoad.class).isValid());
	}
}
