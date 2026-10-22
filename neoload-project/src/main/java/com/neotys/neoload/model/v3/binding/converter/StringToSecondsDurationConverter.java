package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;

public final class StringToSecondsDurationConverter extends StdConverter<String, SecondsDuration> {
	@Override
	public SecondsDuration convert(final String input) {
		return SecondsDuration.parse(input);
	}
}
