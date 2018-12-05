package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.ImmutableVariableExtractor;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.project.variable.ImmutableConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.neotys.neoload.model.v3.project.userpath.VariableExtractor.Decode.URL;
import static junit.framework.TestCase.assertNotNull;


public class IOVariableExtractorTest extends AbstractIOElementsTest {

	@Test
	public void readVariableOnlyRequired() throws IOException, URISyntaxException {
//		final Request expectedRequest = buildRequestContainingVariableExtractor();
//		assertNotNull(expectedRequest);
//
//		read("test-variable-extractor", expectedRequest);
	}

	private Request buildRequestContainingVariableExtractor() {
//		final VariableExtractor variableExtractor = ImmutableVariableExtractor.builder()
//				.name("first-match-on-jsonpath")
//				.jsonPath("$.features[0].type")
//				.regexp("Fea(.*)")
//				.matchNumber(1)
//				.decode(Optional.of(URL))
//				.extractOnce(true)
//				.build();
//		return Request.builder()
//				.name("MyProject")
//				.addVariableExtractir(variableExtractor)
//				.build();
	}
}
