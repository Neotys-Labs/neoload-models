package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.Header;

/**
 * Serializes a {@link Header} as a single-entry map {@code {name: value}}, mirroring
 * {@link HeaderDeserializer}.
 */
public final class HeaderSerializer extends StdSerializer<Header> {
	private static final long serialVersionUID = 3419041330155288066L;

	public HeaderSerializer() {
		super(Header.class);
	}

	@Override
	public void serialize(final Header header, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartObject();
		generator.writeStringField(header.getName(), header.getValue().orElse(null));
		generator.writeEndObject();
	}
}
