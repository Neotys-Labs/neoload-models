package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Writes the dotted keys of {@code project_settings} (such as {@code qtest.enabled}) as nested maps, mirroring
 * {@link ProjectSettingsDeserializer}:
 * <pre>
 * project_settings:
 *   qtest:
 *     enabled: true
 * </pre>
 * Boolean and integer settings are written with their type instead of as strings, as the schema expects.
 */
public final class ProjectSettingsSerializer extends StdSerializer<Map<String, String>> {
	private static final long serialVersionUID = 1L;

	static final String SEPARATOR = ".";

	private static final Set<String> BOOLEAN_SETTINGS = ImmutableSet.of(
			"dynatrace.enabled",
			"qtest.enabled",
			"qtest.create_defects",
			"global_assertions.apply_on_custom_actions");
	private static final Set<String> INTEGER_SETTINGS = ImmutableSet.of(
			"qtest.project_id",
			"qtest.cycle_id");
	private static final Pattern INTEGER = Pattern.compile("-?\\d+");

	@SuppressWarnings({"unchecked", "rawtypes"})
	public ProjectSettingsSerializer() {
		super((Class<Map<String, String>>) (Class) Map.class);
	}

	@Override
	public boolean isEmpty(final SerializerProvider provider, final Map<String, String> settings) {
		return settings == null || settings.isEmpty();
	}

	@Override
	public void serialize(final Map<String, String> settings, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		final ObjectNode root = JsonNodeFactory.instance.objectNode();
		for (final Map.Entry<String, String> setting : settings.entrySet()) {
			put(root, setting.getKey(), toNode(setting.getKey(), setting.getValue()));
		}
		provider.defaultSerializeValue(root, generator);
	}

	/**
	 * Puts the value under the nested path of the dotted key. A key whose path is already taken by a value
	 * (such as {@code a.b} after {@code a}) is kept dotted at the root.
	 */
	private static void put(final ObjectNode root, final String key, final JsonNode value) {
		final String[] segments = key.split(Pattern.quote(SEPARATOR), -1);
		ObjectNode parent = root;
		for (int i = 0; i < segments.length - 1; i++) {
			final JsonNode child = parent.get(segments[i]);
			if (child == null) {
				parent = parent.putObject(segments[i]);
			} else if (child.isObject()) {
				parent = (ObjectNode) child;
			} else {
				root.set(key, value);
				return;
			}
		}
		final String name = segments[segments.length - 1];
		if (parent.has(name)) {
			root.set(key, value);
		} else {
			parent.set(name, value);
		}
	}

	private static JsonNode toNode(final String key, final String value) {
		if (value == null) {
			return JsonNodeFactory.instance.nullNode();
		}
		if (BOOLEAN_SETTINGS.contains(key) && ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))) {
			return JsonNodeFactory.instance.booleanNode(Boolean.parseBoolean(value));
		}
		if (INTEGER_SETTINGS.contains(key) && INTEGER.matcher(value).matches()) {
			return JsonNodeFactory.instance.numberNode(Long.parseLong(value));
		}
		return JsonNodeFactory.instance.textNode(value);
	}
}
