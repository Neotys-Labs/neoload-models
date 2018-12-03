package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;

final class DeserializerHelper {
	private DeserializerHelper() {
        super();
    }

    protected static String asText(final JsonNode node, final String fieldName) {
    	String text = null;
        final JsonNode nodeText = node.get(fieldName);
        if (nodeText != null) {
        	text = nodeText.asText();
        }
        return text;
    }
    
    protected static <T> T asObject(final ObjectCodec codec, final JsonNode node, final String fieldName, final Class<T> clazz) throws JsonProcessingException {
        T object = null;
        final JsonNode nodeObject = node.get(fieldName);
        if (nodeObject != null) {
        	object = codec.treeToValue(nodeObject, clazz);
        			
        }
        return object;
    }
}