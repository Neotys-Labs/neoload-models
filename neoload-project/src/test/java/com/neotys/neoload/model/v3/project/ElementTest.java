package com.neotys.neoload.model.v3.project;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ElementTest {
	@Test
	public void constants() {
		assertEquals("name", Element.NAME);
		assertEquals("description", Element.DESCRIPTION);		
	}
}
