package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;
import com.neotys.neoload.model.v3.project.userpath.ThinkTimeConstant;
import com.neotys.neoload.model.v3.project.userpath.ThinkTimeRandom;
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

	private List<ThinkTime> getThinkTimesOnlyRequired() {
		return List.of(
				ThinkTimeConstant.builder().value("3790200").build(),
				ThinkTimeRandom.builder().max("3790200").build(),
				ThinkTimeConstant.builder().value("3600000").build(),
				ThinkTimeRandom.builder().max("3600000").build(),
				ThinkTimeConstant.builder().value("180000").build(),
				ThinkTimeRandom.builder().max("180000").build(),
				ThinkTimeConstant.builder().value("10000").build(),
				ThinkTimeRandom.builder().max("10000").build(),
				ThinkTimeConstant.builder().value("200").build(),
				ThinkTimeRandom.builder().max("200").build(),
				ThinkTimeConstant.builder().value("${think_time}").build(),
				ThinkTimeRandom.builder().max("${max_think_time}").build()
		);
	}

	private List<ThinkTime> getThinkTimesRequiredAndOptional() {
		return List.of(
				ThinkTimeConstant.builder()
						.name("MyConstantThinkTime1")
						.description("MyConstantThinkTime1Description")
						.value("3790200")
						.build(),
				ThinkTimeConstant.builder()
						.name("MyConstantThinkTime2")
						.description("MyConstantThinkTime2Description")
						.value("${think_time}")
						.build(),
				ThinkTimeRandom.builder()
						.name("MyRandomThinkTime1")
						.description("MyRandomThinkTime1Description")
						.min("3790100")
						.max("3790200")
						.build(),
				ThinkTimeRandom.builder()
						.name("MyRandomThinkTime2")
						.description("MyRandomThinkTime2Description")
						.min("${min_think_time}")
						.max("${max_think_time}")
						.build()
		);
	}
}
