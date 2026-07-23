package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Rendezvous;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IORendezvousTest extends AbstractIOElementsTest {

	@Test
	public void read_Rendezvous_OnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingRendezvous_OnlyRequired();
		assertNotNull(expectedProject);

		read("test-rendezvous-only-required", expectedProject);
	}

	@Test
	public void writeRendezvous_OnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingRendezvous_OnlyRequired();
		assertNotNull(expectedProject);

		write("test-rendezvous-only-required", expectedProject);
	}

	@Test
	public void read_Rendezvous_RequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingRendezvous_RequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-rendezvous-required-and-optional", expectedProject);
	}

	@Test
	public void writeRendezvous_RequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingRendezvous_RequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-rendezvous-required-and-optional", expectedProject);
	}


	private Project buildProjectContainingRendezvous_OnlyRequired() {
		final Rendezvous defaultRendezvous = Rendezvous.builder().build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(defaultRendezvous)
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


	private Project buildProjectContainingRendezvous_RequiredAndOptional() {
		final Rendezvous namedRendezvous = Rendezvous.builder()
				.name("MyRendezVous")
				.description("MyRendezVousDescription")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(namedRendezvous)
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
