package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.UserPath.UserSession;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


public class IOUserPathsTest extends AbstractIOElementsTest {

	private static Project getUserPathsOnlyRequired() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
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

	private static Project getUserPathsRequiredAndOptional() {
		final UserPath userPath1 = UserPath.builder()
				.name("MyUserPath1")
				.description("My User Path 1")
				.userSession(UserSession.RESET_ON)
				.init(Container.builder()
						.name("init")
						.description("My Init Container from My User Path 1")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.actions(Container.builder()
						.name("actions")
						.description("My Actions Container from My User Path 1")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.end(Container.builder()
						.name("end")
						.description("My End Container from My User Path 1")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final UserPath userPath2 = UserPath.builder()
				.name("MyUserPath2")
				.description("My User Path 2")
				.userSession(UserSession.RESET_OFF)
				.init(Container.builder()
						.name("init")
						.description("My Init Container from My User Path 2")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.actions(Container.builder()
						.name("actions")
						.description("My Actions Container from My User Path 2")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.end(Container.builder()
						.name("end")
						.description("My End Container from My User Path 2")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final UserPath userPath3 = UserPath.builder()
				.name("MyUserPath3")
				.description("My User Path 3")
				.userSession(UserSession.RESET_AUTO)
				.init(Container.builder()
						.name("init")
						.description("My Init Container from My User Path 3")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.actions(Container.builder()
						.name("actions")
						.description("My Actions Container from My User Path 3")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.end(Container.builder()
						.name("end")
						.description("My End Container from My User Path 3")
						.addElements(Container.builder()
								.name("MyTransaction")
								.addElements(Delay.builder().value("1000")
										.build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath1)
				.addUserPaths(userPath2)
				.addUserPaths(userPath3)
				.build();

		return project;
	}

	@Test
	public void readUserPathsOnlyRequired() throws IOException {
		final Project expectedProject = getUserPathsOnlyRequired();
		assertNotNull(expectedProject);

		read("test-read-userpaths-only-required", expectedProject);
	}

	@Test
	public void readUserPathsRequiredAndOptional() throws IOException {
		final Project expectedProject = getUserPathsRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-read-write-userpaths-required-and-optional", expectedProject);
	}

//	@Test
//	public void writeUserPathsOnlyRequired() throws IOException, URISyntaxException {
//		final Project expectedProject = getUserPathsOnlyRequired();
//		assertNotNull(expectedProject);
//		
//		write("test-write-userpaths-only-required", expectedProject);
//	}
//
//	@Test
//	public void writeUserPathsRequiredAndOptional() throws IOException, URISyntaxException {
//		final Project expectedProject = getUserPathsRequiredAndOptional();
//		assertNotNull(expectedProject);
//		
//		write("test-read-write-userpaths-required-and-optional", expectedProject);
//	}
}
