package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

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

	@Test
	public void readThinkTimeRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getThinkTimesRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-think-time-without-unit-required-and-optional", expectedProject);
		read("test-think-time-with-unit-required-and-optional", expectedProject);
	}

	@Test
	public void writeThinkTimeRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getThinkTimesRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-think-time-with-unit-required-and-optional", expectedProject);
	}

	private List<Delay> getThinkTimesOnlyRequired() {
		return List.of(
				Delay.builder().isThinkTime(true).duration(DurationConstant.builder().value(Durations.of("3790200")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max(Durations.of("3790200")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationConstant.builder().value(Durations.of("3600000")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max(Durations.of("3600000")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationConstant.builder().value(Durations.of("180000")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max(Durations.of("180000")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationConstant.builder().value(Durations.of("10000")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max(Durations.of("10000")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationConstant.builder().value(Durations.of("200")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max(Durations.of("200")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationConstant.builder().value(Durations.of("${think_time}")).build()).build(),
				Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max(Durations.of("${max_think_time}")).build()).build()
		);
	}

	private List<Delay> getThinkTimesRequiredAndOptional() {
		return List.of(
				Delay.builder().isThinkTime(true)
						.name("MyConstantThinkTime1")
						.description("MyConstantThinkTime1Description")
						.duration(DurationConstant.builder().value(Durations.of("3790200")).build())
						.build(),
				Delay.builder().isThinkTime(true)
						.name("MyConstantThinkTime2")
						.description("MyConstantThinkTime2Description")
						.duration(DurationConstant.builder().value(Durations.of("${think_time}")).build())
						.build(),
				Delay.builder().isThinkTime(true)
						.name("MyRandomThinkTime1")
						.description("MyRandomThinkTime1Description")
						.duration(DurationRandom.builder().min(Durations.of("3790100")).max(Durations.of("3790200")).build()).build(),
				Delay.builder().isThinkTime(true)
						.name("MyRandomThinkTime2")
						.description("MyRandomThinkTime2Description")
						.duration(DurationRandom.builder().min(Durations.of("${min_think_time}")).max(Durations.of("${max_think_time}")).build()).build()
		);
	}
}
