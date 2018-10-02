package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.EvalString;
import com.neotys.neoload.model.repository.ImmutableEvalString;

@SuppressWarnings("squid:S2699")
public class LrevalstringMethodTest {
	
	@Test		
	public void testLrevalstrinGetElement() {		
		
		// lr_eval_string("User : {username} - {tcode}")
		EvalString actual = (EvalString) (new LrevalstringMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"lr_eval_string\"")
				.addParameters("User : {username} - {tcode}")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		EvalString expected = ImmutableEvalString.builder()
				.name(MethodUtils.unquote("lr_eval_string"))
				.returnValue("User : ${username} - ${tcode}")
				.build();				
		assertEquals(expected, actual);
	}

}
