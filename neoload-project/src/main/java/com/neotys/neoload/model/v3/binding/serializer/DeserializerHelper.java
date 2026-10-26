package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;

final class DeserializerHelper {
	private DeserializerHelper() {
        super();
    }

    protected static Optional<String> asText(final JsonNode node, final String fieldName) {
        return asText(node.get(fieldName));
    }

    protected static Optional<String> asText(final JsonNode node) {
        if (isMissing(node)) {
        	return Optional.empty();
        }
        return Optional.of(node.asText());
    }

    protected static <T> Optional<T> asObject(final ObjectCodec codec, final JsonNode node, final String fieldName, final Class<? extends T> clazz) throws JsonProcessingException {
        final JsonNode nodeObject = node.get(fieldName);
        if (isMissing(nodeObject)) {
        	return Optional.empty();
        }
        return Optional.of(codec.treeToValue(nodeObject, clazz));
    }

    private static boolean isMissing(final JsonNode node) {
        return node == null || node.isNull();
    }
}
