package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Delay;

import java.io.IOException;

public class DelayDeserializer extends StdDeserializer<Delay> {
	private static final long serialVersionUID = 6277375929287990100L;

	protected DelayDeserializer() {
		super(Delay.class);
	}

	@Override
	public Delay deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		//TODO NNA
		return null;
	}
}
