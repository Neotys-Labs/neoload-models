package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class ContentAssertionTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_ASSERTION_REQUIRED_FIELDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_ASSERTION_REQUIRED_FIELDS = sb.toString();
	}

	private static final String CONSTRAINTS_ASSERTION_JSONPATH_OR_XPATH;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid attributes usage (xpath and jsonpath cannot be used simultaneously in response content validation).").append(LINE_SEPARATOR);
		CONSTRAINTS_ASSERTION_JSONPATH_OR_XPATH = sb.toString();
	}

	@Test
	public void validateRequiredFields() {
		final Validator validator = new Validator();
		
		ContentAssertion assertion = ContentAssertion.builder()
				.build();
		Validation validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_REQUIRED_FIELDS, validation.getMessage().get());

		assertion = ContentAssertion.builder()
				.xPath("")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_REQUIRED_FIELDS, validation.getMessage().get());

		assertion = ContentAssertion.builder()
				.jsonPath("")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_REQUIRED_FIELDS, validation.getMessage().get());

		assertion = ContentAssertion.builder()
				.contains("")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_REQUIRED_FIELDS, validation.getMessage().get());

		assertion = ContentAssertion.builder()
				.xPath("xpath")
				.contains("")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_REQUIRED_FIELDS, validation.getMessage().get());
		
		assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.contains("")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_REQUIRED_FIELDS, validation.getMessage().get());
		
		assertion = ContentAssertion.builder()
				.xPath("xpath")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		assertion = ContentAssertion.builder()
				.xPath("xpath")
				.contains("contains")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.contains("contains")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		assertion = ContentAssertion.builder()
				.contains("contains")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateXpathOrJsonPath() {
		final Validator validator = new Validator();

		ContentAssertion assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.xPath("xpath")
				.build();
		Validation validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_JSONPATH_OR_XPATH, validation.getMessage().get());
		
		assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.xPath("xpath")
				.contains("contains")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_ASSERTION_JSONPATH_OR_XPATH, validation.getMessage().get());


		assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		assertion = ContentAssertion.builder()
				.jsonPath("jsonpath")
				.contains("contains")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		assertion = ContentAssertion.builder()
				.xPath("xpath")
				.contains("contains")
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
