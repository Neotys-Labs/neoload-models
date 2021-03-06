package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;


public class IOThinkTimeTest extends AbstractIOElementsTest {

	@Test
	public void readServerOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingDelay();
		assertNotNull(expectedProject);

		read("test-think-time-only-required", expectedProject);
	}

	private Project buildProjectContainingDelay() {
		final ThinkTime thinkTime = ThinkTime.builder().value("3790100").build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(thinkTime)
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
