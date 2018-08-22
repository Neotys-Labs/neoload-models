package com.neotys.neoload.model.writers.neoload;

import java.util.stream.Collectors;

import com.neotys.neoload.model.repository.EvalString;

public class EvalStringWriter extends JavascriptWriter {

	private static final String CONTENT_FIRST_PART = "// char *lr_eval_string( char *instring  );\ncontext.variableManager.setValue(\"";

	public EvalStringWriter(final EvalString evalString) {		
		super(evalString);
	}
	
	@Override
	protected String getJavascriptContent() {
		final StringBuilder content = new StringBuilder(CONTENT_FIRST_PART);
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
