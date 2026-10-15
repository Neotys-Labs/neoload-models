package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.Match;

import java.io.IOException;

/**
 * Writes a {@link Match} as its lower-case name ({@code any}/{@code all}), mirroring
 * {@link MatchDeserializer}. Unlike {@link com.neotys.neoload.model.v3.binding.converter.MatchToStringConverter}
 * it can be used as the content serializer of an {@code Optional<Match>}.
 */
public final class MatchSerializer extends StdSerializer<Match> {
	private static final long serialVersionUID = 2658043914872416071L;

	public MatchSerializer() {
		super(Match.class);
	}

	@Override
	public void serialize(final Match match, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(match.getName());
	}
}
