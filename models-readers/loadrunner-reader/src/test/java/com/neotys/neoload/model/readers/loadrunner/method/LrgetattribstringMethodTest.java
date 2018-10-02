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
public class LrgetattribstringMethodTest {
	
	@Test		
	public void testgetattribstringGetElement() {		
		
		// lr_get_attrib_string("interActionTime")
		EvalString actual = (EvalString) (new LrgetattribstringMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"lr_get_attrib_string\"")
				.addParameters("interActionTime")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		EvalString expected = ImmutableEvalString.builder()
				.name(MethodUtils.unquote("lr_get_attrib_string"))
				.returnValue("interActionTime")
				.build();				
		assertEquals(expected, actual);
	}

}
