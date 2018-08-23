package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.SaveString;

public class SaveStringWriter extends JavascriptWriter {

	public SaveStringWriter(final SaveString saveString) {		
		super(saveString);
	}
	
	@Override
	protected String getJavascriptContent() {
		final StringBuilder content = new StringBuilder("context.variableManager.setValue(\"");
		content.append(((SaveString)element).getVariableName());
		content.append("\", ");
		final String variableValue = ((SaveString)element).getVariableValue();
		if(variableValue.startsWith("${") && variableValue.endsWith("}")){
			// NL Variable
			content.append("context.variableManager.getValue(\"");
			content.append(variableValue.substring(2, variableValue.length()-1));
			content.append("\")");
		} else {
			// Plain text
			content.append("\"");
			content.append(variableValue);
			content.append("\"");
		}		
		content.append(");");				
		return content.toString();
	}
	public static SaveStringWriter of(final SaveString saveString) {
		return new SaveStringWriter(saveString);
	}
}
