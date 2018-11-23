package com.neotys.neoload.model.user;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class UserPathTest {
	@Test
	public void constants() {
		assertEquals("init", UserPath.INIT);
		assertEquals("actions", UserPath.ACTIONS);
		assertEquals("end", UserPath.END);
	}
}
