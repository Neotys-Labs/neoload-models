package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class ContainerTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_CONTAINER_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_CONTAINER_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_CONTAINER_ELEMENTS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'do': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_ELEMENTS = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		Container container = Container.builder()
				.addElements(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		Validation validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		container = Container.builder()
				.name("")
				.addElements(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_NAME_BLANK, validation.getMessage().get());	

		container = Container.builder()
				.name(" 	\r\t\n")
				.addElements(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_NAME_BLANK, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addElements(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateElements() {
		final Validator validator = new Validator();
		
		Container container = Container.builder()
				.name("container")
				.build();
		Validation validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ELEMENTS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addElements(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
