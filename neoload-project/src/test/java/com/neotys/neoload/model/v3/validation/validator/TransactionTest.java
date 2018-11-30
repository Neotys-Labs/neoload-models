package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Transaction;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class TransactionTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_TRANSACTION_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_TRANSACTION_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_TRANSACTION_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_TRANSACTION_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_TRANSACTION_ELEMENTS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'do': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_TRANSACTION_ELEMENTS = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		Transaction transaction = Transaction.builder()
				.addElements(Request.builder().url("url").build())
				.build();
		Validation validation = validator.validate(transaction, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TRANSACTION_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		transaction = Transaction.builder()
				.name("")
				.addElements(Request.builder().url("url").build())
				.build();
		validation = validator.validate(transaction, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TRANSACTION_NAME_BLANK, validation.getMessage().get());	

		transaction = Transaction.builder()
				.name(" 	\r\t\n")
				.addElements(Request.builder().url("url").build())
				.build();
		validation = validator.validate(transaction, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TRANSACTION_NAME_BLANK, validation.getMessage().get());	

		transaction = Transaction.builder()
				.name("transaction")
				.addElements(Request.builder().url("url").build())
				.build();
		validation = validator.validate(transaction, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateElements() {
		final Validator validator = new Validator();
		
		Transaction transaction = Transaction.builder()
				.name("transaction")
				.build();
		Validation validation = validator.validate(transaction, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TRANSACTION_ELEMENTS, validation.getMessage().get());	

		transaction = Transaction.builder()
				.name("transaction")
				.addElements(Request.builder().url("url").build())
				.build();
		validation = validator.validate(transaction, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
