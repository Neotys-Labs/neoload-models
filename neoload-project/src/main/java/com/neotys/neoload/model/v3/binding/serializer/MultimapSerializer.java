package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Serializes a {@code Multimap<String, Optional<String>>} as a list of entries
 * ({@code [{name: value}, ...]}), one per key/value pair, mirroring {@link MultimapDeserializer}.
 * An absent value is written as a bare name string (e.g. {@code - name}) instead of a
 * single-entry map, matching the pre-existing Header/Parameter round-trip behavior (a name-only
 * entry vs. one with an explicit empty value).
 */
public final class MultimapSerializer extends StdSerializer<Multimap<String, Optional<String>>> {
	private static final long serialVersionUID = 1L;

	public MultimapSerializer() {
		super(Multimap.class, false);
	}

	@Override
	public boolean isEmpty(final SerializerProvider provider, final Multimap<String, Optional<String>> value) {
		return (value == null) || value.isEmpty();
	}

	@Override
	public void serialize(final Multimap<String, Optional<String>> multimap, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartArray();
		for (final Map.Entry<String, Optional<String>> entry : multimap.entries()) {
			if (entry.getValue().isPresent()) {
				generator.writeStartObject();
				generator.writeStringField(entry.getKey(), entry.getValue().get());
				generator.writeEndObject();
			} else {
				generator.writeString(entry.getKey());
			}
		}
		generator.writeEndArray();
	}
}
