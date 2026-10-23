package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.NameValue;
import java.io.IOException;
import java.util.Map;

/**
 * Deserializes a {@link NameValue}. Three forms are accepted: a bare string
 * (e.g. {@code - name}) is read as a name with no value; a single-entry map
 * ({@code - name: value}) is read as a name with a value (possibly an empty string);
 * an object whose keys are exactly {@code name} and {@code value}
 * ({@code - name: n, value: v}) is read as that name and value. A single-entry map always
 * wins, so {@code - name: foo} is a name "name" with value "foo". {@link NameValueSerializer}
 * only writes the first two forms.
 */
public final class NameValueDeserializer extends StdDeserializer<NameValue> {
	private static final long serialVersionUID = 1L;
	private static final String NAME = "name";
	private static final String VALUE = "value";

	public NameValueDeserializer() {
		super(NameValue.class);
	}

	@Override
	public NameValue deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);
		if (node.isTextual()) {
			return NameValue.builder().name(node.textValue()).build();
		}
		if (isNameValueObject(node)) {
			return build(node.get(NAME).asText(), node.get(VALUE));
		}
		if (!node.isObject() || node.size() != 1) {
			return ctx.reportInputMismatch(this, "Expected a name, a single-entry map or a {name, value} object, got: %s", node);
		}
		final Map.Entry<String, JsonNode> property = node.properties().iterator().next();
		return build(property.getKey(), property.getValue());
	}

	private static boolean isNameValueObject(final JsonNode node) {
		return node.isObject() && node.size() == 2 && node.has(NAME) && node.has(VALUE);
	}

	private static NameValue build(final String name, final JsonNode value) {
		return NameValue.builder()
				.name(name)
				.value(DeserializerHelper.asText(value))
				.build();
	}
}
