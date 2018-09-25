package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ClearCache;
import com.neotys.neoload.model.repository.ImmutableClearCache;
@SuppressWarnings("squid:S2699")
public class WebCacheCleanupMethodTest {
	
	public static final MethodCall WEB_CACHE_CLEANUP = ImmutableMethodCall.builder()
			.name("web_cache_cleanup")
			.build();

	@Test
	public void testGetElement() {		
		
		final ClearCache actualClearCache = (ClearCache) (new WebcachecleanupMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_CACHE_CLEANUP, METHOD_CALL_CONTEXT).get(0);

		final ClearCache expectedClearCache = ImmutableClearCache.builder()
				.name("web_cache_cleanup")
				.build();	

		assertEquals(expectedClearCache, actualClearCache);
	}

}
