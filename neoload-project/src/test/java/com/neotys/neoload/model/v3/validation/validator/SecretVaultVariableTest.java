package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.variable.SecretVaultVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public class SecretVaultVariableTest {

	private static final String MISSING_SECRET_IDENTIFIER = "Data Model is invalid. Violation Number: 1." + System.lineSeparator()
			+ "Violation 1 - Incorrect value for 'secret_identifier': missing value or value is empty."
			+ System.lineSeparator();

	private static final String MISSING_PROVIDER_ID_AND_SECRET_IDENTIFIER = "Data Model is invalid. Violation Number: 2." + System.lineSeparator()
			+ "Violation 1 - Incorrect value for 'provider_id': missing value or value is empty."
			+ System.lineSeparator()
			+ "Violation 2 - Incorrect value for 'secret_identifier': missing value or value is empty."
			+ System.lineSeparator();

	@Test
	public void validateRequiredFields() {
		final Validator validator = new Validator();

		SecretVaultVariable secretVaultVariable = SecretVaultVariable.builder()
				.name("db_password")
				.build();
		Validation validation = validator.validate(secretVaultVariable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(MISSING_PROVIDER_ID_AND_SECRET_IDENTIFIER, validation.getMessage().get());

		secretVaultVariable = SecretVaultVariable.builder()
				.name("db_password")
				.providerId("665f1a2b3c4d5e6f7a8b9c0d")
				.build();
		validation = validator.validate(secretVaultVariable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(MISSING_SECRET_IDENTIFIER, validation.getMessage().get());

		secretVaultVariable = SecretVaultVariable.builder()
				.name("db_password")
				.providerId("665f1a2b3c4d5e6f7a8b9c0d")
				.secretIdentifier("my-app/db")
				.build();
		validation = validator.validate(secretVaultVariable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
