package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Fork;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class IOForkTest extends AbstractIOElementsTest {

	@Test
	public void read_Fork_OnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingFork_OnlyRequired();
		assertNotNull(expectedProject);

		read("test-fork-only-required", expectedProject);
	}

	@Test
	public void read_Fork_RequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingFork_RequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-fork-required-and-optional", expectedProject);
	}

	@Test
	public void write_Fork_OnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingFork_OnlyRequired();
		Assert.assertNotNull(expectedProject);

		write("test-fork-only-required", expectedProject);
	}

	@Test
	public void write_Fork_RequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingFork_RequiredAndOptional();
		Assert.assertNotNull(expectedProject);

		write("test-fork-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingFork_OnlyRequired() {
		final Fork fork = Fork.builder()
				.addSteps(
						Delay.builder().value("1000").build()
				)
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(fork)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(container)
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private Project buildProjectContainingFork_RequiredAndOptional() {
		final Fork fork = Fork.builder()
				.name("MyFork")
				.description("MyForkDescription")
				.copyVariables(true)
				.addSteps(
						Delay.builder().value("1000").build()
				)
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(fork)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(container)
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}