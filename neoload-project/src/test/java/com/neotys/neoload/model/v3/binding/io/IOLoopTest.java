package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Loop;


public class IOLoopTest extends AbstractIOElementsTest {

    private static Loop getLoopOnlyRequired() {
        return Loop.builder()
				.count("5")
				.addSteps(Delay.builder()
						.value("1000")
						.build())
				.build();
    }

	private static Loop getLoopRequiredAndOptional() {
		return Loop.builder()
				.name("looper")
				.description("a simple loop")
				.count("5")
				.addSteps(Delay.builder()
						.value("1000")
						.build())
				.build();
	}

	@Test
	public void readLoopOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getLoopOnlyRequired());
		assertNotNull(expectedProject);

		read("test-loop-only-required", expectedProject);
	}

	@Test
	public void readLoopRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getLoopRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-loop-required-and-optional", expectedProject);
	}

	@Test
    public void writeLoopOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getLoopOnlyRequired());
        assertNotNull(expectedProject);

        write("test-loop-only-required", expectedProject);
    }

	@Test
	public void writeLoopRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getLoopRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-loop-required-and-optional", expectedProject);
	}
}
