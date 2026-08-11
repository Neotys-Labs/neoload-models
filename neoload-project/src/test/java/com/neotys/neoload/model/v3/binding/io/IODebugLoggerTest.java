package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.DebugLogger;
import java.io.IOException;
import org.junit.Test;

public class IODebugLoggerTest extends AbstractIOElementsTest {
	@Test
	public void readDebugLoggerOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getDebugLoggerOnlyRequired());
		assertNotNull(expectedProject);

		read("test-debug-logger-only-required", expectedProject);
	}

	@Test
	public void readDebugLoggerRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getDebugLoggerRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-debug-logger-required-and-optional", expectedProject);
	}

	@Test
	public void writeDebugLoggerOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getDebugLoggerOnlyRequired());
		assertNotNull(expectedProject);

		write("test-debug-logger-only-required", expectedProject);
	}

	@Test
	public void writeDebugLoggerRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getDebugLoggerRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-debug-logger-required-and-optional", expectedProject);
	}

	private DebugLogger getDebugLoggerOnlyRequired() {
		return DebugLogger.builder()
				.text("Current user: ${user_id}")
				.build();
	}

	private DebugLogger getDebugLoggerRequiredAndOptional() {
		return DebugLogger.builder()
				.text("Current user: ${user_id}")
				.file("logs/custom.txt")
				.build();
	}
}
