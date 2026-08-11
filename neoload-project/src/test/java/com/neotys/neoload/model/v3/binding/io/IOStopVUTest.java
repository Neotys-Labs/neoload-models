package com.neotys.neoload.model.v3.binding.io;


import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.StopVU;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOStopVUTest extends AbstractIOElementsTest {

	@Test
	public void readStopVUOnlyRequired() throws IOException {
		read("test-stop-vu-only-required", buildProjectContainingStopVU(StopVU.builder().build()));
	}

	@Test
	public void readStopVURequiredAndOptional() throws IOException {
		read("test-stop-vu-required-and-optional", buildProjectContainingStopVU(StopVU.builder().startNewVU(false).build()));
	}

	// Read-only: an explicit start_new_vu: true is written back as the bare "stop_vu" form, so it has no matching write test.
	@Test
	public void readStopVUStartNewVuTrue() throws IOException {
		read("test-readonly-stop-vu-start-new-vu-true", buildProjectContainingStopVU(StopVU.builder().startNewVU(true).build()));
	}

	@Test
	public void writeStopVUOnlyRequired() throws IOException {
		write("test-stop-vu-only-required", buildProjectContainingStopVU(StopVU.builder().build()));
	}

	@Test
	public void writeStopVURequiredAndOptional() throws IOException {
		write("test-stop-vu-required-and-optional", buildProjectContainingStopVU(StopVU.builder().startNewVU(false).build()));
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
