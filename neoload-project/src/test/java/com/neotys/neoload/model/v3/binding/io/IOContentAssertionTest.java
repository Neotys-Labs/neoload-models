package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;


public class IOContentAssertionTest extends AbstractIOElementsTest {

	@Test
	public void readAsertionsOnlyRequired() throws IOException {
		final Project expectedProject = getAsertionsOnlyRequired();
		assertNotNull(expectedProject);

		read("test-assert-content-only-required", expectedProject);
	}

	@Test
	public void readAsertionsRequiredAndOptional() throws IOException {
		final Project expectedProject = getAsertionsRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-assert-content-required-and-optional", expectedProject);
	}

	@Test
	public void writeAsertionsOnlyRequired() throws IOException {
		final Project expectedProject = getAsertionsOnlyRequired();
		assertNotNull(expectedProject);

		write("test-assert-content-only-required", expectedProject);
	}

	@Test
	public void writeAsertionsRequiredAndOptional() throws IOException {
		final Project expectedProject = getAsertionsRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-assert-content-required-and-optional", expectedProject);
	}


	private Project getAsertionsOnlyRequired() {

		final ContentAssertion assertion = ContentAssertion.builder()
				.contains("DevOps and Automation")
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/select?name:neoload")
				.addContentAssertions(assertion)
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
	
	private Project getAsertionsRequiredAndOptional() {
		final ContentAssertion assertion1 = ContentAssertion.builder()
				.name("assertion_1")
				.not(true)
				.contains("Design & Maintenance")
				.regexp(true)
				.build();
		
		final ContentAssertion assertion2 = ContentAssertion.builder()
				.name("assertion_2")
				.xPath("xpath")
				.not(true)
				.contains("Analyze")
				.regexp(true)
				.build();
		
		final ContentAssertion assertion3 = ContentAssertion.builder()
				.name("assertion_3")
				.jsonPath("jsonpath")
				.not(true)
				.contains("DevOps and Automation")
				.regexp(true)
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/select?name:neoload")
				.addContentAssertions(assertion1)
				.addContentAssertions(assertion2)
				.addContentAssertions(assertion3)
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
