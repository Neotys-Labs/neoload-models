package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.SaveString;

public class SaveStringWriter extends JavascriptWriter {

	private static final String CONTENT_FIRST_PART = "// int lr_save_string( const char *param_value, const char *param_name);\ncontext.variableManager.setValue(\"";

	public SaveStringWriter(final SaveString saveString) {		
		super(saveString);
	}
	
	@Override
	protected String getJavascriptContent() {
		final StringBuilder content = new StringBuilder(CONTENT_FIRST_PART);
		content.append(((SaveString)element).getVariableName());
		content.append("\", \"");
		content.append(((SaveString)element).getVariableValue());
		content.append("\");");				
		return content.toString();
	}
	public static SaveStringWriter of(final SaveString saveString) {
		return new SaveStringWriter(saveString);
	}
}
