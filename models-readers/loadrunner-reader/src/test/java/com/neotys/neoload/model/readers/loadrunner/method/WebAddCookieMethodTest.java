package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.AddCookie;
import com.neotys.neoload.model.repository.ImmutableAddCookie;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Server;
@SuppressWarnings("squid:S2699")
public class WebAddCookieMethodTest {
	
	public static final MethodCall WEB_ADD_COOKIE_TEST = ImmutableMethodCall.builder()
			.name("\"web_add_cookie\"")
			.addParameters("\"NID=131=IGM2b3TaZ32Y6AhTUp7xG3hvGMXb35mBjohIErmdaQWVV-zd203Khdap56-0p30lyMlDq5AYP1WlxdPQfEmNckJMzl525vPuUQi1bY5e7phDCFx6SbiTH4RxN6V-IbpH0xLE3d5P1Ro; DOMAIN=host_test.com\"")
			.build();

	public static final Server SERVER_TEST = ImmutableServer.builder()
            .name("host_test.com")
            .host("host_test.com")
            .port("80")
            .scheme("http")
            .build();

	@Test
	public void testGetElement() {		
		
		final AddCookie actualAddCookie = (AddCookie) (new WebaddcookieMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_ADD_COOKIE_TEST, METHOD_CALL_CONTEXT).get(0);

		final AddCookie expectedAddCookie = ImmutableAddCookie.builder()
				.name("Set cookie NID for server host_test.com")
				.cookieName("NID")
				.cookieValue("131=IGM2b3TaZ32Y6AhTUp7xG3hvGMXb35mBjohIErmdaQWVV-zd203Khdap56-0p30lyMlDq5AYP1WlxdPQfEmNckJMzl525vPuUQi1bY5e7phDCFx6SbiTH4RxN6V-IbpH0xLE3d5P1Ro")
				.server(SERVER_TEST)
				.build();
		

		assertEquals(expectedAddCookie, actualAddCookie);
	}

}
