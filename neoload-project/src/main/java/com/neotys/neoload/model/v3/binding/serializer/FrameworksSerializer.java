package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.framework.Framework;

/**
 * Serializes a {@link Framework} list by wrapping each entry in a discriminator
 * object: {@code {builtin: ...}} when {@code parameters} is empty (= reference
 * to a built-in framework shipped by NeoLoad), {@code {custom: ...}} otherwise.
 */
public class FrameworksSerializer extends StdSerializer<List<Framework>> {
	private static final long serialVersionUID = 1L;

	public static final String BUILTIN = "builtin";
	public static final String CUSTOM = "custom";

	public FrameworksSerializer() {
		super(List.class, false);
	}

	@Override
	public boolean isEmpty(final SerializerProvider provider, final List<Framework> value) {
		return value == null || value.isEmpty();
	}

	@Override
	public void serialize(final List<Framework> frameworks, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartArray();
		for (final Framework framework : frameworks) {
			generator.writeStartObject();
			final String key = framework.getParameters().isEmpty() ? BUILTIN : CUSTOM;
			generator.writeObjectField(key, framework);
			generator.writeEndObject();
		}
		generator.writeEndArray();
	}
}