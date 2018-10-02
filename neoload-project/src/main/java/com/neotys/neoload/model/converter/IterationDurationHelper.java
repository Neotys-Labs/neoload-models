package com.neotys.neoload.model.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class IterationDurationHelper {
	private static final char SPACE = ' ';
	
	private static final String ITERATIONS = "iterations";
	private static final String ITERATION = "iteration";
	
	private static final Pattern ITERATION_PATTERN = Pattern.compile("(\\d+)iteration|(\\d+) iteration|(\\d+)iterations|(\\d+) iterations");
	private static final int[] ITERATION_FACTORS = new int[] {0, 1, 1, 1, 1};
	
	private IterationDurationHelper() {
		super();
	}

	protected static String convertToString(final Integer input) {
		if (input == null) return null;
		if (input <= 0) return null;
		
		final int value = input.intValue();
		final StringBuilder sb = new StringBuilder();
		sb.append(value);
		sb.append(SPACE);
	    if (value > 1) {
	    	sb.append(ITERATIONS);
	    }
	    else {
	    	sb.append(ITERATION);
	    }
	    return sb.toString();		
	}
	
	protected static Integer convertToInteger(final String input) {
		final Matcher matcher = ITERATION_PATTERN.matcher(input);
		if (!matcher.matches()) {
			return null;				
		}

		int value = 0;
		for (int i = 1, ilength = matcher.groupCount(); i <= ilength; i++) {
			final String group = matcher.group(i);
			if (group != null) {
				value = value + Integer.valueOf(group) * ITERATION_FACTORS[i];
			}
		}
		return Integer.valueOf(value);
	}

	
}
