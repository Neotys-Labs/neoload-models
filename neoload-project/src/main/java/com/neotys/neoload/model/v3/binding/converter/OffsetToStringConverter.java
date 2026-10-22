package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.variable.Offset;

public final class OffsetToStringConverter extends StdConverter<Offset, String> {
	@Override
	public String convert(final Offset input) {
		return input.toText();
	}
}
