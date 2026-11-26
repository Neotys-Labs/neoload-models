package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.converter.PercentageParser;
import com.neotys.neoload.model.v3.project.userpath.PercentageLiteral;
import com.neotys.neoload.model.v3.project.userpath.PercentageOrVariable;
import com.neotys.neoload.model.v3.project.userpath.PercentageVariable;
import java.io.IOException;

/**
 * Writes a {@link PercentageOrVariable} as text: a {@link PercentageLiteral} always with its percent sign, a
 * {@link PercentageVariable} as its reference.
 */
public final class PercentageOrVariableSerializer extends StdSerializer<PercentageOrVariable> {
	private static final long serialVersionUID = 1L;

	public PercentageOrVariableSerializer() {
		super(PercentageOrVariable.class);
	}

	@Override
	public void serialize(final PercentageOrVariable value, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(toText(value));
	}

	static String toText(final PercentageOrVariable value) {
		if (value instanceof PercentageLiteral) {
			return PercentageParser.format(((PercentageLiteral) value).getValue());
		}
		return ((PercentageVariable) value).getValue();
	}
}
