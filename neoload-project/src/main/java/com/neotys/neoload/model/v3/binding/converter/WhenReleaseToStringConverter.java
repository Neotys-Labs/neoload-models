package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;

public final class WhenReleaseToStringConverter extends StdConverter<WhenRelease, String> {

	@Override
	public String convert(final WhenRelease whenRelease) {
		if (whenRelease == null) return null;

		String convertedValue = "";
		final WhenRelease.Type type = whenRelease.getType();

		switch (type) {
			case MANUAL:
				convertedValue = "manual";
				break;
			case PERCENTAGE:
				convertedValue = whenRelease.getValue() + "%";
				break;
			case VU_NUMBER:
				convertedValue = (String) whenRelease.getValue();

		}

		return convertedValue;
	}
}
