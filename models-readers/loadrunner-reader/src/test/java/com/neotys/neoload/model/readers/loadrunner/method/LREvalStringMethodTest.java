package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.EvalString;
import com.neotys.neoload.model.repository.ImmutableEvalString;

public class LREvalStringMethodTest {
	
	public static final MethodCall LR_EVALSTRING_TEST = ImmutableMethodCall.builder()
			.name("\"lr_eval_string\"")
			.addParameters("\"{think_time}\"")
			.build();
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
	@Test
	public void testGetElement() {		
		
		final EvalString actualEvalString = (EvalString) (new LREvalStringMethod()).getElement(LOAD_RUNNER_VISITOR, LR_EVALSTRING_TEST, METHOD_CALL_CONTEXT);

		final EvalString exprectedEvalString = ImmutableEvalString.builder()
				.name("lr_eval_string")
				.returnValue("${think_time}")	
				.build();		

		assertEquals(exprectedEvalString, actualEvalString);
	}

}
