package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;

public final class MillisecondsDurationToStringConverter extends StdConverter<MillisecondsDuration, String> {
	@Override
	public String convert(final MillisecondsDuration input) {
		return input.toText();
	}
}
