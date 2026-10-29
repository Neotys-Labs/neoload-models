package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import java.io.IOException;
import java.time.Duration;

/**
 * Writes a {@link Duration} in compact units ({@code 1m30s}).
 */
public final class JavaDurationSerializer extends StdSerializer<Duration> {
	private static final long serialVersionUID = 1L;

	public JavaDurationSerializer() {
		super(Duration.class);
	}

	@Override
	public void serialize(final Duration duration, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(DurationParser.format(duration));
	}
}
