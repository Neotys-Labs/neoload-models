package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class RequestTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	

	private static final String CONSTRAINTS_USER_PATH_REQUEST_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_REQUEST_URL_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].url': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_URL_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_REQUEST_URL_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].url': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_URL_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_REQUEST_URL_PATTERN;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 2.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].url': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'actions.steps[0].url': must match \"^((http[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)\"").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_URL_PATTERN = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_REQUEST_METHOD_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].method': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_METHOD_BLANK = sb.toString();
	}
	
	private static final String CONSTRAINTS_USER_PATH_REQUEST_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].assert_content': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_ASSERTIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_REQUEST_ASSERTIONS_REQUIRED_FIELDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.steps[0].assert_content[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_REQUEST_ASSERTIONS_REQUIRED_FIELDS = sb.toString();
	}

	private static final String CONSTRAINTS_COMPLETE_VERSION;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 6.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'user_paths[0].actions.steps[0].assert_content': must contain only unique names.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'user_paths[0].actions.steps[0].assert_content[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 3 - Incorrect value for 'user_paths[0].actions.steps[0].assert_content[1]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 4 - Incorrect value for 'user_paths[0].actions.steps[0].method': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 5 - Incorrect value for 'user_paths[0].actions.steps[0].name': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 6 - Incorrect value for 'user_paths[0].actions.steps[0].url': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_COMPLETE_VERSION = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.name("")
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_NAME_BLANK, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.name(" 	\r\t\n")
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_NAME_BLANK, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.name("MyHttpRequest")
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateUrl() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.build())
						.build())
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_URL_BLANK_AND_NULL, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_URL_BLANK, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url(" 	\r\t\n")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_URL_PATTERN, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateMethod() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.method("")
								.build())
						.build())
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_METHOD_BLANK, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.method(" 	\r\t\n")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_METHOD_BLANK, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.name("MyHttpRequest")
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.name("MyHttpRequest")
								.url("http://www.neotys.com:80/select?name=neoload")
								.method(Method.POST.name())
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateAssertions() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.addContentAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
								.build())
						.build())
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_ASSERTIONS_NAMES, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.addContentAssertions(ContentAssertion.builder().name("assertion").build())
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_REQUEST_ASSERTIONS_REQUIRED_FIELDS, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.addContentAssertions(ContentAssertion.builder().name("assertion1").contains("contains").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion2").contains("contains").build())
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateCompleteVersion() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.name("")
								.method("")
								.addContentAssertions(ContentAssertion.builder().name("assertion").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion").build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
        		.name("MyProject")
        		.addUserPaths(userPath)
        		.build();

		Validation validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_COMPLETE_VERSION, validation.getMessage().get());	
	}	
}
