package com.neotys.neoload.model.v3.project.userpath.assertion;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ContentAssertionTest {
	@Test
	public void constants() {
		assertEquals("name", ContentAssertion.NAME);
		assertEquals("xpath", ContentAssertion.XPATH);
		assertEquals("jsonpath", ContentAssertion.JSON_PATH);
		assertEquals("not", ContentAssertion.NOT);
		assertEquals("contains", ContentAssertion.CONTAINS);
		assertEquals("regexp", ContentAssertion.REGEXP);
	}
}
