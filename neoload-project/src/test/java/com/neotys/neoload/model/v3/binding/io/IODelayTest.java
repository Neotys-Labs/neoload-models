package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import com.neotys.neoload.model.v3.project.userpath.ThinkTime;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IODelayTest extends AbstractIOElementsTest {

	@Test
	public void readServerOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = buildProjectContainingDelay();
		assertNotNull(expectedProject);

		read("test-delay-only-required", expectedProject);
	}

	private Project buildProjectContainingDelay() {
		final Delay delay1 = Delay.builder().value("180200").build();
		final Delay delay2 = Delay.builder().value("3790100").build();
		final ThinkTime thinkTime = ThinkTime.builder().value("1000").build();
		final Delay delay3 = Delay.builder().value("1000").build();

		final Container container = Container.builder()
				.name("actions")
				.addElements(delay1, delay2, delay3, thinkTime)
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
