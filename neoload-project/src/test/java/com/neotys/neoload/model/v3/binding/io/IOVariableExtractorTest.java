package com.neotys.neoload.model.v3.binding.io;


import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.Request;


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
		return null;
	}
}
