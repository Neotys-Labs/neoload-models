package com.neotys.neoload.model.writers.neoload;

import java.util.stream.Collectors;

import com.neotys.neoload.model.repository.EvalString;

public class EvalStringWriter extends JavascriptWriter {

	public EvalStringWriter(final EvalString evalString) {		
		super(evalString);
	}
	
	@Override
	protected String getJavascriptContent() {
		final StringBuilder content = new StringBuilder("context.variableManager.setValue(\"");
		content.append(((EvalString)element).getVariableName());
		content.append("\", ");
		if(((EvalString)element).getContent().isEmpty()){
			content.append("\"\");");
			return content.toString();
		}
		for(final String part : ((EvalString)element).getContent().stream().map(part -> {
			return part.map(
					text -> {return "\""+text+"\"";}, 
					variable -> {return "context.variableManager.getValue(\"" + variable.getName() + "\")";});
		}).collect(Collectors.toList())){
			content.append(part).append("+");
		}				
		return content.toString().substring(0, content.length()-1)+");";
	}
	public static EvalStringWriter of(final EvalString evalString) {
		return new EvalStringWriter(evalString);
	}
}
