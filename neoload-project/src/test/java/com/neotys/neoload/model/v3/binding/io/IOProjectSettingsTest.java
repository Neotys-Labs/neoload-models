package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import java.io.IOException;
import org.junit.Test;

public class IOProjectSettingsTest extends AbstractIOElementsTest {

	@Test
	public void readGlobalAssertionsProjectSettings() throws IOException {
		final Project expectedProject = getProjectWithGlobalAssertionsProjectSettings();
		assertNotNull(expectedProject);

		read("test-project-settings-global-assertions", expectedProject);
	}

	private Project getProjectWithGlobalAssertionsProjectSettings() {
		return Project.builder()
				.name("MyProject")
				.putProjectSettings("global_assertions.content_types", "text/html,text/xhtml,application/json")
				.putProjectSettings("global_assertions.apply_on_custom_actions", "true")
				.build();
	}

}
