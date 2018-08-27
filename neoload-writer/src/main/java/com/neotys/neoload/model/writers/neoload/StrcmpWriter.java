package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.function.Function;
import com.neotys.neoload.model.function.Strcmp;

public class StrcmpWriter extends JavascriptWriter {

	public StrcmpWriter(final Strcmp strcmp) {		
		super(strcmp);
	}
	
	@Override
	protected String getJavascriptContent() {
		final String s0 =  ((Function)element).getArgs().get(0);
		final String s1 =  ((Function)element).getArgs().get(1);
		final StringBuilder content = new StringBuilder("var strcmp = String(");
		if(s0.startsWith("\"") && s0.endsWith("\"")){
			// Plain text
			content.append(s0);
		} else {
			// NL Variable
			content.append("context.variableManager.getValue(\"");
			content.append(WriterUtils.extractVariableName(s0));
			content.append("\")");
		}
		content.append(").localeCompare(String(");
		if(s1.startsWith("\"") && s1.endsWith("\"")){
			// Plain text
			content.append(s1);
		} else {
			// NL Variable
			content.append("context.variableManager.getValue(\"");
			content.append(WriterUtils.extractVariableName(s1));
			content.append("\")");
		}
		content.append(");\ncontext.variableManager.setValue(\"");
		content.append(element.getName());
		content.append("\", strcmp);");
		return content.toString();
	}
	
	public static StrcmpWriter of(final Strcmp strcmp) {
		return new StrcmpWriter(strcmp);
	}
}
