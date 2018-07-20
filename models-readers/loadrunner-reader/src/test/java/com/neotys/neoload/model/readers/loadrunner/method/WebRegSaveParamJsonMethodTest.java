package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.VariableExtractor;

public class WebRegSaveParamJsonMethodTest {

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);

	public static final MethodCall WEB_REG_SAVE_PARAM_JSON = ImmutableMethodCall.builder()
			.name("\"web_reg_save_param_json\"")
			.addParameters("\"ParamName=CorrelationParameter\"")
			.addParameters("\"QueryString=$.[0].*\"")			
			.addParameters("SEARCH_FILTERS")
			.addParameters("\"Scope=Body\"")
			.addParameters("LAST")
			.build();
	
	@Test
	public void testGetElement() {	
		final VariableExtractor expectedVariableExtractor = ImmutableVariableExtractor.builder()
				.name("CorrelationParameter")
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.JSON)
				.jsonPath("$.[0].*")
				.build();
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamJsonMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_REG_SAVE_PARAM_JSON, METHOD_CALL_CONTEXT);
		assertEquals(expectedVariableExtractor, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}
}
