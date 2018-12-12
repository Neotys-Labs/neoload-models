package com.neotys.neoload.model.v3.util;

import com.google.common.base.Strings;


public class VariableUtils {
	private static final String NL_VARIABLE_START = "${";
	private static final String NL_VARIABLE_END = "}";


	private VariableUtils() {
	}

	public static String getVariableSyntax(final String name) {
		if (name.startsWith(NL_VARIABLE_START) && name.endsWith(NL_VARIABLE_END)) {
			return name;
		}
		return NL_VARIABLE_START + name + NL_VARIABLE_END;
	}

	public static String getVariableName(final String syntax) {
		if (syntax != null && syntax.startsWith(NL_VARIABLE_START) && syntax.endsWith(NL_VARIABLE_END)) {
			return syntax.substring(NL_VARIABLE_START.length(), syntax.length() - NL_VARIABLE_END.length());
		}
		return syntax;
	}

	public static boolean isVariableSyntax(final String syntax) {
		return !Strings.isNullOrEmpty(syntax) && syntax.startsWith(NL_VARIABLE_START) && syntax.endsWith(NL_VARIABLE_END);
	}
}
