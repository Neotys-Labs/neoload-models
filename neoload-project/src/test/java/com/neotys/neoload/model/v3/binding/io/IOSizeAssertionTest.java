package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.CustomAction;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;


public class IOSizeAssertionTest extends AbstractIOElementsTest {

	@Test
	public void readSizeAssertion() throws IOException {
		final Project expectedProject = getSizeAssertionProject();
		assertNotNull(expectedProject);
		read("test-assert-size-custom-action", expectedProject);
	}

	@Test
	public void writeSizeAssertion() throws IOException {
		final Project expectedProject = getSizeAssertionProject();
		assertNotNull(expectedProject);
		write("test-assert-size-custom-action", expectedProject);
	}

	private Project getSizeAssertionProject() {
		final Request requestEquals = Request.builder()
				.name("request_size_equals")
				.url("http://www.neotys.com/download")
				.sizeAssertion(SizeAssertion.builder().equals(1024L).build())
				.build();

		final Request requestRange = Request.builder()
				.name("request_size_range")
				.url("http://www.neotys.com/download")
				.sizeAssertion(SizeAssertion.builder().greaterThan(1024L).lessThan(2048L).build())
				.build();

		final Request requestSingleBound = Request.builder()
				.name("request_size_single_bound")
				.url("http://www.neotys.com/download")
				.sizeAssertion(SizeAssertion.builder().greaterThan(1024L).build())
				.build();

		final CustomAction customAction = CustomAction.builder()
				.name("sql action")
				.type("SQL")
				.sizeAssertion(SizeAssertion.builder().lessThan(2048L).build())
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(requestEquals, requestRange, requestSingleBound, customAction)
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}
