package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

final class DefaultConditionErrorListener extends BaseErrorListener {
	private final List<String> errors = new ArrayList<>();

	protected DefaultConditionErrorListener() {
		super();
	}
   	
   	@Override
   	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
   		final StringBuilder error = new StringBuilder();
   		error.append("Position");
   		error.append(' ');
   		error.append(charPositionInLine);
   		error.append(' ');
   		error.append(msg);
   		errors.add(error.toString());
   	}
   	
   	protected List<String> getErrors() {
   		return errors;
   	}
}