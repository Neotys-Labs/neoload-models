package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;

public class IOTryCatchTest extends AbstractIOElementsTest {

	private static Project getTryCatchOnlyRequired() {
		final TryCatch tryCatch = TryCatch.builder()
				.name("try_catch")
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("1000").build())
						.build())
				.build();

		final UserPath userPath = UserPath.builder()
				.name("user_path_1")
				.actions(Container.builder()
						.name("actions")
						.addSteps(tryCatch)
						.build())
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private static Project getTryCatchRequiredAndOptional() {
		final TryCatch tryCatch = TryCatch.builder()
				.name("my_try_catch")
				.description("my_try_catch_description")
				.caughtExceptions(Arrays.asList(TryCatch.CaughtException.ERRORS, TryCatch.CaughtException.ASSERTIONS))
				.getTry(Container.builder()
						.addSteps(
								Delay.builder().value("1000").build(),
								Delay.builder().value("2000").build()
						)
						.build())
				.getCatch(Container.builder()
						.addSteps(
								Delay.builder().value("500").build()
						)
						.build())
				.build();

		final UserPath userPath = UserPath.builder()
				.name("user_path_1")
				.actions(Container.builder()
						.name("actions")
						.addSteps(tryCatch)
						.build())
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readTryCatchOnlyRequired() throws IOException {
		final Project expectedProject = getTryCatchOnlyRequired();
		assertNotNull(expectedProject);

		read("test-try-catch-only-required", expectedProject);
	}

	@Test
	public void readTryCatchRequiredAndOptional() throws IOException {
		final Project expectedProject = getTryCatchRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-try-catch-required-and-optional", expectedProject);
	}

	@Test
	public void writeTryCatchOnlyRequired() throws IOException {
		final Project expectedProject = getTryCatchOnlyRequired();
		assertNotNull(expectedProject);

		write("test-try-catch-only-required", expectedProject);
	}

	@Test
	public void writeTryCatchRequiredAndOptional() throws IOException {
		final Project expectedProject = getTryCatchRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-try-catch-required-and-optional", expectedProject);
	}

	@Test
	public void readTryCatchInvalidCaughtExceptionsRejected() {
		final IO io = new IO();
		final File file = getFile("test-try-catch-invalid-caught-exceptions", "yaml");
		try {
			io.read(file);
			fail("Expected reading an unknown caught_exceptions value to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage().contains("timeouts"));
		}
	}
}