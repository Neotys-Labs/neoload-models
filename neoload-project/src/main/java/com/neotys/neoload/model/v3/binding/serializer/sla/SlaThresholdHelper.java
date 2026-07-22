package com.neotys.neoload.model.v3.binding.serializer.sla;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.neotys.neoload.model.v3.binding.serializer.DefaultErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdParser.ThresholdContext;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;

public final class SlaThresholdHelper {

	private SlaThresholdHelper() {
		super();
	}

	/**
	 * Serializes a {@link SlaThreshold} to its compact textual form, inverse of
	 * {@link #convertToThreshold(String)}. Condition values are written without a unit: the parser
	 * reads them back with {@code KPI.toValue(value, null)} which keeps the stored (already
	 * normalized) value unchanged.
	 */
	public static String convertToString(final SlaThreshold threshold) {
		if (threshold == null) return null;

		final StringBuilder builder = new StringBuilder();
		builder.append(threshold.getKpi().friendlyName());

		if (threshold.getKpi() == KPI.PERC_TRANSACTION_RESP_TIME) {
			builder.append(" (p").append(threshold.getPercent().orElse(SlaThreshold.DEFAULT_PERCENT)).append(')');
		}

		for (final SlaThresholdCondition condition : threshold.getConditions()) {
			builder.append(' ').append(condition.getSeverity().friendlyName())
					.append(' ').append(condition.getOperator().friendlyName())
					.append(' ').append(formatValue(condition.getValue()));
		}

		builder.append(' ').append(threshold.getScope().friendlyName());
		return builder.toString();
	}

	// Renders the value without a unit and without an exponent, as an INTEGER or DOUBLE token.
	private static String formatValue(final Double value) {
		return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
	}

	public static SlaThreshold convertToThreshold(final String input) throws IOException {
		// Normalyze threshold
		final String thresholdAsText = (input != null) ? input : "";
		
		// Manages the errors
		final DefaultErrorListener thresholdErrorListener = new DefaultErrorListener();
		
		// Lexer
		final SlaThresholdLexer lexer = new SlaThresholdLexer(CharStreams.fromString(thresholdAsText));
	    lexer.removeErrorListeners();
	    lexer.addErrorListener(thresholdErrorListener);
	    // Tokens
	    final CommonTokenStream tokens = new CommonTokenStream(lexer);
	    // Parser
	    final SlaThresholdParser parser = new SlaThresholdParser(tokens);
	    parser.removeErrorListeners();
	    parser.addErrorListener(thresholdErrorListener);
	    // Context
	    final ThresholdContext context;
	    try {
	    	context = parser.threshold();
	    }
	    catch (final Exception e) {
	    	throw newIOException(thresholdAsText, Arrays.asList(e.getMessage()));
		}
	    
	    // Throw the errors if necessary
	    final List<String> errors = thresholdErrorListener.getErrors();
	    if (!errors.isEmpty()) {
	    	throw newIOException(thresholdAsText, errors);
	    }
	    
	    // Threshold visitor
	    final DefaultSlaThresholdVisitor visitor = new DefaultSlaThresholdVisitor();
	    return visitor.visit(context);
	}
	
	private static IOException newIOException(final String threshold, final List<String> errors) {
		final StringBuilder message = new StringBuilder();
   		message.append(threshold);
   		message.append(" is not a valid threshold: ");
   		for (String error : errors) {
			message.append(System.lineSeparator());
			message.append(error);
		}
   		return new IOException(message.toString());
	}
}
