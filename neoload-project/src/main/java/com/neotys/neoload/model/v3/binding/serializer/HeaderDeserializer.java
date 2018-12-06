package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Header;

public final class HeaderDeserializer extends StdDeserializer<Header> {
	private static final long serialVersionUID = 3419041330155288065L;

	public HeaderDeserializer() {
        super(List.class);
    }

	@SuppressWarnings("unchecked")
	@Override
    public Header deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
    	final ObjectCodec codec = parser.getCodec();
        final JsonNode node = codec.readTree(parser);
        
        // Reader Header object as Map object
        final Map<String, String> map = codec.treeToValue(node, Map.class);
        // Convert Map into Header
        if ((map != null) && (!map.isEmpty())) {
        	final Map.Entry<String, String> entry = map.entrySet().stream().findFirst().get();
        	return Header.builder()
        			.name(entry.getKey())
        			.value(Optional.ofNullable(entry.getValue()))
        			.build();
        }
        return null;
    }
}