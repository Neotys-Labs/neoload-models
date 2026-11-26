package com.neotys.neoload.model.v3.binding.io;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.HttpBasedRequestWithResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Rewrites deprecated keys to their current form before binding, and rejects a file declaring both forms:
 * <ul>
 *   <li>the {@code followRedirects} key of {@code request} and {@code soap_request} steps becomes {@code follow_redirects};</li>
 *   <li>the dotted {@code dynatrace} and {@code qtest} keys of {@code project_settings} ({@code qtest.enabled: true})
 *       become nested maps ({@code qtest: {enabled: true}}); other dotted settings are rejected.</li>
 * </ul>
 */
final class DeprecatedKeys {
	private static final String REQUEST = "request";
	private static final String SOAP_REQUEST = "soap_request";
	private static final String SETTING_SEPARATOR = ".";
	private static final Set<String> DOTTED_SETTING_GROUPS = ImmutableSet.of("dynatrace", "qtest");

	private DeprecatedKeys() {
	}

	static void normalize(final JsonNode root) throws JsonMappingException {
		if (root != null && root.isObject() && root.get(Project.PROJECT_SETTINGS) instanceof ObjectNode) {
			nestDottedProjectSettings((ObjectNode) root.get(Project.PROJECT_SETTINGS));
		}
		renameKeys(root);
	}

	private static void renameKeys(final JsonNode node) throws JsonMappingException {
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
				renameKeys(field.getValue());
			}
			return;
		}
		for (final JsonNode child : node) {
			renameKeys(child);
		}
	}

	/**
	 * Rewrites the dotted keys of the project settings as nested maps, keeping the order of the settings.
	 */
	private static void nestDottedProjectSettings(final ObjectNode settings) throws JsonMappingException {
		final ObjectNode nested = settings.objectNode();
		final Iterator<Map.Entry<String, JsonNode>> fields = settings.fields();
		while (fields.hasNext()) {
			final Map.Entry<String, JsonNode> field = fields.next();
			checkDottedKeyIsDeprecatedAlias(field.getKey());
			put(nested, field.getKey(), field.getValue(), field.getKey());
		}
		settings.removeAll();
		settings.setAll(nested);
	}

	/**
	 * Only the settings that existed before the nested form ({@code dynatrace}, {@code qtest}) accept dotted keys.
	 */
	private static void checkDottedKeyIsDeprecatedAlias(final String key) throws JsonMappingException {
		final int separator = key.indexOf(SETTING_SEPARATOR);
		if (separator >= 0 && !DOTTED_SETTING_GROUPS.contains(key.substring(0, separator))) {
			throw new JsonMappingException(null, "The project setting '" + key + "' must be declared in its nested form, e.g. '"
					+ key.replace(SETTING_SEPARATOR, ": ") + "'.");
		}
	}

	/**
	 * Puts the value under the nested path of the dotted key; {@code path} is the full dotted path, for the error message.
	 */
	private static void put(final ObjectNode settings, final String key, final JsonNode value, final String path) throws JsonMappingException {
		final String[] segments = key.split(Pattern.quote(SETTING_SEPARATOR), -1);
		ObjectNode parent = settings;
		for (int i = 0; i < segments.length - 1; i++) {
			final JsonNode child = parent.get(segments[i]);
			if (child == null) {
				parent = parent.putObject(segments[i]);
			} else if (child.isObject()) {
				parent = (ObjectNode) child;
			} else {
				throw bothSettingForms(path);
			}
		}
		final String name = segments[segments.length - 1];
		final JsonNode existing = parent.get(name);
		if (existing == null) {
			parent.set(name, value);
		} else if (existing.isObject() && value.isObject()) {
			final Iterator<Map.Entry<String, JsonNode>> fields = value.fields();
			while (fields.hasNext()) {
				final Map.Entry<String, JsonNode> field = fields.next();
				put((ObjectNode) existing, field.getKey(), field.getValue(), path + SETTING_SEPARATOR + field.getKey());
			}
		} else {
			throw bothSettingForms(path);
		}
	}

	private static JsonMappingException bothSettingForms(final String dottedKey) {
		return new JsonMappingException(null, "The project settings cannot declare both '" + dottedKey
				+ "' and its nested form. Use the nested form only, e.g. '" + dottedKey.replace(SETTING_SEPARATOR, ": ") + "'.");
	}

	private static boolean isRequestKey(final String key) {
		return REQUEST.equals(key) || SOAP_REQUEST.equals(key);
	}

	private static void renameFollowRedirects(final ObjectNode request) throws JsonMappingException {
		if (!request.has(HttpBasedRequestWithResponse.DEPRECATED_FOLLOW_REDIRECTS)) {
			return;
		}
		if (request.has(HttpBasedRequestWithResponse.FOLLOW_REDIRECTS)) {
			throw new JsonMappingException(null, "A request cannot declare both '" + HttpBasedRequestWithResponse.DEPRECATED_FOLLOW_REDIRECTS
					+ "' and '" + HttpBasedRequestWithResponse.FOLLOW_REDIRECTS + "'. Use '" + HttpBasedRequestWithResponse.FOLLOW_REDIRECTS + "' only.");
		}
		request.set(HttpBasedRequestWithResponse.FOLLOW_REDIRECTS, request.remove(HttpBasedRequestWithResponse.DEPRECATED_FOLLOW_REDIRECTS));
	}
}
