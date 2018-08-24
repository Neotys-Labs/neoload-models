package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.EvalString;

public class EvalStringWriter extends ElementWriter {

	public EvalStringWriter(final EvalString evalString) {		
		super(evalString);
	}
		
	public static EvalStringWriter of(final EvalString evalString) {
		return new EvalStringWriter(evalString);
	}
}
