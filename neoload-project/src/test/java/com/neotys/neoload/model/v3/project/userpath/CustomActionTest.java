package com.neotys.neoload.model.v3.project.userpath;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CustomActionTest {
	@Test
	public void constants() {
		assertEquals("name", CustomAction.NAME);
		assertEquals("description", CustomAction.DESCRIPTION);

		assertEquals("type", CustomAction.TYPE);

		assertEquals("parameters", CustomAction.PARAMETERS);

		assertEquals("asRequest", CustomAction.AS_REQUEST);
		assertEquals("libraryPath", CustomAction.LIBRARY_PATH);
	}
}
