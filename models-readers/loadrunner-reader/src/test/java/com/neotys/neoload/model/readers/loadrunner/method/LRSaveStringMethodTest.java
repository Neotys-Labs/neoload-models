package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableSaveString;
import com.neotys.neoload.model.repository.SaveString;

public class LRSaveStringMethodTest {
	
	public static final MethodCall LR_SAVE_STRING_TEST = ImmutableMethodCall.builder()
			.name("\"lr_save_string\"")
			.addParameters("\"1\"")
			.addParameters("\"think_time\"")
			.build();
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
	@Test
	public void testGetElement() {		
		
		final SaveString actualSaveString = (SaveString) (new LrsavestringMethod()).getElement(LOAD_RUNNER_VISITOR, LR_SAVE_STRING_TEST, METHOD_CALL_CONTEXT);

		final SaveString exprectedSaveString = ImmutableSaveString.builder()
				.name("Set variable think_time")
				.variableName("think_time")
				.variableValue("1")				
				.build();		

		assertEquals(exprectedSaveString, actualSaveString);
	}

}
