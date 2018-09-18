package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.repository.ImmutableSaveString;
import com.neotys.neoload.model.repository.SaveString;
@SuppressWarnings("squid:S2699")
public class JavascriptMethodTest {
	
	@Test
	public void testLrsavestring() {		
		
		final SaveString actualSaveString = (SaveString) (new LrsavestringMethod()).getElement(LOAD_RUNNER_VISITOR, 
				ImmutableMethodCall.builder()
				.name("\"lr_save_string\"")
				.addParameters("\"1\"")
				.addParameters("\"think_time\"")
				.build(), 
				METHOD_CALL_CONTEXT).get(0);

		final SaveString exprectedSaveString = ImmutableSaveString.builder()
				.name("Set variable think_time")
				.variableName("think_time")
				.variableValue("1")				
				.build();		

		assertEquals(exprectedSaveString, actualSaveString);
	}
	
	@Test
	public void testSapguiselectactivesessionMethod() {		
		
		final SaveString actualSaveString = (SaveString) (new SapguiselectactivesessionMethod()).getElement(LOAD_RUNNER_VISITOR, 
				ImmutableMethodCall.builder()
				.name("\"sapgui_select_active_session\"")
				.addParameters("\"ses[0]\"")
				.build(), 
				METHOD_CALL_CONTEXT).get(0);

		final SaveString exprectedSaveString = ImmutableSaveString.builder()
				.name("sapgui_select_active_session")
				.variableName("SAP_ACTIVE_SESSION")
				.variableValue("ses[0]")				
				.build();		

		assertEquals(exprectedSaveString, actualSaveString);
	}
	
	@Test
	public void testSapguiselectactivewindowMethod() {		
		
		final SaveString actualSaveString = (SaveString) (new SapguiselectactivewindowMethod()).getElement(LOAD_RUNNER_VISITOR, 
				ImmutableMethodCall.builder()
				.name("\"sapgui_select_active_window\"")
				.addParameters("\"wnd[0]\"")
				.build(), 
				METHOD_CALL_CONTEXT).get(0);

		final SaveString exprectedSaveString = ImmutableSaveString.builder()
				.name("sapgui_select_active_window")
				.variableName("SAP_ACTIVE_WINDOW")
				.variableValue("wnd[0]")				
				.build();		

		assertEquals(exprectedSaveString, actualSaveString);
	}

}
