package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ContainerTest {
	@Test
	public void constants() {
		assertEquals("steps", Container.STEPS);
		assertEquals("assert_content", Container.ASSERT_CONTENT);
	}
}
