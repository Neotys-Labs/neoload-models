package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.binding.converter.IterationDurationHelper;
import com.neotys.neoload.model.v3.project.scenario.IterationLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import java.io.IOException;
import java.time.temporal.ChronoUnit;

/**
 * Reads a {@link LoadDuration}: {@code <n> iteration(s)} gives an {@link IterationLoadDuration}, otherwise a duration
 * read by {@link DurationParser}, a plain number meaning seconds, gives a {@link TimeLoadDuration}.
 * Text that is neither fails the read with an {@link com.neotys.neoload.model.v3.binding.converter.InvalidDurationException}.
 */
public final class LoadDurationDeserializer extends StdDeserializer<LoadDuration> {
	private static final long serialVersionUID = 1L;

	public LoadDurationDeserializer() {
		super(LoadDuration.class);
	}

	@Override
	public LoadDuration deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		return parse(parser.getValueAsString());
	}

	static LoadDuration parse(final String text) {
		final Integer iterations = text == null ? null : IterationDurationHelper.convertToInteger(text.trim());
		if (iterations != null) {
			return IterationLoadDuration.builder().count(iterations).build();
		}
		return TimeLoadDuration.builder().value(DurationParser.parse(text, ChronoUnit.SECONDS)).build();
	}
}
