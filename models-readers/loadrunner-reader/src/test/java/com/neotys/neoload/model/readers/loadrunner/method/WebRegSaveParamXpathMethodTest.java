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
public class WebRegSaveParamXpathMethodTest {


	public static final MethodCall WEB_REG_SAVE_PARAM_XPATH = ImmutableMethodCall.builder()
			.name("\"web_reg_save_param_xpath\"")
			.addParameters("\"ParamName=CorrelationParameter\"")
			.addParameters("\"QueryString=/HP_EXTENSION[1]/object[1]/object[1]/array[1]\"")
			.addParameters("\"DFEs=JsonXml\"")			
			.addParameters("\"ReturnXML=Yes\"")
			.addParameters("SEARCH_FILTERS")
			.addParameters("\"Scope=Body\"")
			.addParameters("LAST")
			.build();
	
	@Test
	public void testGetElement() {	
		final VariableExtractor expectedVariableExtractor = ImmutableVariableExtractor.builder()
				.name("CorrelationParameter")
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.XPATH)
				.xPath("/HP_EXTENSION[1]/object[1]/object[1]/array[1]")
				.build();
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		(new WebregsaveparamxpathMethod()).getElement(LOAD_RUNNER_VISITOR, WEB_REG_SAVE_PARAM_XPATH, METHOD_CALL_CONTEXT);
		assertEquals(expectedVariableExtractor, LOAD_RUNNER_VISITOR.getCurrentExtractors().get(0));
	}
}
