package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.function.Atoi;

public class AtoiWriter extends JavascriptWriter {

	public AtoiWriter(final Atoi atoi) {		
		super(atoi);
	}
	
	@Override
	protected String getJavascriptContent() {
		final StringBuilder content = new StringBuilder("context.variableManager.setValue(\"");
		content.append(((Atoi)element).getName());
		content.append("\", parseInt(");
		final String variableValue = ((Atoi)element).getArgs().get(0);
		if(WriterUtils.isNLVariable(variableValue)){
			// NL Variable
			content.append("context.variableManager.getValue(\"");
			content.append(WriterUtils.extractVariableName(variableValue));
			content.append("\")");
		} else {
			// Plain text
			content.append("\"");
			content.append(variableValue);
			content.append("\"");
		}		
		content.append("));");				
		return content.toString();
	}
	public static AtoiWriter of(final Atoi atoi) {
		return new AtoiWriter(atoi);
	}
}
