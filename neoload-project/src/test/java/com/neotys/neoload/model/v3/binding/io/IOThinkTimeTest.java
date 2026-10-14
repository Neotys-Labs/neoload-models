package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.project.userpath.DelayRandom;
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
				DelayConstant.builder().isThinkTime(true).value("3790200").build(),
				DelayRandom.builder().isThinkTime(true).max("3790200").build(),
				DelayConstant.builder().isThinkTime(true).value("3600000").build(),
				DelayRandom.builder().isThinkTime(true).max("3600000").build(),
				DelayConstant.builder().isThinkTime(true).value("180000").build(),
				DelayRandom.builder().isThinkTime(true).max("180000").build(),
				DelayConstant.builder().isThinkTime(true).value("10000").build(),
				DelayRandom.builder().isThinkTime(true).max("10000").build(),
				DelayConstant.builder().isThinkTime(true).value("200").build(),
				DelayRandom.builder().isThinkTime(true).max("200").build(),
				DelayConstant.builder().isThinkTime(true).value("${think_time}").build(),
				DelayRandom.builder().isThinkTime(true).max("${max_think_time}").build()
		);
	}

	private List<Delay> getThinkTimesRequiredAndOptional() {
		return List.of(
				DelayConstant.builder().isThinkTime(true)
						.name("MyConstantThinkTime1")
						.description("MyConstantThinkTime1Description")
						.value("3790200")
						.build(),
				DelayConstant.builder().isThinkTime(true)
						.name("MyConstantThinkTime2")
						.description("MyConstantThinkTime2Description")
						.value("${think_time}")
						.build(),
				DelayRandom.builder().isThinkTime(true)
						.name("MyRandomThinkTime1")
						.description("MyRandomThinkTime1Description")
						.min("3790100")
						.max("3790200")
						.build(),
				DelayRandom.builder().isThinkTime(true)
						.name("MyRandomThinkTime2")
						.description("MyRandomThinkTime2Description")
						.min("${min_think_time}")
						.max("${max_think_time}")
						.build()
		);
	}
}
