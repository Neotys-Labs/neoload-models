package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Deserializes a list of entries ({@code [{name: value}, ...]}) into a
 * {@code Multimap<String, Optional<String>>}, preserving insertion order and multiple values per
 * key, mirroring {@link MultimapSerializer}. A bare string entry (e.g. {@code - name}) is read as
 * a name with no value; a single-entry map ({@code - name: value}) is read as a name with a
 * value (possibly an empty string).
 */
public final class MultimapDeserializer extends StdDeserializer<Multimap<String, Optional<String>>> {
	private static final long serialVersionUID = 1L;

	public MultimapDeserializer() {
		super(JsonNode.class);
	}

	@Override
	public Multimap<String, Optional<String>> deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		final Multimap<String, Optional<String>> multimap = LinkedHashMultimap.create();
		if (node != null) {
			node.forEach(entryNode -> addEntry(multimap, entryNode));
		}
		return multimap;
	}

	private static void addEntry(final Multimap<String, Optional<String>> multimap, final JsonNode entryNode) {
		if (entryNode.isTextual()) {
			multimap.put(entryNode.textValue(), Optional.empty());
			return;
		}
		for (final Map.Entry<String, JsonNode> property : entryNode.properties()) {
			multimap.put(property.getKey(), Optional.ofNullable(property.getValue().isNull() ? null : property.getValue().asText()));
		}
	}
}
