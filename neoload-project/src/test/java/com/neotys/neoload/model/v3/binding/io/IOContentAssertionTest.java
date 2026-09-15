package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import java.io.IOException;
import org.junit.Test;

public class IOContentAssertionTest extends AbstractIOElementsTest {

	// Legacy `assertions:` key (pre-LOAD-39357 fixtures, unchanged): still readable.

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
	public void legacyOnlyRequiredReserializesUnderNewKey() throws IOException {
		final Project expectedProject = getAsertionsOnlyRequired();
		assertNotNull(expectedProject);

		read("test-assert-content-only-required", expectedProject);
		write("test-content-assertions-only-required", expectedProject);
	}

	@Test
	public void legacyRequiredAndOptionalReserializesUnderNewKey() throws IOException {
		final Project expectedProject = getAsertionsRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-assert-content-required-and-optional", expectedProject);
		write("test-content-assertions-required-and-optional", expectedProject);
	}

	// New `content_assertions:` key: read and written.

	@Test
	public void readContentAssertionsOnlyRequired() throws IOException {
		final Project expectedProject = getAsertionsOnlyRequired();
		assertNotNull(expectedProject);

		read("test-content-assertions-only-required", expectedProject);
	}

	@Test
	public void readContentAssertionsRequiredAndOptional() throws IOException {
		final Project expectedProject = getAsertionsRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-content-assertions-required-and-optional", expectedProject);
	}

	@Test
	public void writeContentAssertionsOnlyRequired() throws IOException {
		final Project expectedProject = getAsertionsOnlyRequired();
		assertNotNull(expectedProject);

		write("test-content-assertions-only-required", expectedProject);
	}

	@Test
	public void writeContentAssertionsRequiredAndOptional() throws IOException {
		final Project expectedProject = getAsertionsRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-content-assertions-required-and-optional", expectedProject);
	}


	private Project getAsertionsOnlyRequired() {

		final ImmutableRequest request = Request.builder()
				.name("http_request")
				.url("http://www.neotys.com/select?name:neoload")
				.addContentAssertions(ContentAssertion.builder()
						.contains("DevOps and Automation")
						.build())
				.addContentAssertions(ContentAssertion.builder()
						.xPath("xpath")
						.contains("DevOps")
						.build())
				.addContentAssertions(ContentAssertion.builder()
						.jsonPath("jsonpath")
						.contains("Automation")
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
				.name("http_request")
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
