package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Reads the nested maps of {@code project_settings} into the dotted keys used by the NeoLoad writer and
 * converters, mirroring {@link ProjectSettingsSerializer}:
 * <pre>
 * project_settings:
 *   global_assertions:
 *     content_types: text/html,application/json
 * </pre>
 * is read as {@code global_assertions.content_types}. The deprecated dotted syntax is rewritten to nested maps
 * beforehand by {@code DeprecatedKeys}.
 */
public final class ProjectSettingsDeserializer extends StdDeserializer<Map<String, String>> {
	private static final long serialVersionUID = 1L;
	/**
	 * Settings that must be a YAML boolean, as in the schema, since a value such as {@code "yes"} would be read as
	 * {@code false}. The {@code dynatrace} and {@code qtest} booleans stay lenient: quoted values were accepted before.
	 */
	private static final Set<String> STRICT_BOOLEAN_SETTINGS = ImmutableSet.of("global_assertions.apply_on_custom_actions");

	public ProjectSettingsDeserializer() {
		super(Map.class);
	}

	@Override
	public Map<String, String> deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);
		if (!node.isObject()) {
			throw JsonMappingException.from(parser, "The project settings must be a map.");
		}
		final Map<String, String> settings = new LinkedHashMap<>();
		flatten(parser, "", node, settings);
		return settings;
	}

	private static void flatten(final JsonParser parser, final String prefix, final JsonNode node, final Map<String, String> settings)
			throws JsonMappingException {
		final Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			final Map.Entry<String, JsonNode> field = fields.next();
			final String key = prefix + field.getKey();
			final JsonNode value = field.getValue();
			if (value.isObject()) {
				flatten(parser, key + ProjectSettingsSerializer.SEPARATOR, value, settings);
			} else if (value.isArray()) {
				throw JsonMappingException.from(parser, "The project setting '" + key + "' must be a value or a map, not a list.");
			} else if (STRICT_BOOLEAN_SETTINGS.contains(key) && !value.isBoolean()) {
				throw JsonMappingException.from(parser, "The project setting '" + key + "' must be a boolean (true or false), not '"
						+ value.asText() + "'.");
			} else if (!value.isNull()) {
				settings.put(key, value.asText());
			}
		}
	}
}
