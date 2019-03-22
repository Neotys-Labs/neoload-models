package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.ifthenelse.ConditionHelper;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.If;

import java.io.IOException;

public final class MatchDeserializer extends StdDeserializer<If.Match> {
	private static final long serialVersionUID = -9105976180532702954L;

	public MatchDeserializer() {
		super(If.Match.class);
	}

	@Override
	public If.Match deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);
		return If.Match.of(node.asText());
	}
}