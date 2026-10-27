package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.variable.Offset;
import java.io.IOException;

/**
 * Writes an {@link Offset} as {@code <amount><unit>}, for instance {@code -5d}.
 */
public final class OffsetSerializer extends StdSerializer<Offset> {
	private static final long serialVersionUID = 1L;

	public OffsetSerializer() {
		super(Offset.class);
	}

	@Override
	public void serialize(final Offset offset, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(offset.toString());
	}
}
