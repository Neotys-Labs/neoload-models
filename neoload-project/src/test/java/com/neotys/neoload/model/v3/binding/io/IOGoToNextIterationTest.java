package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.GoToNextIteration;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOGoToNextIterationTest extends AbstractIOElementsTest {

	@Test
	public void readGoToNextIteration() throws IOException {
		final Project expectedProject = buildProjectContainingGoToNextIteration();
		assertNotNull(expectedProject);

		read("test-go-to-next-iteration", expectedProject);
	}

	private Project buildProjectContainingGoToNextIteration() {
		final GoToNextIteration goToNextIteration = GoToNextIteration.builder().build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(goToNextIteration)
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