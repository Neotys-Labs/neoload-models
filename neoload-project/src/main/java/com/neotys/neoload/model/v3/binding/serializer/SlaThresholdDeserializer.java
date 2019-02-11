package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdHelper;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;

public final class SlaThresholdDeserializer extends StdDeserializer<SlaThreshold> {
	private static final long serialVersionUID = -9106590279518753024L;

	public SlaThresholdDeserializer() {
		super(PopulationPolicy.class);
	}

	@Override
	public SlaThreshold deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		return asThreshold(node);
	}

	protected static SlaThreshold asThreshold(final JsonNode node) throws IOException {
		return SlaThresholdHelper.convertToThreshold(node.asText());
	}
}