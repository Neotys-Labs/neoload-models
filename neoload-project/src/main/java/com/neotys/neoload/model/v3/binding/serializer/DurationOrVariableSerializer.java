package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationVariable;
import java.io.IOException;

/**
 * Writes a {@link DurationOrVariable} as text: a {@link DurationLiteral} in compact units, a
 * {@link DurationVariable} as its reference.
 */
public final class DurationOrVariableSerializer extends StdSerializer<DurationOrVariable> {
	private static final long serialVersionUID = 1L;

	public DurationOrVariableSerializer() {
		super(DurationOrVariable.class);
	}

	@Override
	public void serialize(final DurationOrVariable value, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(toText(value));
	}

	static String toText(final DurationOrVariable value) {
		if (value instanceof DurationLiteral) {
			return DurationParser.format(((DurationLiteral) value).getValue());
		}
		return ((DurationVariable) value).getValue();
	}
}
