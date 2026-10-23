package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;

public final class SecondsDurationToStringConverter extends StdConverter<SecondsDuration, String> {
	@Override
	public String convert(final SecondsDuration input) {
		return input.toText();
	}
}
