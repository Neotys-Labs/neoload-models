package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringToWhenReleaseConverter extends StdConverter<String, WhenRelease> {
	private static final WhenRelease ERROR_VALUE = WhenRelease.builder().build();
	public static final Pattern PERCENTAGE_PATTERN = Pattern.compile("([-]?[1-9]+[\\.]?[0-9]*)?([%])");
	public static final Pattern POSITIVE_PATTERN = Pattern.compile("^[1-9]\\d*$");

	@Override
	public WhenRelease convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}


		if ("manual".equalsIgnoreCase(input)) {
			return WhenRelease.builder()
					.value(input.toLowerCase())
					.type(WhenRelease.Type.MANUAL)
					.build();
		}
		final Matcher percentageMatcher = PERCENTAGE_PATTERN.matcher(input);
		if (percentageMatcher.matches()) {
			return WhenRelease.builder()
					.value(percentageMatcher.group(1))
					.type(WhenRelease.Type.PERCENTAGE)
					.build();
		}
		final Matcher positiveMatcher = POSITIVE_PATTERN.matcher(input);
		if (positiveMatcher.matches()) {
			return WhenRelease.builder()
					.value(input)
					.type(WhenRelease.Type.VU_NUMBER)
					.build();
		}

		return ERROR_VALUE;
	}
}
