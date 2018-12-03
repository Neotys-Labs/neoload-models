package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.TestCase.assertNotNull;


public class IODelayTest extends AbstractIOElementsTest {

	@Test
	public void readServerOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = buildProjectContainingDelay();
		assertNotNull(expectedProject);

		read("test-delay-only-required", expectedProject);
	}

	private Project buildProjectContainingDelay() {
		final Delay delay = Delay.builder().delay("180100").build();
		final Delay thinkTime = Delay.builder().delay("3790100").isThinkTime(true).build();

		final Container container = Container.builder().
				addElements(delay, thinkTime).build();

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
