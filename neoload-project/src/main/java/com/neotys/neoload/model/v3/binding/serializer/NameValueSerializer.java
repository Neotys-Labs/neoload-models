package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.NameValue;
import java.io.IOException;

/**
 * Serializes a {@link NameValue} as a single-entry map ({@code name: value}), mirroring
 * {@link NameValueDeserializer}. An absent value is written as a bare name string
 * (e.g. {@code - name}) instead of a single-entry map, so a name-only entry stays distinct from
 * one with an explicit empty value.
 */
public final class NameValueSerializer extends StdSerializer<NameValue> {
	private static final long serialVersionUID = 1L;

	public NameValueSerializer() {
		super(NameValue.class);
	}

	@Override
	public void serialize(final NameValue nameValue, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		if (nameValue.getValue().isPresent()) {
			generator.writeStartObject();
			generator.writeStringField(nameValue.getName(), nameValue.getValue().get());
			generator.writeEndObject();
		} else {
			generator.writeString(nameValue.getName());
		}
	}
}
