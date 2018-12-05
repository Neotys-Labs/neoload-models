package com.neotys.neoload.model.v3.project.userpath;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class VariableExtractorTest {
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
}
