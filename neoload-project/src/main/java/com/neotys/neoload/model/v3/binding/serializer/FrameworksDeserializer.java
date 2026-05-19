package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.framework.Framework;

/**
 * Deserializes a {@link Framework} list where each entry is wrapped in a discriminator
 * object: {@code {builtin: ...}} for a reference to a built-in framework, or
 * {@code {custom: ...}} for a user-defined framework with its dynamic parameters.
 * The discriminator key is informational; the resulting {@code Framework} carries the
 * same shape and downstream code disambiguates by inspecting {@code parameters.isEmpty()}.
 */
public class FrameworksDeserializer extends StdDeserializer<List<Framework>> {
	private static final long serialVersionUID = 1L;

	public FrameworksDeserializer() {
		super(List.class);
	}

	@Override
	public List<Framework> deserialize(final JsonParser jsonParser, final DeserializationContext ctx) throws IOException {
		final List<Framework> frameworks = new ArrayList<>();
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode root = codec.readTree(jsonParser);

		final Iterator<JsonNode> items = root.elements();
		while (items.hasNext()) {
			final JsonNode item = items.next();
			final Iterator<String> keys = item.fieldNames();
			if (!keys.hasNext()) {
				continue;
			}
			final String key = keys.next();
			final JsonNode body = item.get(key);
			final Framework framework = codec.treeToValue(body, Framework.class);
			if (framework != null) {
				frameworks.add(framework);
			}
		}
		return frameworks;
	}
}