package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ClearCookies;
import com.neotys.neoload.model.repository.ImmutableClearCookies;
@SuppressWarnings("squid:S2699")
public class WebCleanupCookiesMethodTest {
	
	public static final MethodCall WEB_CLEANUP_COOKIES = ImmutableMethodCall.builder()
			.name("web_cleanup_cookies")
			.build();
	
	@Test
	public void testGetElement() {		
		
		final ClearCookies actualClearCookies = (ClearCookies) (new WebcleanupcookiesMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_CLEANUP_COOKIES, METHOD_CALL_CONTEXT).get(0);

		final ClearCookies expectedClearCookies = ImmutableClearCookies.builder()
				.name("web_cleanup_cookies")
				.build();	

		assertEquals(expectedClearCookies, actualClearCookies);
	}

}
