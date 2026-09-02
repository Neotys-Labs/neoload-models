package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.CustomAction;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
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
	public void readSizeAssertionOnCustomAction() throws IOException {
		final Project expectedProject = getSizeAssertionOnCustomAction();
		assertNotNull(expectedProject);
		read("test-assert-size-custom-action", expectedProject);
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

	@Test
	public void writeSizeAssertionOnCustomAction() throws IOException {
		final Project expectedProject = getSizeAssertionOnCustomAction();
		assertNotNull(expectedProject);
		write("test-assert-size-custom-action", expectedProject);
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

	private Project getSizeAssertionOnCustomAction() {
		final CustomAction customAction = CustomAction.builder()
				.name("sql action")
				.type("SQL")
				.sizeAssertion(SizeAssertion.builder()
						.lessThan(2048L)
						.build())
				.build();

		return getProject(customAction);
	}

	private Project getProject(final SizeAssertion sizeAssertion) {
		return getProject(Request.builder()
				.name("request")
				.url("http://www.neotys.com/download")
				.sizeAssertion(sizeAssertion)
				.build());
	}

	private Project getProject(final Step step) {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(step)
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}
