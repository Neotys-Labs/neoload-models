package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.Header;
import com.neotys.neoload.model.repository.ImmutableHeader;

public class WebAddAutoHeaderMethodTest {

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);

	public static final MethodCall WEB_ADD_AUTO_HEADER = ImmutableMethodCall.builder().name("\"web_add_auto_header\"").addParameters(
			"\"headerName\"").addParameters("\"headerValue\"").build();

	@Test
	public void testGetElement() {
		LOAD_RUNNER_VISITOR.getCurrentHeaders().clear();
		LOAD_RUNNER_VISITOR.getGlobalHeaders().clear();
		(new WebaddautoheaderMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_ADD_AUTO_HEADER, METHOD_CALL_CONTEXT);
		assertEquals(0, LOAD_RUNNER_VISITOR.getCurrentHeaders().size());
		assertEquals(1, LOAD_RUNNER_VISITOR.getGlobalHeaders().size());
		final Header actualHeader = LOAD_RUNNER_VISITOR.getGlobalHeaders().get(0);
		final Header expectedHeader = ImmutableHeader.builder()
				.headerName("headerName")
				.headerValue("headerValue")
				.build();
		assertEquals(expectedHeader, actualHeader);
	}
}
