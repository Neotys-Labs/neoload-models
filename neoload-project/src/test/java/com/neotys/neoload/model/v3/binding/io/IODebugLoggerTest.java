package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.DebugLogger;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IODebugLoggerTest extends AbstractIOElementsTest {

	@Test
	public void readDebugLogger() throws IOException {
		final Project expectedProject = buildProjectContainingDebugLogger();
		assertNotNull(expectedProject);

		read("test-debug-logger", expectedProject);
	}

	private Project buildProjectContainingDebugLogger() {
		final DebugLogger debugLogger = DebugLogger.builder()
				.text("Current user: ${user_id}")
				.file("logs/custom.txt")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(debugLogger)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("user_path_1")
				.actions(container)
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}