package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.converter.MillisecondsDurationToStringConverter;
import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import java.io.IOException;

/**
 * Writes a {@link Duration}: a scalar for a constant, an object with {@code min} / {@code max} for a
 * random one. Durations are formatted back from milliseconds; {@code min} is written only when it is
 * present and not zero. The fields can also be written inline, for steps that carry their own
 * {@code name} / {@code description}.
 */
public final class DurationSerializer extends StdSerializer<Duration> {
	private static final long serialVersionUID = 1L;
	private static final String ZERO = "0";
	private static final MillisecondsDurationToStringConverter TO_TEXT = new MillisecondsDurationToStringConverter();

	public DurationSerializer() {
		super(Duration.class);
	}

	@Override
	public void serialize(final Duration duration, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		if (duration instanceof DurationConstant) {
			generator.writeString(toText(((DurationConstant) duration).getValue()));
		} else if (duration instanceof DurationRandom) {
			generator.writeStartObject();
			writeFields(generator, duration);
			generator.writeEndObject();
		}
	}

	static void writeFields(final JsonGenerator generator, final Duration duration) throws IOException {
		if (duration instanceof DurationConstant) {
			generator.writeStringField(DurationConstant.VALUE, toText(((DurationConstant) duration).getValue()));
		} else if (duration instanceof DurationRandom) {
			writeRandomFields(generator, (DurationRandom) duration);
		}
	}

	private static void writeRandomFields(final JsonGenerator generator, final DurationRandom random) throws IOException {
		final String min = random.getMin().map(DurationSerializer::toText).orElse(ZERO);
		if (!ZERO.equals(min)) {
			generator.writeStringField(DurationRandom.MIN, min);
		}
		generator.writeStringField(DurationRandom.MAX, toText(random.getMax()));
	}

	static String toText(final MillisecondsDuration duration) {
		return TO_TEXT.convert(duration);
	}
}
