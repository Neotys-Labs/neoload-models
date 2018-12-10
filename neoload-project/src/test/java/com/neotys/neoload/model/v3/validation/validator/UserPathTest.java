package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class UserPathTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_USER_PATH_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_USER_PATH_ACTIONS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'actions': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_USER_PATH_ACTIONS = sb.toString();
	}

	private static final String CONSTRAINTS_COMPLETE_VERSION;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 4.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'user_paths[0].actions': missing value.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'user_paths[0].name': missing value.").append(LINE_SEPARATOR);
		sb.append("Violation 3 - Incorrect value for 'user_paths[1].actions.do': missing value.").append(LINE_SEPARATOR);
		sb.append("Violation 4 - Incorrect value for 'user_paths[1].name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_COMPLETE_VERSION = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		UserPath userPath = UserPath.builder()
				.actions(Container.builder()
						.name("actions")
						.addElements(Request.builder()
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
						.addElements(Request.builder()
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
						.addElements(Request.builder()
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
						.addElements(Request.builder()
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
						.addElements(Request.builder()
								.url("http://www.neotys.com:80/select?name=neoload")
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
		
		UserPath userPath1 = UserPath.builder()
				.build();
		UserPath userPath2 = UserPath.builder()
				.name("")
				.actions(Container.builder()
						.name("actions")
						.build())
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
