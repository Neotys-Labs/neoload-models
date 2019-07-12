package com.neotys.neoload.model.listener;

public class TestEventListener implements EventListener {

	public TestEventListener() {
		// Empty. For test purpose only.
	}

	@Override
	public void startReadingScripts(int totalScriptNumber) {
		// Empty. For test purpose only.
	}

	@Override
	public void endReadingScripts() {
		// Empty. For test purpose only.
	}

	@Override
	public void startScript(String scriptPath) {
		// Empty. For test purpose only.
	}

	@Override
	public void endScript() {
		// Empty. For test purpose only.
	}

	@Override
	public void readSupportedAction(String actionName) {
		// Empty. For test purpose only.
	}
	
	@Override
	public void readUnsupportedAction(String actionName) {
		// Empty. For test purpose only.
	}
	
	@Override
	public void readSupportedFunction(String scriptName, String functionName, Integer lineNumber) {
		// Empty. For test purpose only.
	}

	@Override
	public void readSupportedFunctionWithWarn(String scriptName, String functionName, Integer lineNumber, String warning) {
		// Empty. For test purpose only.
	}

	@Override
	public void readUnsupportedFunction(String scriptName, String functionName, Integer lineNumber) {
		// Empty. For test purpose only.
	}

	@Override
	public void readSupportedParameter(String scriptName,final String parameterType, final String parameterName) {
		// Empty. For test purpose only.
	}

	@Override
	public void readSupportedParameterWithWarn(String scriptName, final String parameterType, final String parameterName,
			String warning) {
		// Empty. For test purpose only.
	}

	@Override
	public void readUnsupportedParameter(String scriptName, final String parameterType, final String parameterName) {
		// Empty. For test purpose only.
	}

}
