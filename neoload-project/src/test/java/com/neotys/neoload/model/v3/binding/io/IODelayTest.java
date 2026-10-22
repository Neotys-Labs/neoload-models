package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

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
				Delay.builder().duration(DurationConstant.builder().value(MillisecondsDuration.of(3790200)).build()).build(),
				Delay.builder().duration(DurationRandom.builder().max(MillisecondsDuration.of(3790200)).build()).build(),
				Delay.builder().duration(DurationConstant.builder().value(MillisecondsDuration.of(3600000)).build()).build(),
				Delay.builder().duration(DurationRandom.builder().max(MillisecondsDuration.of(3600000)).build()).build(),
				Delay.builder().duration(DurationConstant.builder().value(MillisecondsDuration.of(180000)).build()).build(),
				Delay.builder().duration(DurationRandom.builder().max(MillisecondsDuration.of(180000)).build()).build(),
				Delay.builder().duration(DurationConstant.builder().value(MillisecondsDuration.of(10000)).build()).build(),
				Delay.builder().duration(DurationRandom.builder().max(MillisecondsDuration.of(10000)).build()).build(),
				Delay.builder().duration(DurationConstant.builder().value(MillisecondsDuration.of(200)).build()).build(),
				Delay.builder().duration(DurationRandom.builder().max(MillisecondsDuration.of(200)).build()).build(),
				Delay.builder().duration(DurationConstant.builder().value(MillisecondsDuration.ofVariable("delay")).build()).build(),
				Delay.builder().duration(DurationRandom.builder().max(MillisecondsDuration.ofVariable("max_delay")).build()).build()
		);
	}

	private List<Delay> getDelaysRequiredAndOptional() {
		return List.of(
				Delay.builder()
						.name("MyConstantDelay1")
						.description("MyConstantDelay1Description")
						.duration(DurationConstant.builder().value(MillisecondsDuration.of(3790200)).build())
						.build(),
				Delay.builder()
						.name("MyConstantDelay2")
						.description("MyConstantDelay2Description")
						.duration(DurationConstant.builder().value(MillisecondsDuration.ofVariable("delay")).build())
						.build(),
				Delay.builder()
						.name("MyRandomDelay1")
						.description("MyRandomDelay1Description")
						.duration(DurationRandom.builder().min(MillisecondsDuration.of(3790100)).max(MillisecondsDuration.of(3790200)).build()).build(),
				Delay.builder()
						.name("MyRandomDelay2")
						.description("MyRandomDelay2Description")
						.duration(DurationRandom.builder().min(MillisecondsDuration.ofVariable("min_delay")).max(MillisecondsDuration.ofVariable("max_delay")).build()).build()
		);
	}
}
