package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;


public class IODurationAssertionTest extends AbstractIOElementsTest {

	@Test
	public void readDurationAssertionOnlyRequired() throws IOException {
		final Project expectedProject = getDurationAssertionOnlyRequired();
		assertNotNull(expectedProject);
		read("test-assert-duration-only-required", expectedProject);
	}

	@Test
	public void readDurationAssertionRequiredAndOptional() throws IOException {
		final Project expectedProject = getDurationAssertionRequiredAndOptional();
		assertNotNull(expectedProject);
		read("test-assert-duration-required-and-optional", expectedProject);
	}

	@Test
	public void writeDurationAssertionOnlyRequired() throws IOException {
		final Project expectedProject = getDurationAssertionOnlyRequired();
		assertNotNull(expectedProject);
		write("test-assert-duration-only-required", expectedProject);
	}

	@Test
	public void writeDurationAssertionRequiredAndOptional() throws IOException {
		final Project expectedProject = getDurationAssertionRequiredAndOptional();
		assertNotNull(expectedProject);
		write("test-assert-duration-required-and-optional", expectedProject);
	}

	private Project getDurationAssertionOnlyRequired() {
		final DurationAssertion assertion = DurationAssertion.builder()
				.value(500)
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/download")
				.addAssertions(assertion)
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

	private Project getDurationAssertionRequiredAndOptional() {
		final DurationAssertion assertion = DurationAssertion.builder()
				.name("under_500ms")
				.value(500)
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/download")
				.addAssertions(assertion)
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
}
