package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


public class IORequestTest extends AbstractIOElementsTest {

	private static Project getRequestOnlyRequired() {

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com/select?name:neoload")
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	private static Project getRequestRequiredAndOptional() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("/select?name=neoload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(Header.builder()
										.name("Content-Type")
										.value("text/html; charset=utf-8")
										.build())
								.addHeaders(Header.builder()
										.name("Accept-Encoding")
										.value("gzip, compress, br")
										.build())
								.body("My Body\nline 1\nline 2\n")
								.addExtractors(VariableExtractor.builder()
										.name("MyVariable1")
										.jsonPath("MyJsonPath")
										.build())
								.build())
						.addSteps(Request.builder()
								.url("/select?name=neoload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(Header.builder()
										.name("Content-Type")
										.value("text/html; charset=utf-8")
										.build())
								.addHeaders(Header.builder()
										.name("Accept-Encoding")
										.value("gzip, compress, br")
										.build())
								.body("My Body line 1 line 2")
								.addExtractors(VariableExtractor.builder()
										.name("MyVariable1")
										.jsonPath("MyJsonPath")
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
	public void readUserPathsOnlyRequired() throws IOException {
		final Project expectedProject = getRequestOnlyRequired();
		assertNotNull(expectedProject);

		read("test-request-only-required", expectedProject);
	}

	@Test
	public void readUserPathsRequiredAndOptional() throws IOException {
		final Project expectedProject = getRequestRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-request-required-and-optional", expectedProject);
	}
}
