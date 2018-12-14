package com.neotys.neoload.model.v3.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;

class VariableUtils {
    private static final String VARIABLE_REGEX  = "^\\$\\{(.+)}$";
    private static final Pattern PATTERN = Pattern.compile(VARIABLE_REGEX) ;

    private VariableUtils() {
    }

    static boolean isVariableSyntax(final String syntax) {
        if (isNullOrEmpty(syntax)) {
            return false;
        }
        return PATTERN.matcher(syntax).matches();
    }

    static String getVariableName(final String syntax) {
        if (syntax == null) {
            return null;
        }

        final Matcher matcher = PATTERN.matcher(syntax);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return syntax;
    }
}
