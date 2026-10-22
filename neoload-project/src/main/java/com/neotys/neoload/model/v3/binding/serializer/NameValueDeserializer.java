package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.NameValue;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Deserializes a {@link NameValue}, mirroring {@link NameValueSerializer}. A bare string
 * (e.g. {@code - name}) is read as a name with no value; a single-entry map
 * ({@code - name: value}) is read as a name with a value (possibly an empty string).
 */
public final class NameValueDeserializer extends StdDeserializer<NameValue> {
	private static final long serialVersionUID = 1L;

	public NameValueDeserializer() {
		super(NameValue.class);
	}

	@Override
	public NameValue deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);
		if (node.isTextual()) {
			return NameValue.builder().name(node.textValue()).build();
		}
		if (!node.isObject() || node.size() != 1) {
			return ctx.reportInputMismatch(this, "Expected a name or a single-entry map, got: %s", node);
		}
		final Map.Entry<String, JsonNode> property = node.properties().iterator().next();
		final JsonNode value = property.getValue();
		return NameValue.builder()
				.name(property.getKey())
				.value(Optional.ofNullable(value.isNull() ? null : value.asText()))
				.build();
	}
}
