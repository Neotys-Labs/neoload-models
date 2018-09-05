package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ClearCache;
import com.neotys.neoload.model.repository.ImmutableClearCache;

public class WebCacheCleanupMethodTest {
	
	public static final MethodCall WEB_CACHE_CLEANUP = ImmutableMethodCall.builder()
			.name("web_cache_cleanup")
			.build();

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
	@Test
	public void testGetElement() {		
		
		final ClearCache actualClearCache = (ClearCache) (new WebcachecleanupMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_CACHE_CLEANUP, METHOD_CALL_CONTEXT);

		final ClearCache expectedClearCache = ImmutableClearCache.builder()
				.name("web_cache_cleanup")
				.build();	

		assertEquals(expectedClearCache, actualClearCache);
	}

}
