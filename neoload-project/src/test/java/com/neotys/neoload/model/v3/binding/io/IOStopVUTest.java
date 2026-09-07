package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.StopVU;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import java.io.IOException;
import org.junit.Test;

public class IOStopVUTest extends AbstractIOElementsTest {

	@Test
	public void readStopVUOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingStopVU(StopVU.builder().build());
		assertNotNull(expectedProject);

		read("test-stop-vu-only-required", expectedProject);
	}

	@Test
	public void readStopVURequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingStopVU(StopVU.builder().startNewVU(false).build());
		assertNotNull(expectedProject);

		read("test-stop-vu-required-and-optional", expectedProject);
	}

	// Read-only: an explicit start_new_vu: true is written back as the bare "stop_vu" form, so it has no matching write test.
	@Test
	public void readStopVUStartNewVuTrue() throws IOException {
		final Project expectedProject = buildProjectContainingStopVU(StopVU.builder().startNewVU(true).build());
		assertNotNull(expectedProject);

		read("test-readonly-stop-vu-start-new-vu-true", expectedProject);
	}

	@Test
	public void writeStopVUOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingStopVU(StopVU.builder().build());
		assertNotNull(expectedProject);

		write("test-stop-vu-only-required", expectedProject);
	}

	@Test
	public void writeStopVURequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingStopVU(StopVU.builder().startNewVU(false).build());
		assertNotNull(expectedProject);

		write("test-stop-vu-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingStopVU(final StopVU stopVU) {
		final Container container = Container.builder()
				.name("actions")
				.addSteps(stopVU)
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
