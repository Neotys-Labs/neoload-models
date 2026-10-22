package com.neotys.neoload.model.v3.binding.converter;

import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.variable.IncrementTimeUnit;
import java.util.regex.Pattern;

/**
 * Describes one duration grammar: the regex a text must match, the units it allows (ordered
 * {@code h}, {@code m}, {@code s}, {@code ms}) and the unit a plain number is expressed in.
 */
public final class DurationGrammar {
	private static final String SECONDS_UNITS = "(?:(?<h>\\d+)h)?(?:(?<m>\\d+)m)?(?:(?<s>\\d+)s)?";
	private static final String MILLISECONDS_UNITS = "(?:(?<h>\\d+)h\\s*)?(?:(?<m>\\d+)m\\s*)?(?:(?<s>\\d+)s\\s*)?(?:(?<ms>\\d+)ms)?";

	public static final DurationGrammar MILLISECONDS = new DurationGrammar(
			"^\\s*(?:(?<plain>\\d+)|(?=\\d)" + MILLISECONDS_UNITS + "|\\$\\{(?<var>" + Duration.VARIABLE_NAME_PATTERN + "(?:\\." + Duration.VARIABLE_NAME_PATTERN + ")*)\\})\\s*$",
			"ms", new long[] {3_600_000L, 60_000L, 1_000L, 1L}, Long.MAX_VALUE, true);

	public static final DurationGrammar SECONDS = new DurationGrammar(
			"^(?:(?<plain>\\d+)|(?=\\d)" + SECONDS_UNITS + ")$",
			"s", new long[] {3_600L, 60L, 1L, 0L}, Integer.MAX_VALUE, false);

	public static final DurationGrammar OFFSET = new DurationGrammar(IncrementTimeUnit.PATTERN.pattern(), null, new long[0], Integer.MAX_VALUE, false);

	private final Pattern pattern;
	private final String defaultUnit;
	private final long[] unitSizes;
	private final long maxValue;
	private final boolean variableAllowed;

	private DurationGrammar(final String regex, final String defaultUnit, final long[] unitSizes, final long maxValue, final boolean variableAllowed) {
		this.pattern = Pattern.compile(regex);
		this.defaultUnit = defaultUnit;
		this.unitSizes = unitSizes;
		this.maxValue = maxValue;
		this.variableAllowed = variableAllowed;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getDefaultUnit() {
		return defaultUnit;
	}

	/**
	 * @return the size of {@code h}, {@code m}, {@code s} and {@code ms} in the default unit, 0 for a unit the grammar does not allow
	 */
	public long[] getUnitSizes() {
		return unitSizes.clone();
	}

	public long getMaxValue() {
		return maxValue;
	}

	public boolean isVariableAllowed() {
		return variableAllowed;
	}
}
