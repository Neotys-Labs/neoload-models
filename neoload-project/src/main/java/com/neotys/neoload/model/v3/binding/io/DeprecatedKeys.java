package com.neotys.neoload.model.v3.binding.io;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neotys.neoload.model.v3.project.userpath.HttpBasedRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * Rewrites the deprecated {@code followRedirects} key of {@code request} and {@code soap_request} steps
 * to {@code follow_redirects} before binding, and rejects a step declaring both keys.
 */
final class DeprecatedKeys {
	private static final String REQUEST = "request";
	private static final String SOAP_REQUEST = "soap_request";

	private DeprecatedKeys() {
	}

	static void normalize(final JsonNode node) throws JsonMappingException {
		if (node == null) {
			return;
		}
		if (node.isObject()) {
			final Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
			while (fields.hasNext()) {
				final Map.Entry<String, JsonNode> field = fields.next();
				if (isRequestKey(field.getKey()) && field.getValue().isObject()) {
					renameFollowRedirects((ObjectNode) field.getValue());
				}
				normalize(field.getValue());
			}
			return;
		}
		for (final JsonNode child : node) {
			normalize(child);
		}
	}

	private static boolean isRequestKey(final String key) {
		return REQUEST.equals(key) || SOAP_REQUEST.equals(key);
	}

	private static void renameFollowRedirects(final ObjectNode request) throws JsonMappingException {
		if (!request.has(HttpBasedRequest.DEPRECATED_FOLLOW_REDIRECTS)) {
			return;
		}
		if (request.has(HttpBasedRequest.FOLLOW_REDIRECTS)) {
			throw new JsonMappingException(null, "A request cannot declare both '" + HttpBasedRequest.DEPRECATED_FOLLOW_REDIRECTS
					+ "' and '" + HttpBasedRequest.FOLLOW_REDIRECTS + "'. Use '" + HttpBasedRequest.FOLLOW_REDIRECTS + "' only.");
		}
		request.set(HttpBasedRequest.FOLLOW_REDIRECTS, request.remove(HttpBasedRequest.DEPRECATED_FOLLOW_REDIRECTS));
	}
}
