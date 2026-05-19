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


/**
 * Backward-compatibility test: verifies that YAML/JSON written in the legacy flat
 * assertion format (no {@code content:} wrapper, fields at the item level) is still
 * accepted by the deserializer. Read-only — the canonical write format is the wrapped
 * one (see {@link IOContentAssertionTest}).
 */
public class IOAssertionLegacyFormatTest extends AbstractIOElementsTest {

	@Test
	public void readLegacyFlatContentAssertions() throws IOException {
		final ContentAssertion a1 = ContentAssertion.builder()
				.contains("DevOps and Automation")
				.build();
		final ContentAssertion a2 = ContentAssertion.builder()
				.name("assertion_named")
				.xPath("xpath")
				.not(true)
				.contains("Analyze")
				.regexp(true)
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/select?name:neoload")
				.addAssertions(a1)
				.addAssertions(a2)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(request)
						.build())
				.build();

		final Project expectedProject = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
		assertNotNull(expectedProject);

		read("test-assert-content-legacy-flat", expectedProject);
	}
}
