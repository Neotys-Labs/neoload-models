package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.scenario.*;

import java.io.IOException;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asObject;
import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;
import static com.neotys.neoload.model.v3.project.Element.NAME;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.*;

public final class PopulationPolicyDeserializer extends StdDeserializer<PopulationPolicy> {
	private static final long serialVersionUID = -9100000271338565024L;

	public PopulationPolicyDeserializer() {
		super(PopulationPolicy.class);
	}

	private static LoadPolicy asLoadPolicy(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		// Constant Load Policy
		LoadPolicy loadPolicy = asObject(codec, node, CONSTANT_LOAD, ConstantLoadPolicy.class);
		if (loadPolicy != null) {
			return loadPolicy;
		}
		// Rampup Load Policy
		loadPolicy = asObject(codec, node, RAMPUP_LOAD, RampupLoadPolicy.class);
		if (loadPolicy != null) {
			return loadPolicy;
		}
		// Peaks Load Policy
		loadPolicy = asObject(codec, node, PEAKS_LOAD, PeaksLoadPolicy.class);
		if (loadPolicy != null) {
			return loadPolicy;
		}
		// Custom Load Policy if exists
		return asObject(codec, node, CUSTOM_LOAD, CustomLoadPolicy.class);
	}

	@Override
	public PopulationPolicy deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		String name = asText(node, NAME);
		final LoadPolicy loadPolicy = asLoadPolicy(codec, node);

		return PopulationPolicy.builder()
				.name(name)
				.loadPolicy(loadPolicy)
				.build();
	}
}