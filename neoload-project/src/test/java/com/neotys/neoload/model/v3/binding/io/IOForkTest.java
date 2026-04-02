package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Fork;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOForkTest extends AbstractIOElementsTest {

	@Test
	public void readFork() throws IOException {
		final Project expectedProject = buildProjectContainingFork();
		assertNotNull(expectedProject);

		read("test-fork", expectedProject);
	}

	private Project buildProjectContainingFork() {
		final Fork fork = Fork.builder()
				.name("my_fork")
				.copyVariables(false)
				.addSteps(
						Delay.builder().value("1000").build(),
						Delay.builder().value("2000").build()
				)
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(fork)
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