package com.neotys.neoload.model.serializer;

import static com.neotys.neoload.model.core.Element.NAME;
import static com.neotys.neoload.model.scenario.PopulationPolicy.CONSTANT_LOAD;
import static com.neotys.neoload.model.scenario.PopulationPolicy.PEAKS_LOAD;
import static com.neotys.neoload.model.scenario.PopulationPolicy.RAMPUP_LOAD;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.LoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class PopulationPolicyDeserializer extends StdDeserializer<PopulationPolicy>{
	private static final long serialVersionUID = -9100000271338565024L;

	public PopulationPolicyDeserializer() {
        super(PopulationPolicy.class);
    }

	private static LoadPolicy getLoadPolicy(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		JsonNode loadPolicyNode = node.get(CONSTANT_LOAD);
		if (loadPolicyNode != null) {
			return codec.treeToValue(loadPolicyNode, ConstantLoadPolicy.class);
		}

		loadPolicyNode = node.get(RAMPUP_LOAD);
		if (loadPolicyNode != null) {
			return codec.treeToValue(loadPolicyNode, RampupLoadPolicy.class);
		}
		
		loadPolicyNode = node.get(PEAKS_LOAD);
		if (loadPolicyNode != null) {
			return codec.treeToValue(loadPolicyNode, PeaksLoadPolicy.class);
		}
		
		return null;
	}
	
    @Override
    public PopulationPolicy deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
    	final ObjectCodec codec = parser.getCodec();
        final JsonNode node = codec.readTree(parser);
        
        String name = null;
        final JsonNode nodeName = node.get(NAME);
        if (nodeName != null) {
        	name = nodeName.asText();
        }
        final LoadPolicy loadPolicy = getLoadPolicy(codec, node);
        
        return PopulationPolicy.builder()
        		.name(name)
        		.loadPolicy(loadPolicy)
        		.build();
    }
}