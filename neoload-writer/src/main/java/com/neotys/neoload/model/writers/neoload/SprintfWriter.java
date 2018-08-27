package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.Sprintf;

public class SprintfWriter extends JavascriptWriter {

	public SprintfWriter(final Sprintf sprintf) {
		super(sprintf);
	}

	@Override
	protected String getJavascriptContent() {
		final Sprintf sprintf = ((Sprintf) element);
		final StringBuilder content = new StringBuilder("var sprintf = java.util.String.format(");
		content.append(sprintf.getFormat());
		for (final String arg : sprintf.getArgs()) {
			content.append(", context.variableManager.getValue(\"");
			content.append(arg);
			content.append("\")");
		}
		content.append(");\n");
		final String variableName = sprintf.getVariableName();
		if (WriterUtils.isNLVariable(variableName)) {
			// NL Variable
			content.append("context.variableManager.setValue(\"");
			content.append(WriterUtils.extractVariableName(variableName));
			content.append("\")");
		} else {
			// Plain text
			content.append("\"");
			content.append(variableName);
			content.append("\"");
		}
		content.append(", sprintf);");
		return content.toString();
	}

	public static SprintfWriter of(Sprintf sprintf) {
		return new SprintfWriter(sprintf);
	}
}
