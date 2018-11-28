package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;



public class UserPathTest {
	@Test
	public void constants() {
		assertEquals("user_session", UserPath.USER_SESSION);
		assertEquals("reset_on", UserPath.RESET_ON);
		assertEquals("reset_off", UserPath.RESET_OFF);
		assertEquals("reset_auto", UserPath.RESET_AUTO);

		assertEquals("init", UserPath.INIT);
		assertEquals("actions", UserPath.ACTIONS);
		assertEquals("end", UserPath.END);
		
		assertEquals(UserPath.UserSession.RESET_AUTO, UserPath.DEFAULT_USER_SESSION);
		
		final UserPath userPath1 = UserPath.builder().build();
		assertEquals(UserPath.DEFAULT_USER_SESSION, userPath1.getUserSession());

		final UserPath userPath2 = UserPath.builder().userSession(UserPath.UserSession.RESET_ON).build();
		assertEquals(UserPath.UserSession.RESET_ON, userPath2.getUserSession());

		final UserPath userPath3 = UserPath.builder().userSession(UserPath.UserSession.RESET_OFF).build();
		assertEquals(UserPath.UserSession.RESET_OFF, userPath3.getUserSession());
		
		final UserPath userPath4 = UserPath.builder().userSession(UserPath.UserSession.RESET_AUTO).build();
		assertEquals(UserPath.UserSession.RESET_AUTO, userPath4.getUserSession());
	}
}
