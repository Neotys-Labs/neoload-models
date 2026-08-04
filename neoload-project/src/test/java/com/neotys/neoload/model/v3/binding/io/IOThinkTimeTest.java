package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;


public class IOThinkTimeTest extends AbstractIOElementsTest {

	@Test
	public void readThinkTimeOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getThinkTimesOnlyRequired());
		assertNotNull(expectedProject);

		read("test-think-time-without-unit-only-required", expectedProject);
		read("test-think-time-with-unit-only-required", expectedProject);
	}

	@Test
	public void writeThinkTimeOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getThinkTimesOnlyRequired());
		assertNotNull(expectedProject);

		write("test-think-time-with-unit-only-required", expectedProject);
	}

	private List<ThinkTime> getThinkTimesOnlyRequired() {
		return List.of(
				ThinkTime.builder().value("3790200").build(),
				ThinkTime.builder().value("3600000").build(),
				ThinkTime.builder().value("180000").build(),
				ThinkTime.builder().value("10000").build(),
				ThinkTime.builder().value("200").build()
		);
	}
}
