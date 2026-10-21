package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.project.userpath.DelayRandom;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;


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

	@Test
	public void readDelayRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getDelaysRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-delay-without-unit-required-and-optional", expectedProject);
		read("test-delay-with-unit-required-and-optional", expectedProject);
	}

	@Test
	public void writeDelayRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getDelaysRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-delay-with-unit-required-and-optional", expectedProject);
	}

	private List<Delay> getDelaysOnlyRequired() {
		return List.of(
				DelayConstant.builder().value("3790200").build(),
				DelayRandom.builder().max("3790200").build(),
				DelayConstant.builder().value("3600000").build(),
				DelayRandom.builder().max("3600000").build(),
				DelayConstant.builder().value("180000").build(),
				DelayRandom.builder().max("180000").build(),
				DelayConstant.builder().value("10000").build(),
				DelayRandom.builder().max("10000").build(),
				DelayConstant.builder().value("200").build(),
				DelayRandom.builder().max("200").build(),
				DelayConstant.builder().value("${delay}").build(),
				DelayRandom.builder().max("${max_delay}").build()
		);
	}

	private List<Delay> getDelaysRequiredAndOptional() {
		return List.of(
				DelayConstant.builder()
						.name("MyConstantDelay1")
						.description("MyConstantDelay1Description")
						.value("3790200")
						.build(),
				DelayConstant.builder()
						.name("MyConstantDelay2")
						.description("MyConstantDelay2Description")
						.value("${delay}")
						.build(),
				DelayRandom.builder()
						.name("MyRandomDelay1")
						.description("MyRandomDelay1Description")
						.min("3790100")
						.max("3790200")
						.build(),
				DelayRandom.builder()
						.name("MyRandomDelay2")
						.description("MyRandomDelay2Description")
						.min("${min_delay}")
						.max("${max_delay}")
						.build()
		);
	}
}
