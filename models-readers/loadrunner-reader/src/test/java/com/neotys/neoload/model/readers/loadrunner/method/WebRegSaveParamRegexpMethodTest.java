package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import org.junit.Test;

import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.VariableExtractor;

public class WebRegSaveParamRegexpMethodTest {

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);

	@Test
	public void toElementTest1() {
		final List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"webRegSaveParamRegexp_TEST\"");
		inputStrList.add("\"RegExp=Name=(.*?) and SurName=(.*?)\"");
		inputStrList.add("\"Group=2\"");		
		final MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("webRegSaveParam").build();

		final VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("webRegSaveParamRegexp_TEST").exitOnError(true).extractType(VariableExtractor.ExtractType.BOTH).regExp("Name=(.*?) and SurName=(.*?)").group("2").build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebregsaveparamregexpMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}
}
