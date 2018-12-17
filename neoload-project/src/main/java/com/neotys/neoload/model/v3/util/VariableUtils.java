package com.neotys.neoload.model.v3.util;

import java.util.Optional;

import com.google.common.base.Strings;


public class VariableUtils {
	private static final String NL_VARIABLE_START = "${";
	private static final String NL_VARIABLE_END = "}";


	private VariableUtils() {
	}

	public static String getVariableSyntax(final String name) {
		if (Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("The parameter 'name' must not be null or empty.");
		}
		final String cleanedName = name.trim();
		if (cleanedName.isEmpty()) {
			throw new IllegalArgumentException("The parameter 'name' must not be blank.");
		}
		
		if (cleanedName.startsWith(NL_VARIABLE_START) && cleanedName.endsWith(NL_VARIABLE_END)) {
			return cleanedName;
		}
		return NL_VARIABLE_START + cleanedName + NL_VARIABLE_END;
	}

	public static Optional<String> getVariableName(final String syntax) {
		if (isVariableSyntax(syntax)) {
			final String cleanedSyntax = syntax.trim();
			return Optional.ofNullable(cleanedSyntax.substring(NL_VARIABLE_START.length(), cleanedSyntax.length() - NL_VARIABLE_END.length()).trim());
		}
		return Optional.empty();
	}

	public static boolean isVariableSyntax(final String syntax) {
		if (Strings.isNullOrEmpty(syntax)) {
			return false;
		}
		final String cleanedSyntax = syntax.trim();
		if (cleanedSyntax.isEmpty()) {
			return false;
		}
		return cleanedSyntax.startsWith(NL_VARIABLE_START) && cleanedSyntax.endsWith(NL_VARIABLE_END);
	}
}
