package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.SoapRequestContent;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class SoapRequestTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_USER_PATH_SOAP_REQUEST_CONTENT_MISSING;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].content': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_SOAP_REQUEST_CONTENT_MISSING = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_SOAP_REQUEST_URL_MISSING;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].url': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_SOAP_REQUEST_URL_MISSING = sb.toString();
	}

	@Test
	public void validateContentRequired() {
		final Validator validator = new Validator();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.build())
						.build())
				.build();
		final Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_SOAP_REQUEST_CONTENT_MISSING, validation.getMessage().get());
	}

	@Test
	public void validateUrlRequired() {
		final Validator validator = new Validator();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();
		final Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_SOAP_REQUEST_URL_MISSING, validation.getMessage().get());
	}

	@Test
	public void validateAbsoluteUrl() {
		final Validator validator = new Validator();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();
		final Validation validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validateServerAndRelativeUrl() {
		final Validator validator = new Validator();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("/soap")
								.server("myServer")
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();
		final Validation validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
