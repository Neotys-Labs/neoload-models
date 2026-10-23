package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;

public final class StringToMillisecondsDurationConverter extends StdConverter<String, MillisecondsDuration> {
	@Override
	public MillisecondsDuration convert(final String input) {
		return MillisecondsDuration.parse(input);
	}
}
