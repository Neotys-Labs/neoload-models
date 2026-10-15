package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Deserializes a list of entries ({@code [{name: value}, ...]}) into a
 * {@code List<Map.Entry<String, Optional<String>>>}, keeping every name/value pair in YAML order,
 * including repeated names, interleaved names and duplicate pairs, mirroring
 * {@link NameValueListSerializer}. A bare string entry (e.g. {@code - name}) is read as a name
 * with no value; a single-entry map ({@code - name: value}) is read as a name with a value
 * (possibly an empty string).
 */
public final class NameValueListDeserializer extends StdDeserializer<List<Map.Entry<String, Optional<String>>>> {
	private static final long serialVersionUID = 1L;

	public NameValueListDeserializer() {
		super(JsonNode.class);
	}

	@Override
	public List<Map.Entry<String, Optional<String>>> deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		final List<Map.Entry<String, Optional<String>>> entries = new ArrayList<>();
		if (node != null) {
			node.forEach(entryNode -> addEntry(entries, entryNode));
		}
		return entries;
	}

	private static void addEntry(final List<Map.Entry<String, Optional<String>>> entries, final JsonNode entryNode) {
		if (entryNode.isTextual()) {
			entries.add(Maps.immutableEntry(entryNode.textValue(), Optional.empty()));
			return;
		}
		for (final Map.Entry<String, JsonNode> property : entryNode.properties()) {
			entries.add(Maps.immutableEntry(property.getKey(), Optional.ofNullable(property.getValue().isNull() ? null : property.getValue().asText())));
		}
	}
}
