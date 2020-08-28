package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;

import java.util.regex.Pattern;

public final class StringToWhenReleaseConverter extends StdConverter<String, WhenRelease> {
	private static final WhenRelease ERROR_VALUE = WhenRelease.builder().build();
	public static final String PERCENTAGE_REGEX = "\\d+(?:\\.\\d+)?%";
	public static final String POSITIVE_REGEX = "^[1-9]\\d*$";

	@Override
	public WhenRelease convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}

		if (input.equalsIgnoreCase("manual")) {
			return WhenRelease.builder()
					.value(input.toLowerCase())
					.type(WhenRelease.Type.MANUAL)
					.build();
		}
		if (Pattern.matches(PERCENTAGE_REGEX, input)) {
			return WhenRelease.builder()
					.value(input.substring(0, input.length() - 1))
					.type(WhenRelease.Type.PERCENTAGE)
					.build();
		}
		if (Pattern.matches(POSITIVE_REGEX, input)) {
			return WhenRelease.builder()
					.value(input)
					.type(WhenRelease.Type.VU_NUMBER)
					.build();
		}


		return ERROR_VALUE;
	}
}
