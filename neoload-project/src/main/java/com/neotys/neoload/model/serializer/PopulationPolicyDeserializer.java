package com.neotys.neoload.model.serializer;

import static com.neotys.neoload.model.serializer.PopulationPolicyConstants.FILED_CONSTANT_LOAD;
import static com.neotys.neoload.model.serializer.PopulationPolicyConstants.FILED_NAME;
import static com.neotys.neoload.model.serializer.PopulationPolicyConstants.FILED_PEAKS_LOAD;
import static com.neotys.neoload.model.serializer.PopulationPolicyConstants.FILED_RAMPUP_LOAD;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.ImmutablePopulationPolicy;
import com.neotys.neoload.model.scenario.LoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;

public final class PopulationPolicyDeserializer extends StdDeserializer<PopulationPolicy>{
	private static final long serialVersionUID = -9100000271338565024L;

	public PopulationPolicyDeserializer() {
        super(PopulationPolicy.class);
    }

	private static LoadPolicy getLoadPolicy(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		JsonNode loadPolicyNode = node.get(FILED_CONSTANT_LOAD);
		if (loadPolicyNode != null) {
			return codec.treeToValue(loadPolicyNode, ConstantLoadPolicy.class);
		}

		loadPolicyNode = node.get(FILED_RAMPUP_LOAD);
		if (loadPolicyNode != null) {
			return codec.treeToValue(loadPolicyNode, RampupLoadPolicy.class);
		}
		
		loadPolicyNode = node.get(FILED_PEAKS_LOAD);
		if (loadPolicyNode != null) {
			return codec.treeToValue(loadPolicyNode, PeaksLoadPolicy.class);
		}
		
		return null;
	}
	
    @Override
    public PopulationPolicy deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
    	final ObjectCodec codec = parser.getCodec();
        final JsonNode node = codec.readTree(parser);
        
        final String name = node.get(FILED_NAME).asText();
        final LoadPolicy loadPolicy = getLoadPolicy(codec, node);
        
        return ImmutablePopulationPolicy.builder()
        		.name(name)
        		.loadPolicy(loadPolicy)
        		.build();
    }
}