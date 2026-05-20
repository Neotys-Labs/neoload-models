package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.HttpPage;
import com.neotys.neoload.model.v3.project.userpath.ThinkTimeMode;
import com.neotys.neoload.model.v3.project.userpath.ThinkTimeRange;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOHttpPageTest extends AbstractIOElementsTest {

	private static Project getHttpPageOnlyRequired() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(HttpPage.builder()
								.name("MyHttpPage")
								.addSteps(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	private static Project getHttpPageRequiredAndOptional() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(HttpPage.builder()
								.name("MyHttpPage")
								.description("My Http Page")
								.thinkTime("2s")
								.thinkTimeRange(ThinkTimeRange.builder()
										.min("1s")
										.max("5s")
										.build())
								.thinkTimeMode(ThinkTimeMode.RANDOM)
								.screenshot(true)
								.dynamicAction(true)
								.forceEncodingForDynamicResources(true)
								.addSteps(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	@Test
	public void readHttpPageOnlyRequired() throws IOException {
		final Project expectedProject = getHttpPageOnlyRequired();
		assertNotNull(expectedProject);

		read("test-http-page-only-required", expectedProject);
	}

	@Test
	public void readHttpPageRequiredAndOptional() throws IOException {
		final Project expectedProject = getHttpPageRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-http-page-required-and-optional", expectedProject);
	}

	@Test
	public void writeHttpPageOnlyRequired() throws IOException {
		final Project expectedProject = getHttpPageOnlyRequired();
		assertNotNull(expectedProject);

		write("test-http-page-only-required", expectedProject);
	}

	@Test
	public void writeHttpPageRequiredAndOptional() throws IOException {
		final Project expectedProject = getHttpPageRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-http-page-required-and-optional", expectedProject);
	}
}