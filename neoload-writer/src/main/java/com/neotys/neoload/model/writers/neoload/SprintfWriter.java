package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.Sprintf;

public class SprintfWriter extends JavascriptWriter {

	public SprintfWriter(final Sprintf sprintf) {
		super(sprintf);
	}

	@Override
	protected String getJavascriptContent() {
		final Sprintf sprintf = ((Sprintf) element);
		final StringBuilder content = new StringBuilder("var sprintf = java.lang.String.format(");
		content.append(sprintf.getFormat());
		for (final String arg : sprintf.getArgs()) {
			content.append(", context.variableManager.getValue(\"");
			content.append(arg);
			content.append("\")");
		}
		content.append(");\n");
		content.append("context.variableManager.setValue(\"");
		content.append(sprintf.getVariableName());		
		content.append("\", sprintf);");
		return content.toString();
	}

	public static SprintfWriter of(Sprintf sprintf) {
		return new SprintfWriter(sprintf);
	}
}
