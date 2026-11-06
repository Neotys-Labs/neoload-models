package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class ContainerTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_CONTAINER_PACING_MIN_GREATER_THAN_MAX;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'pacing': duration 'min' must be less than or equal to 'max'.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_PACING_MIN_GREATER_THAN_MAX = sb.toString();
	}

	private static final String CONSTRAINTS_CONTAINER_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'content_assertions': must contain only unique names. Duplicates: assertion.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_ASSERTIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_CONTAINER_CONTENT_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'content_assertions': must contain only unique names. Duplicates: assertion.").append(LINE_SEPARATOR);
		CONSTRAINTS_CONTAINER_CONTENT_ASSERTIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_CONTAINER_ASSERTION_REQUIRED_FILEDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'content_assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
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
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

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
	public void validateContentAssertionsNames() {
		final Validator validator = new Validator();

		final Container container = Container.builder()
				.name("container")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").xPath("xpath").build())
				.addContentAssertions(ContentAssertion.builder().name("assertion").jsonPath("jsonpath").build())
				.build();
		final Validation validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_CONTENT_ASSERTIONS_NAMES, validation.getMessage().get());
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

	@Test
	public void validatePacing() {
		final Validator validator = new Validator();

		Container container = Container.builder()
				.addSteps(Request.builder().url("http://www.neotys.com:80/select?name=neoload").build())
				.pacing(DurationConstant.builder().value(Durations.of("30000")).build())
				.build();
		Validation validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());

		container = Container.builder()
				.addSteps(Request.builder().url("http://www.neotys.com:80/select?name=neoload").build())
				.pacing(DurationRandom.builder().max(Durations.of("3000")).build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());

		container = Container.builder()
				.addSteps(Request.builder().url("http://www.neotys.com:80/select?name=neoload").build())
				.pacing(DurationRandom.builder().min(Durations.of("3000")).max(Durations.of("3000")).build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());

		container = Container.builder()
				.addSteps(Request.builder().url("http://www.neotys.com:80/select?name=neoload").build())
				.pacing(DurationRandom.builder().min(Durations.of("3000")).max(Durations.of("1000")).build())
				.build();
		validation = validator.validate(container, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_CONTAINER_PACING_MIN_GREATER_THAN_MAX, validation.getMessage().get());
	}
}
