package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Pacing;
import com.neotys.neoload.model.v3.project.userpath.PacingConstant;
import com.neotys.neoload.model.v3.project.userpath.PacingRandom;
import java.io.IOException;

public final class PacingDeserializer extends StdDeserializer<Pacing> {
	private static final long serialVersionUID = 1L;

	public PacingDeserializer() {
		super(Pacing.class);
	}

	@Override
	public Pacing deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);

		if (node.isObject()) {
			if (node.has(PacingConstant.VALUE)) {
				throw new IOException("'pacing' cannot mix a constant '" + PacingConstant.VALUE + "' with range bounds ('" + PacingRandom.MIN + "'/'" + PacingRandom.MAX + "').");
			}
			final PacingRandom.Builder builder = PacingRandom.builder();
			final String min = asText(node, PacingRandom.MIN);
			if (min != null) {
				builder.min(min);
			}
			final String max = asText(node, PacingRandom.MAX);
			if (max != null) {
				builder.max(max);
			}
			return builder.build();
		}
		if (node.isValueNode()) {
			return PacingConstant.builder().value(node.asText()).build();
		}
		throw new IOException("Unsupported 'pacing' value: " + node);
	}
}
