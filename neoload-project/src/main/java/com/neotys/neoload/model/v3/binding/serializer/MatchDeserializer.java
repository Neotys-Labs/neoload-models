package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Match;

import java.io.IOException;

public final class MatchDeserializer extends StdDeserializer<Match> {
	private static final long serialVersionUID = -9105976180532702954L;

	public MatchDeserializer() {
		super(Match.class);
	}

	@Override
	public Match deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);
		return Match.of(node.asText());
	}
}