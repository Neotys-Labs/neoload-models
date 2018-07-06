package com.neotys.neoload.model.listener;

public interface EventListener {

	public void startReadingScripts(final int totalScriptNumber);
	public void endReadingScripts();	
	
	public void startScript(final String scriptPath);
	public void endScript();
	
	public void readSupportedAction(final String actionName);
	public void readUnsupportedAction(final String actionName);
	
	public void readSupportedFunction(final String scriptName, final String functionName, final Integer lineNumber);
	public void readSupportedFunctionWithWarn(final String scriptName, final String functionName, final Integer lineNumber, final String warning);
	public void readUnsupportedFunction(final String scriptName, final String functionName, final Integer lineNumber);
	
	public void readSupportedParameter(final String scriptName, final String parameterType, final String parameterName);
	public void readSupportedParameterWithWarn(final String scriptName, final String parameterType, final String parameterName, final String warning);
	public void readUnsupportedParameter(final String scriptName, final String parameterType, final String parameterName);
		
}
