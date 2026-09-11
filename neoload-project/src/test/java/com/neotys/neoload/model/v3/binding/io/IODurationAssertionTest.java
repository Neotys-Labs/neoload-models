package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.CustomAction;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class IODurationAssertionTest extends AbstractIOElementsTest {

	@Test
	public void readDurationAssertionOnRequest() throws IOException {
		final Project expectedProject = getProjectWithRequestDurationAssertion();
		assertNotNull(expectedProject);

		read("test-assert-duration-request", expectedProject);
	}

	@Test
	public void writeDurationAssertionOnRequest() throws IOException {
		final Project expectedProject = getProjectWithRequestDurationAssertion();
		assertNotNull(expectedProject);

		write("test-assert-duration-request", expectedProject);
	}

	@Test
	public void readDurationAssertionOnCustomAction() throws IOException {
		final Project expectedProject = getProjectWithCustomActionDurationAssertion();
		assertNotNull(expectedProject);

		read("test-assert-duration-custom-action", expectedProject);
	}

	@Test
	public void writeDurationAssertionOnCustomAction() throws IOException {
		final Project expectedProject = getProjectWithCustomActionDurationAssertion();
		assertNotNull(expectedProject);

		write("test-assert-duration-custom-action", expectedProject);
	}

	@Test
	public void readDurationAssertionWithUnknownPropertyFails() {
		final File file = getFile("test-assert-duration-unknown-property", "yaml");
		try {
			new IO().read(file);
			fail("Expected UnrecognizedPropertyException for unknown property 'name' on duration_assertion");
		} catch (final UnrecognizedPropertyException e) {
			// expected: duration_assertion has no property named "name"
		} catch (final IOException e) {
			fail("Expected UnrecognizedPropertyException, got " + e);
		}
	}

	private Project getProjectWithRequestDurationAssertion() {
		final ImmutableRequest request = Request.builder()
				.name("http_request")
				.url("http://www.neotys.com/select?name:neoload")
				.durationAssertion(DurationAssertion.builder()
						.lessThan(2048L)
						.build())
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(request)
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private Project getProjectWithCustomActionDurationAssertion() {
		final CustomAction customAction = CustomAction.builder()
				.name("sql action")
				.type("SQL")
				.durationAssertion(DurationAssertion.builder()
						.lessThan(500L)
						.build())
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(customAction)
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

}
