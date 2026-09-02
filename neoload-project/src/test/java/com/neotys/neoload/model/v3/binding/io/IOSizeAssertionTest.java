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


public class IOSizeAssertionTest extends AbstractIOElementsTest {

	@Test
	public void readSizeAssertionEquals() throws IOException {
		final Project expectedProject = getSizeAssertionEquals();
		assertNotNull(expectedProject);
		read("test-assert-size-equals", expectedProject);
	}

	@Test
	public void readSizeAssertionRange() throws IOException {
		final Project expectedProject = getSizeAssertionRange();
		assertNotNull(expectedProject);
		read("test-assert-size-range", expectedProject);
	}

	@Test
	public void readSizeAssertionSingleBound() throws IOException {
		final Project expectedProject = getSizeAssertionSingleBound();
		assertNotNull(expectedProject);
		read("test-assert-size-single-bound", expectedProject);
	}

	@Test
	public void writeSizeAssertionEquals() throws IOException {
		final Project expectedProject = getSizeAssertionEquals();
		assertNotNull(expectedProject);
		write("test-assert-size-equals", expectedProject);
	}

	@Test
	public void writeSizeAssertionRange() throws IOException {
		final Project expectedProject = getSizeAssertionRange();
		assertNotNull(expectedProject);
		write("test-assert-size-range", expectedProject);
	}

	@Test
	public void writeSizeAssertionSingleBound() throws IOException {
		final Project expectedProject = getSizeAssertionSingleBound();
		assertNotNull(expectedProject);
		write("test-assert-size-single-bound", expectedProject);
	}

	private Project getSizeAssertionEquals() {
		return getProject(SizeAssertion.builder()
				.equals(1024L)
				.build());
	}

	private Project getSizeAssertionRange() {
		return getProject(SizeAssertion.builder()
				.greaterThan(1024L)
				.lessThan(2048L)
				.build());
	}

	private Project getSizeAssertionSingleBound() {
		return getProject(SizeAssertion.builder()
				.greaterThan(1024L)
				.build());
	}

	private Project getProject(final SizeAssertion sizeAssertion) {
		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/download")
				.sizeAssertion(sizeAssertion)
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
