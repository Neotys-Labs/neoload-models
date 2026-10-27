package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.variable.Offset;
import java.io.IOException;

/**
 * Reads an {@link Offset}. Text that is not an amount followed by a unit, or whose amount overflows an
 * {@code int}, fails the read.
 */
public final class OffsetDeserializer extends StdDeserializer<Offset> {
	private static final long serialVersionUID = 1L;

	public OffsetDeserializer() {
		super(Offset.class);
	}

	@Override
	public Offset deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final String text = parser.getValueAsString();
		return Offset.parse(text).orElseThrow(() -> new IllegalArgumentException("Incorrect offset: '" + text + "' is not a valid offset."));
	}
}
