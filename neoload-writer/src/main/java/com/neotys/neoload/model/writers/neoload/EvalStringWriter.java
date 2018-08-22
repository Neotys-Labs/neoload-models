package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.EvalString;

public class EvalStringWriter extends JavascriptWriter {

	private static final String CONTENT = "// char *lr_eval_string( char *instring  );";

	public EvalStringWriter(final EvalString evalString) {		
		super(evalString);
	}
	
	@Override
	protected String getJavascriptContent() {
		return CONTENT;
	}
	public static EvalStringWriter of(final EvalString evalString) {
		return new EvalStringWriter(evalString);
	}
}
