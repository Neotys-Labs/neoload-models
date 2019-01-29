package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOContainerTest extends AbstractIOElementsTest {

	private static Project getContainerOnlyRequired() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Container.builder()
								.name("MyTransaction")
								.addSteps(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	private static Project getContainerRequiredAndOptional() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Container.builder()
								.name("MyTransaction")
								.description("My Transaction")
								.slaProfile("MySlaProfile")
								.addSteps(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	@Test
	public void readUserPathsOnlyRequired() throws IOException {
		final Project expectedProject = getContainerOnlyRequired();
		assertNotNull(expectedProject);

		read("test-container-only-required", expectedProject);
	}

	@Test
	public void readUserPathsRequiredAndOptional() throws IOException {
		final Project expectedProject = getContainerRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-container-required-and-optional", expectedProject);
	}
}
