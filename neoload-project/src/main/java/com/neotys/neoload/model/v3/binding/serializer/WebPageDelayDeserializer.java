package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import java.io.IOException;

/**
 * Reads the web page {@code think_time} field, which shares the step duration syntax
 * but rejects a name or description.
 */
public final class WebPageDelayDeserializer extends StdDeserializer<Delay> {
	private static final long serialVersionUID = -3187534229401672430L;

	public WebPageDelayDeserializer() {
		super(Delay.class);
	}

	@Override
	public Delay deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);
		return DelayHelper.read(node, true, false);
	}
}
