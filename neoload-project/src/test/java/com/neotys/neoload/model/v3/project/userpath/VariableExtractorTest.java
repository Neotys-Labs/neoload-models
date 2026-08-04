package com.neotys.neoload.model.v3.project.userpath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor.Decode;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor.From;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class VariableExtractorTest {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	// Serialized value of an enum constant (its @JsonProperty wire string), without the JSON quotes.
	private static String wire(final Object enumValue) throws Exception {
		final String json = MAPPER.writeValueAsString(enumValue);
		return json.substring(1, json.length() - 1);
	}

	@Test
	public void constants() {
		assertEquals("from", VariableExtractor.FROM);
		assertEquals("xpath", VariableExtractor.XPATH);
		assertEquals("jsonpath", VariableExtractor.JSON_PATH);
		assertEquals("regexp", VariableExtractor.REGEXP);
		assertEquals("match_number", VariableExtractor.MATCH_NUMBER);
		assertEquals("template", VariableExtractor.TEMPLATE);
		assertEquals("decode", VariableExtractor.DECODE);
		assertEquals("extract_once", VariableExtractor.EXTRACT_ONCE);
		assertEquals("default", VariableExtractor.DEFAULT);
		assertEquals("throw_assertion_error", VariableExtractor.THROW_ASSERTION_ERROR);
	}

	@Test
	public void fromValues() {
		assertArrayEquals(new From[]{From.HEADER, From.BODY, From.BOTH}, From.values());
	}

	@Test
	public void fromWire() throws Exception {
		assertEquals("header", wire(From.HEADER));
		assertEquals("body", wire(From.BODY));
		assertEquals("both", wire(From.BOTH));
	}

	@Test
	public void decodeValues() {
		assertArrayEquals(new Decode[]{Decode.HTML, Decode.URL}, Decode.values());
	}

	@Test
	public void decodeWire() throws Exception {
		assertEquals("html", wire(Decode.HTML));
		assertEquals("url", wire(Decode.URL));
	}
}
