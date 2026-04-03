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
	public void readRendezvous() throws IOException {
		final Project expectedProject = buildProjectContainingRendezvous();
		assertNotNull(expectedProject);

		read("test-rendezvous", expectedProject);
	}

	private Project buildProjectContainingRendezvous() {
		final Rendezvous rendezvous = Rendezvous.builder()
				.name("my_rendezvous")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(rendezvous)
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