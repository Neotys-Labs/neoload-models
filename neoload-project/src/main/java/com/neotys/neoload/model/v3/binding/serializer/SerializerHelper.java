package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

final class SerializerHelper {
	private SerializerHelper() {
        super();
    }

    protected static void writeStringIfPresent(final JsonGenerator generator, final String fieldName, final String value) throws IOException {
        if (value != null) {
        	generator.writeStringField(fieldName, value);
        }
    }
}
