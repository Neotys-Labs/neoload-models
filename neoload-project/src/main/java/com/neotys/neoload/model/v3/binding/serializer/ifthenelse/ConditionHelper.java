package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import com.neotys.neoload.model.v3.binding.serializer.ConditionLexer;
import com.neotys.neoload.model.v3.binding.serializer.ConditionParser;
import com.neotys.neoload.model.v3.binding.serializer.DefaultErrorListener;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ConditionHelper {

	private ConditionHelper() {
		super();
	}

	// Operands made only of these characters are emitted as-is (grammar rule WORD); everything else
	// (empty, spaces, operators, quotes...) must be quoted so it re-parses to the same operand.
	private static final String WORD_PATTERN = "[a-zA-Z0-9${}_]+";

	// Operator keywords must be quoted when they appear as an operand, otherwise the lexer tokenizes
	// them as the operator instead of as a value (e.g. operand2 == "contains").
	private static final Set<String> RESERVED_WORDS = buildReservedWords();

	private static Set<String> buildReservedWords() {
		final Set<String> words = new HashSet<>();
		for (final Condition.Operator operator : Condition.Operator.values()) {
			for (final String name : operator.getNames()) {
				if (name.matches(WORD_PATTERN)) {
					words.add(name);
				}
			}
		}
		words.add("find_regexp"); // present in the grammar but not in the Operator enum
		return words;
	}

	/**
	 * Serializes a {@link Condition} to its compact textual form {@code operand1 operator operand2},
	 * inverse of {@link #convertToCondition(String)}.
	 */
	public static String convertToString(final Condition condition) {
		if (condition == null) return null;

		final StringBuilder builder = new StringBuilder();
		builder.append(escapeOperand(condition.getOperand1()));
		builder.append(' ').append(condition.getOperator().getNames().get(0));
		condition.getOperand2().ifPresent(operand2 -> builder.append(' ').append(escapeOperand(operand2)));
		return builder.toString();
	}

	private static String escapeOperand(final String operand) {
		if (operand != null && operand.matches(WORD_PATTERN) && !RESERVED_WORDS.contains(operand)) {
			return operand;
		}
		final String value = (operand != null) ? operand : "";
		// Use single quotes when the value contains double quotes but no single quote, otherwise
		// double quotes (escaping backslashes and inner double quotes).
		if (value.contains("\"") && !value.contains("'")) {
			return "'" + value + "'";
		}
		return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

	public static Condition convertToCondition(final String input) throws IOException {
		// Normalise condition
		final String conditionAsText = (input != null) ? input : "";
		
		// Manages the errors
		final DefaultErrorListener errorListener = new DefaultErrorListener();
		
		// Lexer
		final ConditionLexer lexer = new ConditionLexer(CharStreams.fromString(conditionAsText));
		lexer.removeErrorListeners();
	    // Tokens
	    final CommonTokenStream tokens = new CommonTokenStream(lexer);
	    // Parser
	    final ConditionParser parser = new ConditionParser(tokens);
	    parser.removeErrorListeners();
	    parser.addErrorListener(errorListener);
	    // Context
	    final ConditionParser.ConditionContext context;
	    try {
	    	context = parser.condition();
	    }
	    catch (final Exception e) {
	    	throw newIOException(conditionAsText, Collections.singletonList(e.getMessage()));
		}
	    
	    // Throw the errors if necessary
	    final List<String> errors = errorListener.getErrors();
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
