package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class ContainerTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_CONTAINER_ELEMENTS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'steps': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_ELEMENTS = sb.toString();
	}
	
	private static final String CONSTRAINTS_CONTAINER_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'assert_content': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_ASSERTIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'assert_content[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		Container container = Container.builder()
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		Validation validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.isValid());

		container = Container.builder()
				.name("")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());

		container = Container.builder()
				.name(" 	\r\t\n")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
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
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validateAssertionsNames() {
		final Validator validator = new Validator();
		
		Container container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").xPath("xpath").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").jsonPath("jsonpath").build())
				.build();
		Validation validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTIONS_NAMES, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion1").xPath("xpath").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion2").jsonPath("jsonpath").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion3").contains("contains").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validateAssertionRequiredFields() {
		final Validator validator = new Validator();
		
		Container container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").build())
				.build();
		Validation validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").xPath("").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").xPath("xpath").contains("").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").jsonPath("").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").jsonPath("jsonpath").contains("").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").contains("").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS, validation.getMessage().get());	

		container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion1").xPath("xpath").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion2").xPath("xpath").contains("contains").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion3").jsonPath("jsonpath").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion4").jsonPath("jsonpath").contains("contains").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion5").contains("contains").build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
