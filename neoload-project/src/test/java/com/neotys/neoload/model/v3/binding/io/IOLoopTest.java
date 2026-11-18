package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import java.io.IOException;
import org.junit.Test;

public class IOLoopTest extends AbstractIOElementsTest {

    private static Loop getLoopOnlyRequired() {
        return Loop.builder()
				.count("5")
				.addSteps(Delay.builder()
						.duration(DurationConstant.builder().value(Durations.of("1000")).build())
						.build())
				.build();
    }

	private static Loop getLoopRequiredAndOptional() {
		return Loop.builder()
				.name("looper")
				.description("a simple loop")
				.count("5")
				.addSteps(Delay.builder()
						.duration(DurationConstant.builder().value(Durations.of("1000")).build())
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

	@Test
	public void variablePortAndLoopCountRoundTrip() throws IOException {
		final Loop loop = Loop.builder()
				.count("${my-var}")
				.addSteps(Delay.builder()
						.duration(DurationConstant.builder().value(Durations.of("1000")).build())
						.build())
				.build();
		final Server server = Server.builder()
				.name("MyServer")
				.host("myserver.intranet.neotys.com")
				.port("${file.col}")
				.build();
		final Project expectedProject = Project.builder()
				.from(buildProject(loop))
				.addServers(server)
				.build();

		final Project actualProject = new IO().read(getFile("test-variable-port-and-loop-count", "yaml")).getProject();
		assertEquals("${file.col}", actualProject.getServers().get(0).getPort());
		assertEquals("${my-var}", ((Loop) actualProject.getUserPaths().get(0).getActions().getSteps().get(0)).getCount());

		assertRead("test-variable-port-and-loop-count", expectedProject);
		assertWrite("test-variable-port-and-loop-count", expectedProject);
	}
}
