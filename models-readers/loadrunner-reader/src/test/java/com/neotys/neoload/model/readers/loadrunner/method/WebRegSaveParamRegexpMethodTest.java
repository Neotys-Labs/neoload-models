package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.VariableExtractor;
@SuppressWarnings("squid:S2699")
public class WebRegSaveParamRegexpMethodTest {

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
