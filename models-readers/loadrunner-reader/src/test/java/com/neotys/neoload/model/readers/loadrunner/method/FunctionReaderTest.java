package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.function.Atoi;
import com.neotys.neoload.model.function.ImmutableAtoi;
import com.neotys.neoload.model.function.ImmutableStrcmp;
import com.neotys.neoload.model.function.Strcmp;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.repository.EvalString;
import com.neotys.neoload.model.repository.ImmutableEvalString;
@SuppressWarnings("squid:S2699")
public class FunctionReaderTest {

	@Test
	public void testEvalStringReader() {				
		final EvalString actualEvalString = (EvalString) (new LrevalstringMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"lr_eval_string\"")
				.addParameters("\"{think_time}\"")
				.build(), METHOD_CALL_CONTEXT).get(0);
		final EvalString expectedEvalString = ImmutableEvalString.builder()
				.name("lr_eval_string")
				.returnValue("${think_time}")	
				.build();		
		Assert.assertEquals(expectedEvalString, actualEvalString);
	}
	
	@Test
	public void testAtoiReader() {				
		final Atoi actualAtoi = (Atoi) (new AtoiMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"atoi\"")
				.addParameters("\"1\"")
				.build(), METHOD_CALL_CONTEXT).get(0);
		final Atoi expectedAtoi = ImmutableAtoi.builder()
				.name("atoi_1")
				.returnValue("${atoi_1}")	
				.args(ImmutableList.of("1"))
				.build();		
		Assert.assertEquals(expectedAtoi, actualAtoi);
	}
	
	@Test
	public void testStrcmpReader() {				
		final Strcmp actualStrcmp = (Strcmp) (new StrcmpMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"strcmp\"")
				.addParameters("\"A\"")
				.addParameters("\"B\"")
				.build(), METHOD_CALL_CONTEXT).get(0);
		final Strcmp expectedStrcmp = ImmutableStrcmp.builder()
				.name("strcmp_1")
				.returnValue("${strcmp_1}")	
				.args(ImmutableList.of("\"A\"", "\"B\""))
				.build();		
		Assert.assertEquals(expectedStrcmp, actualStrcmp);
	}

}
