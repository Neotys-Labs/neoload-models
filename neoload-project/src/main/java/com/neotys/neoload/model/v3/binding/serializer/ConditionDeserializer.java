package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Condition;

import java.io.IOException;

public final class ConditionDeserializer extends StdDeserializer<Condition> {
	private static final long serialVersionUID = -9106185970275309524L;

	public ConditionDeserializer() {
		super(Condition.class);
	}

	@Override
	public Condition deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);
		// TODO
		return Condition.builder().operand1("operand1").operator(Condition.Operator.EQUALS).operand2("operand2").build();
	}
}