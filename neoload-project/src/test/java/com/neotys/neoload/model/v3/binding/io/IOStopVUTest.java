package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.StopVU;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOStopVUTest extends AbstractIOElementsTest {

	@Test
	public void readStopVU() throws IOException {
		final Project expectedProject = buildProjectContainingStopVU();
		assertNotNull(expectedProject);

		read("test-stop-vu", expectedProject);
	}

	private Project buildProjectContainingStopVU() {
		final StopVU stopVU = StopVU.builder()
				.startNewVU(false)
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(stopVU)
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