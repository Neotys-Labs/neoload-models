package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.method.WebAddCookieMethod;
import com.neotys.neoload.model.repository.AddCookie;
import com.neotys.neoload.model.repository.ImmutableAddCookie;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Server;

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
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
	@Test
	public void testGetElement() {		
		
		final AddCookie actualAddCookie = (AddCookie) (new WebAddCookieMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_ADD_COOKIE_TEST, METHOD_CALL_CONTEXT);

		final AddCookie expectedAddCookie = ImmutableAddCookie.builder()
				.name("Set cookie NID for server host_test.com")
				.cookieName("NID")
				.cookieValue("131=IGM2b3TaZ32Y6AhTUp7xG3hvGMXb35mBjohIErmdaQWVV-zd203Khdap56-0p30lyMlDq5AYP1WlxdPQfEmNckJMzl525vPuUQi1bY5e7phDCFx6SbiTH4RxN6V-IbpH0xLE3d5P1Ro")
				.server(SERVER_TEST)
				.build();
		

		assertEquals(expectedAddCookie, actualAddCookie);
	}

}
