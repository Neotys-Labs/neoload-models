package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
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
		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths()
				.build();


		return project;
	}
}
