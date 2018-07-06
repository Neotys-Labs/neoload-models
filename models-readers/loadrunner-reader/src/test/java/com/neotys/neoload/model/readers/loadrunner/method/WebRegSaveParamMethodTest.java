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


public class WebRegSaveParamMethodTest {

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);

	@Test
	public void toElementTest1() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"webRegSaveParam_TEST\"");
		inputStrList.add("\"LB/BIN/ALNUMIC=left boundary\"");
		inputStrList.add("\"RB=\"");
		inputStrList.add("\"Ord=2\"");
		inputStrList.add("\"SaveOffset=11\"");
		inputStrList.add("\"SaveLen=8\"");
		inputStrList.add("\"Search=Headers\"");
		inputStrList.add("\"RelFrameId=tre_frame_id\"");
		inputStrList.add("\"IgnoreRedirections=Yes\"");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("webRegSaveParam").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("webRegSaveParam_TEST").startExpression(
				"left boundary").endExpression("").nbOccur(2).exitOnError(true).extractType(VariableExtractor.ExtractType.HEADERS).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));

	}

	@Test
	public void toElementTest2() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"webRegSaveParam_TEST2\"");
		inputStrList.add("\"LB=left boundary\"");
		inputStrList.add("\"RB=right\"");
		inputStrList.add("\"Ord=2\"");
		inputStrList.add("\"SaveOffset=11\"");
		inputStrList.add("\"SaveLen=8\"");
		inputStrList.add("\"NotFound=WARNING\"");
		inputStrList.add("\"Search=All\"");
		inputStrList.add("\"RelFrameId=tre_frame_id\"");
		inputStrList.add("\"IgnoreRedirections=Yes\"");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("webRegSaveParam_TEST2").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("webRegSaveParam_TEST2").startExpression(
				"left boundary").endExpression("right").nbOccur(2).exitOnError(false).extractType(VariableExtractor.ExtractType.BOTH).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));

	}

	@Test
	public void toElementTest3() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"WCSParam_Text1\"");
		inputStrList.add("\"LB=textecourant\"");
		inputStrList.add("\"RB=<\"");
		inputStrList.add("\"Ord=3\"");
		inputStrList.add("\"RelFrameId=1\"");
		inputStrList.add("\"Search=body\"");
		inputStrList.add("LAST");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("WCSParam_Text1").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("WCSParam_Text1").startExpression(
				"textecourant").endExpression("<").nbOccur(3).exitOnError(true).extractType(VariableExtractor.ExtractType.BODY).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}

	@Test
	public void testSearchAll() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"testSearchAll\"");
		inputStrList.add("\"LB=a\"");
		inputStrList.add("\"RB=b\"");
		inputStrList.add("\"Search=All\"");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("testSearchAll").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("testSearchAll").startExpression("a").endExpression(
				"b").exitOnError(true).extractType(VariableExtractor.ExtractType.BOTH).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}

	@Test
	public void testSearchDefault() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"testSearchAll\"");
		inputStrList.add("\"LB=a\"");
		inputStrList.add("\"RB=b\"");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("testSearchAll").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("testSearchAll").startExpression("a").endExpression(
				"b").exitOnError(true).extractType(VariableExtractor.ExtractType.BOTH).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}

	@Test
	public void testSearchHeaders() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"testSearchAll\"");
		inputStrList.add("\"LB=a\"");
		inputStrList.add("\"RB=b\"");
		inputStrList.add("\"Search=Headers\"");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("testSearchAll").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("testSearchAll").startExpression("a").endExpression(
				"b").exitOnError(true).extractType(VariableExtractor.ExtractType.HEADERS).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}

	@Test
	public void testSearchBody() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"testSearchAll\"");
		inputStrList.add("\"LB=a\"");
		inputStrList.add("\"RB=b\"");
		inputStrList.add("\"Search=body\"");
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("testSearchAll").build();

		VariableExtractor expectedGenerator = ImmutableVariableExtractor.builder().name("testSearchAll").startExpression("a").endExpression(
				"b").exitOnError(true).extractType(VariableExtractor.ExtractType.BODY).build();

		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebRegSaveParamMethod()).getElement(LOAD_RUNNER_VISITOR, input, METHOD_CALL_CONTEXT);
		assertEquals(expectedGenerator, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}
}
