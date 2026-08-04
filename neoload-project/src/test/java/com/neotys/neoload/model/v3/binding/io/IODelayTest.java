package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Delay;


public class IODelayTest extends AbstractIOElementsTest {

	@Test
	public void readDelayOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getDelaysOnlyRequired());
		assertNotNull(expectedProject);

		read("test-delay-without-unit-only-required", expectedProject);
		read("test-delay-with-unit-only-required", expectedProject);
	}

	@Test
	public void writeDelayOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getDelaysOnlyRequired());
		assertNotNull(expectedProject);

		write("test-delay-with-unit-only-required", expectedProject);
	}

	private List<Delay> getDelaysOnlyRequired() {
		return List.of(
				Delay.builder().value("3790200").build(),
				Delay.builder().value("3600000").build(),
				Delay.builder().value("180000").build(),
				Delay.builder().value("10000").build(),
				Delay.builder().value("200").build()
		);
	}
}
