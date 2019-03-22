package com.neotys.neoload.model.v3.binding.serializer;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public final class DefaultErrorListener extends BaseErrorListener {
	private final List<String> errors = new ArrayList<>();
	
	public DefaultErrorListener() {
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
   	
   	public List<String> getErrors() {
   		return errors;
   	}
}