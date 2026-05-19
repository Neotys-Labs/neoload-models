package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeOperator;


public class IOSizeAssertionTest extends AbstractIOElementsTest {

	@Test
	public void readSizeAssertionOnlyRequired() throws IOException {
		final Project expectedProject = getSizeAssertionOnlyRequired();
		assertNotNull(expectedProject);
		read("test-assert-size-only-required", expectedProject);
	}

	@Test
	public void readSizeAssertionRequiredAndOptional() throws IOException {
		final Project expectedProject = getSizeAssertionRequiredAndOptional();
		assertNotNull(expectedProject);
		read("test-assert-size-required-and-optional", expectedProject);
	}

	@Test
	public void writeSizeAssertionOnlyRequired() throws IOException {
		final Project expectedProject = getSizeAssertionOnlyRequired();
		assertNotNull(expectedProject);
		write("test-assert-size-only-required", expectedProject);
	}

	@Test
	public void writeSizeAssertionRequiredAndOptional() throws IOException {
		final Project expectedProject = getSizeAssertionRequiredAndOptional();
		assertNotNull(expectedProject);
		write("test-assert-size-required-and-optional", expectedProject);
	}

	private Project getSizeAssertionOnlyRequired() {
		final SizeAssertion assertion = SizeAssertion.builder()
				.operator(SizeOperator.LESS_THAN)
				.value(1048576)
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

	private Project getSizeAssertionRequiredAndOptional() {
		final SizeAssertion a1 = SizeAssertion.builder()
				.name("small_response")
				.operator(SizeOperator.LESS_THAN)
				.value(1024)
				.build();
		final SizeAssertion a2 = SizeAssertion.builder()
				.name("exact_match")
				.operator(SizeOperator.EQUALS)
				.value(200)
				.build();
		final SizeAssertion a3 = SizeAssertion.builder()
				.name("min_size")
				.operator(SizeOperator.GREATER_THAN)
				.value(100)
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/download")
				.addAssertions(a1)
				.addAssertions(a2)
				.addAssertions(a3)
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
