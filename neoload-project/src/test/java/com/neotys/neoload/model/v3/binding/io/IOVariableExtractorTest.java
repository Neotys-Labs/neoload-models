package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.neotys.neoload.model.v3.project.userpath.VariableExtractor.Decode.URL;
import static org.junit.Assert.assertNotNull;


public class IOVariableExtractorTest extends AbstractIOElementsTest {

	@Test
	public void readVariableOnlyRequired() throws IOException {
		final Project expectedProject = buildRequestContainingVariableExtractor();
		assertNotNull(expectedProject);

		read("test-variable-extractor", expectedProject);
	}

	private Project buildRequestContainingVariableExtractor() {

		final VariableExtractor variableExtractor = VariableExtractor.builder()
				.name("first-match-on-jsonpath")
				.jsonPath("$.features[0].type")
				.regexp("Fea(.*)")
				.matchNumber(1)
				.decode(Optional.of(URL))
				.extractOnce(true)
				.build();

		final ImmutableRequest request = Request.builder()
				.name("request")
				.url("http://www.neotys.com/select?name:neoload")
				.addExtractors(variableExtractor)
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
