package com.neotys.neoload.model.listener;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CmdEventListenerTest {

	@Test
	public void shouldGetSummary(){
		final CmdEventListener cmdEventListener = new CmdEventListener("source", "dest", "nlProject");
		cmdEventListener.startReadingScripts(1);
		cmdEventListener.startScript("scriptPath");
		cmdEventListener.readSupportedAction("myAction");
		cmdEventListener.readUnsupportedAction("myActionNotSupported");
		cmdEventListener.readUnsupportedParameter("scriptName", "type", "myParameter");
		cmdEventListener.readSupportedFunctionWithWarn("scriptName", "myFunction", 12, "my warning");
		cmdEventListener.readSupportedFunction("scriptName", "myFunction", 10);
		cmdEventListener.readSupportedParameter("scriptName", "type", "myParameter");
		cmdEventListener.readSupportedParameterWithWarn("scriptName", "type", "myParameter", "my warning");
		cmdEventListener.readUnsupportedAction("myActionNotSupported");
		cmdEventListener.endScript();
		cmdEventListener.endReadingScripts();
		final String summary = cmdEventListener.newSummary();
		assertNotNull(summary);
	}
}
