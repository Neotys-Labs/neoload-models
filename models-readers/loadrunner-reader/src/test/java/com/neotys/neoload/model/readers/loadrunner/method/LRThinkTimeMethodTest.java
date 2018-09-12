package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.repository.Delay;
import com.neotys.neoload.model.repository.ImmutableDelay;

public class LRThinkTimeMethodTest {

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
	@Test
	public void testGetElement() {
		assertDelay("1", "1000");
		assertDelay("10", "10000");
		assertDelay("10.00", "10000");	
		assertDelay("0", "0");
		assertDelay("a", "${a}000");		
		assertDelay("${a}", "${a}000");
	}
	
	public void assertDelay(final String lrDelay, final String modelDelay){
		final Delay actualDelay = (Delay) (new LrthinktimeMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"lr_think_time\"")
				.addParameters(lrDelay)
				.build(), METHOD_CALL_CONTEXT).get(0);
		final Delay expectedDelay = ImmutableDelay.builder()
				.name("delay")
				.delay(modelDelay)
				.isThinkTime(true)
				.build();
		assertEquals(expectedDelay, actualDelay);
	}

}
