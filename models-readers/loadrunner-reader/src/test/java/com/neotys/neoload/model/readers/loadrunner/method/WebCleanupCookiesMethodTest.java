package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ClearCookies;
import com.neotys.neoload.model.repository.ImmutableClearCookies;

public class WebCleanupCookiesMethodTest {
	
	public static final MethodCall WEB_CLEANUP_COOKIES = ImmutableMethodCall.builder()
			.name("web_cleanup_cookies")
			.build();

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
	@Test
	public void testGetElement() {		
		
		final ClearCookies actualClearCookies = (ClearCookies) (new WebcleanupcookiesMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_CLEANUP_COOKIES, METHOD_CALL_CONTEXT).get(0);

		final ClearCookies expectedClearCookies = ImmutableClearCookies.builder()
				.name("web_cleanup_cookies")
				.build();	

		assertEquals(expectedClearCookies, actualClearCookies);
	}

}
