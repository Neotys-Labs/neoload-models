package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class UserPathTest {
	@Test
	public void constants() {
		assertEquals("init", UserPath.INIT);
		assertEquals("actions", UserPath.ACTIONS);
		assertEquals("end", UserPath.END);
	}
}
