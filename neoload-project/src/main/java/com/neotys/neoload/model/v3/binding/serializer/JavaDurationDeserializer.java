package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Reads a {@link Duration} of a second-based field with {@link DurationParser}, a plain number meaning seconds.
 * Text that is not a valid duration fails the read with an
 * {@link com.neotys.neoload.model.v3.binding.converter.InvalidDurationException}.
 */
public final class JavaDurationDeserializer extends StdDeserializer<Duration> {
	private static final long serialVersionUID = 1L;

	public JavaDurationDeserializer() {
		super(Duration.class);
	}

	@Override
	public Duration deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		return DurationParser.parse(parser.getValueAsString(), ChronoUnit.SECONDS);
	}
}
