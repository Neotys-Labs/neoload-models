package com.neotys.neoload.model.v3.binding.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

final class PercentageHelper {
	public static final String UNIT = "%";
	public static final Pattern PATTERN = Pattern.compile("([-]?[0-9]+[\\.]?[0-9]*)([" + UNIT + "]?)");
	private static final DecimalFormat FORMAT = new DecimalFormat("###.###", new DecimalFormatSymbols(Locale.US));

	private PercentageHelper() {
		super();
	}

	protected static String convertToString(final Double input) {
		if (input == null) return null;

		// DecimalFormat is not thread-safe; it's better to clone it here
		return ((DecimalFormat) (FORMAT.clone())).format(input) + UNIT;
	}
	
	protected static Double convertToDouble(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return null;
		}
		String value = input.trim();
		if (Strings.isNullOrEmpty(value)) {
			return null;
		}

		final Matcher matcher = PATTERN.matcher(value);
		if (matcher.matches()) {
			value = matcher.group(1);
			try {
				return Double.valueOf(((DecimalFormat)(FORMAT.clone())).format(Double.valueOf(value)));
			} catch (final NumberFormatException exception) {
				return null;
			}
		}
		throw new IllegalArgumentException("The value '" + input + "' is not a valid percentage.");		
	}
}
