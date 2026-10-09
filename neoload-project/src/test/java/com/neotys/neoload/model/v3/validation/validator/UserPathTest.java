package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class UserPathTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_USER_PATH_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_ACTIONS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_ACTIONS = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_THINK_TIME_OVERRIDE_AND_FACTOR;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'think_time': invalid attributes usage (override and factor cannot be used simultaneously).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_THINK_TIME_OVERRIDE_AND_FACTOR = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_THINK_TIME_EMPTY;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'think_time': invalid attributes usage (at least one of override, factor or random is required).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_THINK_TIME_EMPTY = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_THINK_TIME_VALUES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 2.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'think_time.override': must be a number of milliseconds (e.g. 100), a combination of h, m, s and ms (e.g. 1h30m, 5s, 500ms) or a variable.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'think_time.random': must be a positive integer percentage (e.g. 10%) or a variable.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_THINK_TIME_VALUES = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'assertions': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_ASSERTIONS_NAMES = sb.toString();
	}
	
	private static final String CONSTRAINTS_USER_PATH_INIT_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'init.assertions': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_INIT_ASSERTIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_ACTIONS_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.assertions': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_ACTIONS_ASSERTIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_END_ASSERTIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'end.assertions': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_END_ASSERTIONS_NAMES = sb.toString();
	}
	
	private static final String CONSTRAINTS_USER_PATH_ASSERTIONS_REQUIRED_FIELDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_ASSERTIONS_REQUIRED_FIELDS = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_INIT_ASSERTIONS_REQUIRED_FIELDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'init.assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_INIT_ASSERTIONS_REQUIRED_FIELDS = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_ACTIONS_ASSERTIONS_REQUIRED_FIELDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions.assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_ACTIONS_ASSERTIONS_REQUIRED_FIELDS = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_END_ASSERTIONS_REQUIRED_FIELDS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'end.assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_END_ASSERTIONS_REQUIRED_FIELDS = sb.toString();
	}

	private static final String CONSTRAINTS_COMPLETE_VERSION;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 18.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'user_paths[0].actions': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'user_paths[0].name': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 3 - Incorrect value for 'user_paths[1].actions.assertions': must contain only unique names.").append(LINE_SEPARATOR);
		sb.append("Violation 4 - Incorrect value for 'user_paths[1].actions.assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 5 - Incorrect value for 'user_paths[1].actions.assertions[1]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 6 - Incorrect value for 'user_paths[1].actions.steps': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 7 - Incorrect value for 'user_paths[1].assertions': must contain only unique names.").append(LINE_SEPARATOR);
		sb.append("Violation 8 - Incorrect value for 'user_paths[1].assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 9 - Incorrect value for 'user_paths[1].assertions[1]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 10 - Incorrect value for 'user_paths[1].end.assertions': must contain only unique names.").append(LINE_SEPARATOR);
		sb.append("Violation 11 - Incorrect value for 'user_paths[1].end.assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 12 - Incorrect value for 'user_paths[1].end.assertions[1]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 13 - Incorrect value for 'user_paths[1].end.steps': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 14 - Incorrect value for 'user_paths[1].init.assertions': must contain only unique names.").append(LINE_SEPARATOR);
		sb.append("Violation 15 - Incorrect value for 'user_paths[1].init.assertions[0]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 16 - Incorrect value for 'user_paths[1].init.assertions[1]': invalid attributes usage (xpath, jsonpath or contains must be specified).").append(LINE_SEPARATOR);
		sb.append("Violation 17 - Incorrect value for 'user_paths[1].init.steps': missing value or value is empty.").append(LINE_SEPARATOR);
		sb.append("Violation 18 - Incorrect value for 'user_paths[1].name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_COMPLETE_VERSION = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_NAME_BLANK, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name(" 	\r\t\n")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_NAME_BLANK, validation.getMessage().get());	

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
	public void validateActions() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_ACTIONS, validation.getMessage().get());

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
	public void validateThinkTime() {
		final Validator validator = new Validator();
		final Container actions = Container.builder()
				.name("actions")
				.addSteps(Request.builder()
						.url("http://www.neotys.com:80/select?name=neoload")
						.build())
				.build();

		UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.thinkTime(UserPathThinkTime.builder().override("5s").factor("150%").build())
				.actions(actions)
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_THINK_TIME_OVERRIDE_AND_FACTOR, validation.getMessage().get());

		userPath = UserPath.builder()
				.name("MyUserPath")
				.thinkTime(UserPathThinkTime.builder().build())
				.actions(actions)
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_THINK_TIME_EMPTY, validation.getMessage().get());

		userPath = UserPath.builder()
				.name("MyUserPath")
				.thinkTime(UserPathThinkTime.builder().override("-5s").random("abc").build())
				.actions(actions)
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_THINK_TIME_VALUES, validation.getMessage().get());

		userPath = UserPath.builder()
				.name("MyUserPath")
				.onError(UserPath.FailurePolicy.GO_TO_NEXT_ITERATION)
				.onAssertionFailure(UserPath.FailurePolicy.STOP_AND_START_NEW_VU)
				.thinkTime(UserPathThinkTime.builder().override("${my_think_time}").random("10%").build())
				.actions(actions)
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		userPath = UserPath.builder()
				.name("MyUserPath")
				.thinkTime(UserPathThinkTime.builder().factor("150").random("${my_random_delay}").build())
				.actions(actions)
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
								.build())
						.build())
				.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
				.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
				.build();
		Validation validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_ASSERTIONS_NAMES, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.init(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
						.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
						.build())
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_INIT_ASSERTIONS_NAMES, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
						.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_ACTIONS_ASSERTIONS_NAMES, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.end(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
						.addAssertions(ContentAssertion.builder().name("assertion").contains("contains").build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_END_ASSERTIONS_NAMES, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.addAssertions(ContentAssertion.builder().name("assertion").build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_ASSERTIONS_REQUIRED_FIELDS, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.init(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.build())
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_INIT_ASSERTIONS_REQUIRED_FIELDS, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.build())				
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_ACTIONS_ASSERTIONS_REQUIRED_FIELDS, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.end(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.build())				
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USER_PATH_END_ASSERTIONS_REQUIRED_FIELDS, validation.getMessage().get());	

		userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
								.build())
						.build())
				.addAssertions(ContentAssertion.builder().name("assertion1").contains("contains").build())
				.addAssertions(ContentAssertion.builder().name("assertion2").contains("contains").build())
				.build();
		validation = validator.validate(userPath, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}

	@Test
	public void validateCompleteVersion() {
		final Validator validator = new Validator();
		
		UserPath userPath1 = UserPath.builder()
				.build();
		UserPath userPath2 = UserPath.builder()
				.name("")
				.init(Container.builder()
						.name("init")
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.build())
				.actions(Container.builder()
						.name("actions")
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.build())
				.end(Container.builder()
						.name("end")
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.addAssertions(ContentAssertion.builder().name("assertion").build())
						.build())
				.addAssertions(ContentAssertion.builder().name("assertion").build())
				.addAssertions(ContentAssertion.builder().name("assertion").build())
				.build();

		final Project project = Project.builder()
        		.name("MyProject")
        		.addUserPaths(userPath1)
        		.addUserPaths(userPath2)
        		.build();
        
		Validation validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_COMPLETE_VERSION, validation.getMessage().get());	
	}	
}
