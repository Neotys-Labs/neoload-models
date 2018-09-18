package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.VariableExtractor;
@SuppressWarnings("squid:S2699")
public class WebRegSaveParamExMethodTest {

	public static final MethodCall WEB_REG_SAVE_PARAM = ImmutableMethodCall.builder()
			.name("\"webRegSaveParam\"")
			.addParameters("\"webRegSaveParam_TEST\"")
			.addParameters("\"LB/BIN/ALNUMIC=left boundary\"")
			.addParameters("\"RB=\"")
			.addParameters("\"Ord=2\"")
			.addParameters("\"SaveOffset=11\"")
			.addParameters("\"SaveLen=8\"")
			.addParameters("\"Search=Headers\"")
			.addParameters("\"RelFrameId=tre_frame_id\"")
			.addParameters("\"IgnoreRedirections=Yes\"")			
			.build();
	
	@Test
	public void testGetElement() {		
		final VariableExtractor expectedVariableExtractor = ImmutableVariableExtractor.builder().name("webRegSaveParam_TEST").startExpression(
				"left boundary").endExpression("").nbOccur(2).exitOnError(true).extractType(VariableExtractor.ExtractType.HEADERS).build();
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebregsaveparamMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_REG_SAVE_PARAM, METHOD_CALL_CONTEXT);
		assertEquals(expectedVariableExtractor, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}

}
