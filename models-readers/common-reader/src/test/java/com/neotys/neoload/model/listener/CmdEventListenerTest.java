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
		cmdEventListener.readSupportedFunction("scriptName", "myFunction", 10);
		cmdEventListener.readSupportedParameterWithWarn("scriptName", "myFunction", "myParameter", "my warning");
		cmdEventListener.readUnsupportedAction("myActionNotSupported");
		cmdEventListener.endScript();
		cmdEventListener.endReadingScripts();
		final String summary = cmdEventListener.newSummary();
		assertNotNull(summary);
	}
}
