package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.project.scenario.IterationLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import java.io.IOException;

/**
 * Writes a {@link LoadDuration} as text: a {@link TimeLoadDuration} in compact units ({@code 1m30s}), an
 * {@link IterationLoadDuration} as {@code <n> iterations}.
 */
public final class LoadDurationSerializer extends StdSerializer<LoadDuration> {
	private static final long serialVersionUID = 1L;

	public LoadDurationSerializer() {
		super(LoadDuration.class);
	}

	@Override
	public void serialize(final LoadDuration value, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(toText(value));
	}

	static String toText(final LoadDuration value) {
		if (value instanceof TimeLoadDuration) {
			return DurationParser.format(((TimeLoadDuration) value).getValue());
		}
		final int count = ((IterationLoadDuration) value).getCount();
		return count + (count == 1 ? " iteration" : " iterations");
	}
}
