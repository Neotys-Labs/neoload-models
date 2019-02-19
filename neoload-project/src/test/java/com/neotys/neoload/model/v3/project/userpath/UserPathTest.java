package com.neotys.neoload.model.v3.project.userpath;


import com.neotys.neoload.model.v3.project.Element;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


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

	@Test
	public void flattened() {
		final UserPath userPath = UserPath.builder().actions(Container.builder()
				.addSteps(Request.builder().build())
				.addSteps(Delay.builder().build())
				.build())
				.build();

		final List<Element> elements = userPath.flattened().collect(Collectors.toList());
		assertEquals(3, elements.size()); // actions, request, delay
	}
}
