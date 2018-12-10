package com.neotys.neoload.model.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
final class TimeDurationHelper {
	private static final String ZERO = "0";
	
	private static final char HOURS = 'h';
	private static final char MINUTES = 'm';
	private static final char SECONDES = 's';

	private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)|(\\d+)s|(\\d+)m|(\\d+)h|(\\d+)m(\\d+)s|(\\d+)h(\\d+)s|(\\d+)h(\\d+)m|(\\d+)h(\\d+)m(\\d+)s");
	private static final int[] TIME_FACTORS = new int[] {0, 1, 1, 60, 60 * 60, 60, 1, 60 * 60, 1, 60 * 60, 60, 60 * 60, 60, 1};

	private TimeDurationHelper() {
		super();
	}

	protected static String convertToString(final Integer input) {
		if (input == null) return null;
		if (input <= 0) return ZERO;
		
		final int hours = input / 3600;
		final int minutes = (input - hours * 3600) / 60;
		final int secondes = (input - hours * 3600 - minutes * 60);
	    
	    final StringBuilder sb = new StringBuilder();
	    if (hours != 0) {
	    	sb.append(hours);
	    	sb.append(HOURS);
	    }
	    if (minutes != 0) {
	    	sb.append(minutes);
	    	sb.append(MINUTES);
	    }
	    if (secondes != 0) {
	    	sb.append(secondes);
	    	sb.append(SECONDES);
	    }
	    return sb.toString();		
	}
	
	protected static Integer convertToInteger(final String input) {
		final Matcher matcher = TIME_PATTERN.matcher(input);
		if (!matcher.matches()) {
			return null;				
		}

		int value = 0;
		for (int i = 1, ilength = matcher.groupCount(); i <= ilength; i++) {
			final String group = matcher.group(i);
			if (group != null) {
				value = value + Integer.valueOf(group) * TIME_FACTORS[i];
			}
		}
		return value;
	}
}
