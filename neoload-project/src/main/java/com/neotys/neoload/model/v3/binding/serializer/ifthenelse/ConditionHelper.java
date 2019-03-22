package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import com.neotys.neoload.model.v3.binding.serializer.ConditionLexer;
import com.neotys.neoload.model.v3.binding.serializer.ConditionParser;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class ConditionHelper {

	private ConditionHelper() {
		super();
	}

	public static Condition convertToCondition(final String input) throws IOException {
		// Normalise condition
		final String conditionAsText = (input != null) ? input : "";
		
		// Manages the errors
		final DefaultConditionErrorListener conditionErrorListener = new DefaultConditionErrorListener();
		
		// Lexer
		final ConditionLexer lexer = new ConditionLexer(CharStreams.fromString(conditionAsText));
	    // Tokens
	    final CommonTokenStream tokens = new CommonTokenStream(lexer);
	    // Parser
	    final ConditionParser parser = new ConditionParser(tokens);
	    parser.removeErrorListeners();
	    parser.addErrorListener(conditionErrorListener);
	    // Context
	    final ConditionParser.ConditionContext context;
	    try {
	    	context = parser.condition();
	    }
	    catch (final Exception e) {
	    	throw newIOException(conditionAsText, Arrays.asList(e.getMessage()));
		}
	    
	    // Throw the errors if necessary
	    final List<String> errors = conditionErrorListener.getErrors();
	    if (!errors.isEmpty()) {
	    	throw newIOException(conditionAsText, errors);
	    }
	    
	    // Condition visitor
	    final DefaultConditionVisitor visitor = new DefaultConditionVisitor();
	    return visitor.visit(context);
	}
	
	private static IOException newIOException(final String condition, final List<String> errors) {
		final StringBuilder message = new StringBuilder();
   		message.append(condition);
   		message.append(" is not a valid condition: ");
   		for (String error : errors) {
			message.append(System.lineSeparator());
			message.append(error);
		}
   		return new IOException(message.toString());
	}
}
