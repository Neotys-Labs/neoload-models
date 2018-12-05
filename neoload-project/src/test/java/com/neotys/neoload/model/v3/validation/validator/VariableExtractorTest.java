package com.neotys.neoload.model.v3.validation.validator;


import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

import static org.junit.Assert.*;


public class VariableExtractorTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VARIABLE_EXTRACTOR_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_VARIABLE_EXTRACTOR_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_VARIABLE_EXTRACTOR_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_VARIABLE_EXTRACTOR_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_VARIABLE_EXTRACTOR_MATCH_NUMBER;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'match_number': must be greater than or equal to -1").append(LINE_SEPARATOR);
		CONSTRAINTS_VARIABLE_EXTRACTOR_MATCH_NUMBER = sb.toString();
	}

	private static final String CONSTRAINTS_VARIABLE_EXTRACTOR_JSONPATH_OR_XPATH;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid attributes usage (xpath and jsonpath cannot be used at the same time in variable extractor)").append(LINE_SEPARATOR);
		CONSTRAINTS_VARIABLE_EXTRACTOR_JSONPATH_OR_XPATH = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		VariableExtractor variableExtractor = VariableExtractor.builder()
				.build();
		Validation validation = validator.validate(variableExtractor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VARIABLE_EXTRACTOR_NAME_BLANK_AND_NULL, validation.getMessage().get());

		variableExtractor = VariableExtractor.builder()
				.name("")
				.build();
		validation = validator.validate(variableExtractor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VARIABLE_EXTRACTOR_NAME_BLANK, validation.getMessage().get());

		variableExtractor = VariableExtractor.builder()
				.name("extractor")
				.build();
		validation = validator.validate(variableExtractor, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateMatchNumber() {
		final Validator validator = new Validator();

		VariableExtractor variableExtractor = VariableExtractor.builder()
				.name("name")
				.matchNumber(-20)
				.build();

		Validation validation = validator.validate(variableExtractor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VARIABLE_EXTRACTOR_MATCH_NUMBER, validation.getMessage().get());

		variableExtractor = VariableExtractor.builder()
				.name("name")
				.matchNumber(3)
				.build();
		validation = validator.validate(variableExtractor, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validateXpathOrJsonPath() {
		final Validator validator = new Validator();

		VariableExtractor variableExtractor = VariableExtractor.builder()
				.name("name")
				.jsonPath("jsonPath")
				.xpath("xpath")
				.build();

		Validation validation = validator.validate(variableExtractor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VARIABLE_EXTRACTOR_JSONPATH_OR_XPATH, validation.getMessage().get());

		variableExtractor = VariableExtractor.builder()
				.name("name")
				.jsonPath("jsonPath")
				.build();
		validation = validator.validate(variableExtractor, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		variableExtractor = VariableExtractor.builder()
				.name("name")
				.xpath("xpath")
				.build();
		validation = validator.validate(variableExtractor, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		variableExtractor = VariableExtractor.builder()
				.name("name")
				.xpath("xpath")
				.from(VariableExtractor.From.HEADER)
				.build();
		validation = validator.validate(variableExtractor, NeoLoad.class);
		assertFalse(validation.isValid());
	}
}
