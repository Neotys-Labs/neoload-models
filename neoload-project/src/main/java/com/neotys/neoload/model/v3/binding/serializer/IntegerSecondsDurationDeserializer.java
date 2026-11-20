package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.binding.converter.InvalidDurationException;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Reads a {@link Duration} of a second-based field like {@link JavaDurationDeserializer}, and also rejects a
 * milliseconds part and a value above {@link Integer#MAX_VALUE} seconds, the largest the runtime handles.
 */
public final class IntegerSecondsDurationDeserializer extends StdDeserializer<Duration> {
	private static final long serialVersionUID = 1L;
	private static final Duration MAX = Duration.ofSeconds(Integer.MAX_VALUE);

	public IntegerSecondsDurationDeserializer() {
		super(Duration.class);
	}

	@Override
	public Duration deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final String text = parser.getValueAsString();
		final Duration duration = DurationParser.parse(text, ChronoUnit.SECONDS);
		if (duration.getNano() != 0 || duration.compareTo(MAX) > 0) {
			throw new InvalidDurationException(text);
		}
		return duration;
	}
}
