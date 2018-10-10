package com.neotys.neoload.model.listener;

import com.google.common.io.Files;
import com.neotys.neoload.model.stats.ProjectType;
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

	@Test
	public void shouldPrintSummary(){
		final CmdEventListener cmdEventListener = new CmdEventListener("source", "dest", "nlProject");
		cmdEventListener.startReadingScripts(2);
		cmdEventListener.startScript("scriptPath");
		cmdEventListener.readSupportedAction("myAction");
		cmdEventListener.endScript();
		cmdEventListener.startScript("scriptPath2");
		cmdEventListener.readSupportedAction("myAction");
		cmdEventListener.readUnsupportedFunction("scriptName", "myFunction", 10);
		cmdEventListener.endScript();
		cmdEventListener.endReadingScripts();
		cmdEventListener.printSummary();
	}

	@Test
	public void shouldGeneratoJsonReport(){
		final CmdEventListener cmdEventListener = new CmdEventListener("source", Files.createTempDir().getPath(), "nlProject");
		cmdEventListener.startReadingScripts(2);
		cmdEventListener.startScript("scriptPath");
		cmdEventListener.readSupportedAction("myAction");
		cmdEventListener.endScript();
		cmdEventListener.startScript("scriptPath2");
		cmdEventListener.readSupportedAction("myAction");
		cmdEventListener.readUnsupportedFunction("scriptName", "myFunction", 10);
		cmdEventListener.endScript();
		cmdEventListener.endReadingScripts();
		cmdEventListener.generateJsonReport(ProjectType.LOAD_RUNNER, "statusCode");
	}
}
