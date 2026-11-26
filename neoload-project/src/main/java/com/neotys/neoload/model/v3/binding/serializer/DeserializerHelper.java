package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    /**
     * Reads the object held by a field of the node. A reading error names the field in its path: the object is read
     * from its own tree, so Jackson's reference chain would not include it.
     */
    protected static <T> Optional<T> asObject(final ObjectCodec codec, final JsonNode node, final String fieldName, final Class<? extends T> clazz) throws JsonProcessingException {
        final JsonNode nodeObject = node.get(fieldName);
        if (isMissing(nodeObject)) {
        	return Optional.empty();
        }
        try {
            return Optional.of(codec.treeToValue(nodeObject, clazz));
        } catch (final JsonMappingException e) {
            e.prependPath(clazz, fieldName);
            throw e;
        }
    }

    private static boolean isMissing(final JsonNode node) {
        return node == null || node.isNull();
    }
}
