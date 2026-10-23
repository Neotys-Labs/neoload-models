package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.variable.Offset;

public final class StringToOffsetConverter extends StdConverter<String, Offset> {
	@Override
	public Offset convert(final String input) {
		return Offset.parse(input);
	}
}
