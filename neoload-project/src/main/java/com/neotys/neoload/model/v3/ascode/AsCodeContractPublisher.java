package com.neotys.neoload.model.v3.ascode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copies one custom-action family contract into the open as-code schema, the family's documentation
 * page, and the step-to-type registry. A second family is another contract, not another code path.
 */
public final class AsCodeContractPublisher {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Set<String> SLICE_FIELDS = Set.of("action", "parameter_names", "parameter_groups");
	private static final Pattern PUT = Pattern.compile("\\.put\\(\"([^\"]*)\", \"([^\"]*)\"\\)");
	private static final Path STEP_REGISTRY = Path.of(
			"neoload-project/src/main/java/com/neotys/neoload/model/v3/binding/serializer/CustomActionSteps.java");

	public PublishResult publish(final Path repository, final Path contractDirectory) throws IOException {
		final FamilyContract contract = FamilyContract.read(contractDirectory);
		final Path schema = openSchema(repository);
		final String originalSchema = Files.readString(schema);
		final String updatedSchema = updateSchema(originalSchema, contract);
		final boolean schemaChanged = !originalSchema.equals(updatedSchema);
		if (schemaChanged) {
			Files.writeString(schema, updatedSchema);
		}
		final Path documentation = repository.resolve(contract.documentation());
		final String originalPage = Files.readString(documentation);
		final String updatedPage = updatePage(originalPage, contract, previousSteps(originalSchema, contract.schemaKey()));
		final boolean pageChanged = !originalPage.equals(updatedPage);
		if (pageChanged) {
			Files.writeString(documentation, updatedPage);
		}
		final Path registry = repository.resolve(STEP_REGISTRY);
		final String originalRegistry = Files.readString(registry);
		final String updatedRegistry = updateRegistry(originalRegistry, previousSteps(originalSchema, contract.schemaKey()), contract);
		final boolean stepsChanged = !originalRegistry.equals(updatedRegistry);
		if (stepsChanged) {
			Files.writeString(registry, updatedRegistry);
		}
		return new PublishResult(schemaChanged, pageChanged, stepsChanged);
	}

	static Path openSchema(final Path repository) throws IOException {
		Path latest = null;
		int[] latestVersion = null;
		try (var directories = Files.list(repository.resolve("schemas"))) {
			for (final Path directory : directories.collect(Collectors.toList())) {
				final int[] version = versionOf(directory.getFileName().toString());
				if (version != null && (latestVersion == null || compare(version, latestVersion) > 0)) {
					latestVersion = version;
					latest = directory;
				}
			}
		}
		if (latest == null) {
			throw new IllegalArgumentException("No as-code schema version under " + repository);
		}
		return latest.resolve("as-code.schema.json");
	}

	private static int[] versionOf(final String name) {
		if (!name.startsWith("v")) {
			return null;
		}
		final String[] parts = name.substring(1).split("\\.");
		final int[] numbers = new int[parts.length];
		for (int index = 0; index < parts.length; index++) {
			try {
				numbers[index] = Integer.parseInt(parts[index]);
			} catch (final NumberFormatException e) {
				return null;
			}
		}
		return numbers;
	}

	private static int compare(final int[] left, final int[] right) {
		final int length = Math.max(left.length, right.length);
		for (int index = 0; index < length; index++) {
			final int difference = (index < left.length ? left[index] : 0) - (index < right.length ? right[index] : 0);
			if (difference != 0) {
				return difference;
			}
		}
		return 0;
	}

	private static Set<String> previousSteps(final String schema, final String schemaKey) throws IOException {
		final JsonNode slice = MAPPER.readTree(schema).at("/definitions/user_paths/" + schemaKey);
		return stepNames(slice);
	}

	static Set<String> stepNames(final JsonNode slice) {
		final Set<String> names = new LinkedHashSet<>();
		if (slice == null || !slice.isObject()) {
			return names;
		}
		slice.fieldNames().forEachRemaining(name -> {
			if (!SLICE_FIELDS.contains(name)) {
				names.add(name);
			}
		});
		return names;
	}

	static String updateSchema(final String schema, final FamilyContract contract) throws IOException {
		final JsonNode root = MAPPER.readTree(schema);
		final ObjectNode userPaths = (ObjectNode) root.at("/definitions/user_paths");
		final JsonNode previous = userPaths.get(contract.schemaKey());
		final Set<String> previousSteps = stepNames(previous);
		final Set<String> publishedSteps = stepNames(contract.schema());
		if (!publishedSteps.equals(contract.steps().keySet())) {
			throw new IllegalArgumentException("Contract steps do not match the schema slice keys");
		}
		userPaths.set(contract.schemaKey(), contract.schema());
		updateStepWiring(userPaths.withObject("steps"), contract, previousSteps, publishedSteps);
		String text = replaceObject(schema, "\"" + contract.schemaKey() + "\":", contract.schema());
		text = replaceSteps(text, userPaths.get("steps"), previousSteps);
		return text;
	}

	private static void updateStepWiring(final ObjectNode steps, final FamilyContract contract, final Set<String> previousSteps, final Set<String> publishedSteps) {
		final ArrayNode enumeration = (ArrayNode) steps.at("/items/oneOf/0/enum");
		if (enumeration != null) {
			for (int index = enumeration.size() - 1; index >= 0; index--) {
				if (previousSteps.contains(enumeration.get(index).asText())) {
					enumeration.remove(index);
				}
			}
			for (final String step : publishedSteps) {
				if (isBare(contract.schema().get(step))) {
					enumeration.add(step);
				}
			}
		}
		final ObjectNode properties = (ObjectNode) steps.at("/items/oneOf/1/properties");
		previousSteps.forEach(properties::remove);
		final String prefix = "#/definitions/user_paths/" + contract.schemaKey() + "/";
		for (final String step : publishedSteps) {
			final ObjectNode ref = properties.putObject(step);
			ref.put("$ref", prefix + step);
		}
	}

	/**
	 * A bare step is the step name alone, for example {@code - browser_open_tab}. An action that
	 * requires its parameter list cannot be written that way.
	 */
	private static boolean isBare(final JsonNode action) {
		final JsonNode required = action.get("required");
		if (required != null && required.isArray()) {
			for (final JsonNode item : required) {
				if ("parameters".equals(item.asText())) {
					return false;
				}
			}
		}
		final JsonNode allOf = action.at("/properties/parameters/allOf");
		return !allOf.isArray() || allOf.isEmpty();
	}

	private static String replaceObject(final String text, final String keyMarker, final JsonNode value) {
		final int key = text.indexOf(keyMarker);
		if (key < 0) {
			throw new IllegalArgumentException("Missing schema object " + keyMarker);
		}
		final int open = text.indexOf('{', key + keyMarker.length());
		final int close = JsonTexts.matchingBrace(text, open);
		final String indent = JsonTexts.lineIndent(text, key);
		final String unit = indent.isEmpty() ? "\t" : indent.substring(0, 1);
		final String body = JsonTexts.write(value, unit, indent);
		return text.substring(0, open) + body + text.substring(close + 1);
	}

	private static String replaceSteps(final String text, final JsonNode steps, final Set<String> previousSteps) {
		final int title = text.indexOf("\"title\": \"Steps\"");
		final int key = text.lastIndexOf("\"steps\":", title);
		final int open = text.indexOf('{', key);
		final int close = JsonTexts.matchingBrace(text, open);
		final String indent = JsonTexts.lineIndent(text, key);
		final String unit = indent.isEmpty() ? "\t" : indent.substring(0, 1);
		final String body = preserveForeignSteps(text.substring(open, close + 1), steps, previousSteps, unit, indent);
		return text.substring(0, open) + body + text.substring(close + 1);
	}

	/**
	 * Rewrites the family entries inside the steps object and keeps every other entry's original text,
	 * so a transaction step does not move in the diff.
	 */
	private static String preserveForeignSteps(final String original, final JsonNode updated, final Set<String> previousSteps, final String unit, final String indent) {
		String text = replaceEnum(original, updated.at("/items/oneOf/0/enum"), previousSteps, unit, indent + unit + unit + unit + unit);
		final ObjectNode properties = (ObjectNode) updated.at("/items/oneOf/1/properties");
		final int propertiesKey = text.indexOf("\"properties\":");
		final int open = text.indexOf('{', propertiesKey);
		final int close = JsonTexts.matchingBrace(text, open);
		final String propertiesIndent = JsonTexts.lineIndent(text, propertiesKey);
		final String rebuilt = rebuildProperties(text.substring(open, close + 1), properties, previousSteps, unit, propertiesIndent);
		return text.substring(0, open) + rebuilt + text.substring(close + 1);
	}

	private static String replaceEnum(final String stepsObject, final JsonNode enumeration, final Set<String> previousSteps, final String unit, final String indent) {
		final int key = stepsObject.indexOf("\"enum\":");
		if (key < 0 || enumeration == null || enumeration.isMissingNode()) {
			return stepsObject;
		}
		final int open = stepsObject.indexOf('[', key);
		final int close = JsonTexts.matchingBrace(stepsObject, open);
		final String original = stepsObject.substring(open, close + 1);
		final Set<String> published = new LinkedHashSet<>();
		enumeration.forEach(value -> published.add(value.asText()));
		final List<String> lines = new ArrayList<>();
		final Set<String> kept = new LinkedHashSet<>();
		for (final String line : original.split("\\R")) {
			final String trimmed = line.trim();
			if (!trimmed.startsWith("\"")) {
				continue;
			}
			final String name = trimmed.substring(1, trimmed.indexOf('"', 1));
			if (previousSteps.contains(name) && !published.contains(name)) {
				continue;
			}
			lines.add(withoutTrailingComma(line));
			kept.add(name);
		}
		for (final String name : published) {
			if (!kept.contains(name)) {
				lines.add(indent + unit + "\"" + name + "\"");
			}
		}
		final StringBuilder array = new StringBuilder();
		array.append('[').append('\n');
		for (int index = 0; index < lines.size(); index++) {
			array.append(lines.get(index));
			if (index + 1 < lines.size()) {
				array.append(',');
			}
			array.append('\n');
		}
		array.append(JsonTexts.lineIndent(stepsObject, open)).append(']');
		return stepsObject.substring(0, open) + array + stepsObject.substring(close + 1);
	}

	private static String withoutTrailingComma(final String line) {
		final String trimmed = line.stripTrailing();
		return trimmed.endsWith(",") ? trimmed.substring(0, trimmed.length() - 1) : trimmed;
	}

	private static String rebuildProperties(final String original, final ObjectNode properties, final Set<String> previousSteps, final String unit, final String indent) {
		final List<String> blocks = new ArrayList<>();
		final Set<String> written = new LinkedHashSet<>();
		int insertAt = -1;
		int cursor = original.indexOf('{') + 1;
		final int end = original.lastIndexOf('}');
		while (cursor < end) {
			final int quote = original.indexOf('"', cursor);
			if (quote < 0 || quote > end) {
				break;
			}
			final int nameEnd = original.indexOf('"', quote + 1);
			final String name = original.substring(quote + 1, nameEnd);
			final int valueOpen = original.indexOf('{', nameEnd);
			final int valueClose = JsonTexts.matchingBrace(original, valueOpen);
			int next = valueClose + 1;
			if (next < original.length() && original.charAt(next) == ',') {
				next++;
			}
			if (previousSteps.contains(name)) {
				if (insertAt < 0) {
					insertAt = blocks.size();
				}
			} else {
				final int lineStart = original.lastIndexOf('\n', quote) + 1;
				blocks.add(original.substring(lineStart, valueClose + 1).stripTrailing());
				written.add(name);
			}
			cursor = next;
		}
		final List<String> family = new ArrayList<>();
		properties.fieldNames().forEachRemaining(name -> {
			if (!written.contains(name)) {
				family.add(propertyBlock(name, properties.get(name), unit, indent));
			}
		});
		if (insertAt < 0) {
			insertAt = blocks.size();
		}
		blocks.addAll(insertAt, family);
		final StringBuilder object = new StringBuilder();
		object.append('{').append('\n');
		for (int index = 0; index < blocks.size(); index++) {
			object.append(blocks.get(index));
			if (index + 1 < blocks.size()) {
				object.append(',');
			}
			object.append('\n');
		}
		object.append(indent).append('}');
		return object.toString();
	}

	private static String propertyBlock(final String name, final JsonNode value, final String unit, final String indent) {
		return indent + unit + "\"" + name + "\": " + JsonTexts.write(value, unit, indent + unit);
	}

	static String updateRegistry(final String source, final Set<String> previousSteps, final FamilyContract contract) {
		final Map<String, String> types = new LinkedHashMap<>();
		final Matcher matcher = PUT.matcher(source);
		while (matcher.find()) {
			if (!previousSteps.contains(matcher.group(1))) {
				types.put(matcher.group(1), matcher.group(2));
			}
		}
		types.putAll(contract.steps());
		final Map<String, String> current = new LinkedHashMap<>();
		final Matcher currentMatcher = PUT.matcher(source);
		while (currentMatcher.find()) {
			current.put(currentMatcher.group(1), currentMatcher.group(2));
		}
		if (current.equals(types)) {
			return source;
		}
		final Matcher first = PUT.matcher(source);
		if (!first.find()) {
			throw new IllegalArgumentException("Step registry has no step pairs");
		}
		final int start = first.start();
		int end = first.end();
		final Matcher rest = PUT.matcher(source);
		while (rest.find()) {
			end = rest.end();
		}
		final String indent = JsonTexts.lineIndent(source, start);
		final StringBuilder pairs = new StringBuilder();
		final Iterator<Map.Entry<String, String>> entries = types.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry<String, String> entry = entries.next();
			pairs.append(".put(\"").append(entry.getKey()).append("\", \"").append(entry.getValue()).append("\")");
			if (entries.hasNext()) {
				pairs.append('\n').append(indent);
			}
		}
		return source.substring(0, start) + pairs + source.substring(end);
	}

	static String updatePage(final String page, final FamilyContract contract, final Set<String> previousSteps) throws IOException {
		final Set<String> steps = new LinkedHashSet<>(previousSteps);
		steps.addAll(contract.steps().keySet());
		final String newline = page.contains("\r\n") ? "\r\n" : "\n";
		final String[] lines = page.split("\\R", -1);
		final int firstSection = firstFamilyHeading(lines, steps);
		final StringBuilder preamble = new StringBuilder();
		for (int index = 0; index < firstSection; index++) {
			preamble.append(lines[index]).append(newline);
		}
		final String refreshedPreamble = refreshIndex(preamble.toString(), contract, newline);
		final StringBuilder out = new StringBuilder(refreshedPreamble);
		if (!refreshedPreamble.endsWith(newline)) {
			out.append(newline);
		}
		for (final String step : contract.steps().keySet()) {
			if (!out.toString().endsWith(newline + newline)) {
				out.append(newline);
			}
			out.append(section(step, contract).replace("\n", newline)).append(newline);
		}
		appendForeignSections(out, lines, firstSection, steps, newline);
		return out.toString();
	}

	private static int firstFamilyHeading(final String[] lines, final Set<String> steps) {
		for (int index = 0; index < lines.length; index++) {
			final String heading = headingName(lines[index]);
			if (steps.contains(heading) || isFamilyHeading(lines[index], steps)) {
				return index;
			}
		}
		return lines.length;
	}

	private static boolean isFamilyHeading(final String line, final Set<String> steps) {
		final String name = headingName(line);
		return steps.contains(name);
	}

	private static String headingName(final String line) {
		if (!line.startsWith("## ")) {
			return "";
		}
		return line.substring(3).trim();
	}

	private static void appendForeignSections(final StringBuilder out, final String[] lines, final int from, final Set<String> steps, final String newline) {
		boolean skipping = false;
		for (int index = from; index < lines.length; index++) {
			final String heading = headingName(lines[index]);
			if (!heading.isEmpty()) {
				skipping = steps.contains(heading);
			}
			if (!skipping) {
				out.append(lines[index]).append(newline);
			}
		}
	}

	private static String refreshIndex(final String preamble, final FamilyContract contract, final String newline) {
		final String[] lines = preamble.split("\\R", -1);
		int first = -1;
		int last = -1;
		for (int index = 0; index < lines.length; index++) {
			final Matcher link = Pattern.compile("\\[([a-z0-9_]+)]\\(#\\1\\)").matcher(lines[index]);
			if (link.find() && contract.steps().containsKey(link.group(1))) {
				if (first < 0) {
					first = index;
				}
				last = index;
			}
		}
		if (first < 0) {
			return preamble;
		}
		final StringBuilder out = new StringBuilder();
		for (int index = 0; index < first; index++) {
			out.append(lines[index]).append(newline);
		}
		for (final Map.Entry<String, String> step : contract.steps().entrySet()) {
			out.append("| [").append(step.getKey()).append("](#").append(step.getKey()).append(") | `")
					.append(step.getValue()).append("` |").append(newline);
		}
		for (int index = last + 1; index < lines.length; index++) {
			final Matcher link = Pattern.compile("\\[([a-z0-9_]+)]\\(#\\1\\)").matcher(lines[index]);
			if (!(link.find() && contract.steps().containsKey(link.group(1)))) {
				out.append(lines[index]);
				if (index + 1 < lines.length) {
					out.append(newline);
				}
			}
		}
		return out.toString();
	}

	private static String section(final String step, final FamilyContract contract) {
		final String type = contract.steps().get(step);
		final JsonNode action = contract.schema().get(step);
		final String narrative = narrative(action.path("description").asText(""), type);
		final StringBuilder section = new StringBuilder();
		section.append("## ").append(step).append("\n\n");
		section.append("Type `").append(type).append("`.");
		if (!narrative.isEmpty()) {
			section.append(' ').append(narrative);
		}
		section.append('\n');
		final List<ParameterRow> rows = parameters(contract.schema(), action);
		if (!rows.isEmpty()) {
			section.append("\n| Parameter | Description | Required |\n");
			section.append("|:----------|:------------|:--------:|\n");
			for (final ParameterRow row : rows) {
				section.append("| `").append(row.name).append("` | ").append(row.description.replace("|", "\\|"))
						.append(" | ").append(row.required ? "&#x2713;" : "-").append(" |\n");
			}
		}
		return section.toString().stripTrailing();
	}

	private static String narrative(final String description, final String type) {
		final String marker = "action " + type + ". ";
		final int at = description.indexOf(marker);
		return at < 0 ? description : description.substring(at + marker.length()).trim();
	}

	private static List<ParameterRow> parameters(final JsonNode slice, final JsonNode action) {
		final List<ParameterRow> rows = new ArrayList<>();
		final JsonNode name = action.at("/properties/parameters/items/properties/name");
		if (!name.isObject()) {
			return rows;
		}
		final Set<String> required = requiredNames(slice, action.at("/properties/parameters/allOf"));
		final JsonNode choices = name.get("anyOf");
		if (choices == null) {
			return rows;
		}
		for (final JsonNode choice : choices) {
			rows.addAll(expand(slice, choice, required));
		}
		return rows;
	}

	private static Set<String> requiredNames(final JsonNode slice, final JsonNode allOf) {
		final Set<String> names = new LinkedHashSet<>();
		if (!allOf.isArray()) {
			return names;
		}
		for (final JsonNode entry : allOf) {
			final JsonNode name = entry.at("/contains/properties/name");
			if (name.has("const")) {
				names.add(name.get("const").asText());
			}
			final String ref = name.path("$ref").asText("");
			if (ref.endsWith("/selector_parameter_name")) {
				names.add("selector");
			} else if (ref.contains("/parameter_names/")) {
				final JsonNode target = resolve(slice, ref);
				if (target.has("const")) {
					names.add(target.get("const").asText());
				}
			}
		}
		return names;
	}

	private static List<ParameterRow> expand(final JsonNode slice, final JsonNode choice, final Set<String> required) {
		final List<ParameterRow> rows = new ArrayList<>();
		final String ref = choice.path("$ref").asText("");
		if (ref.contains("/parameter_groups/")) {
			final JsonNode group = resolve(slice, ref);
			for (final JsonNode member : group.path("anyOf")) {
				rows.addAll(expand(slice, member, required));
			}
			return rows;
		}
		if (ref.contains("/parameter_names/")) {
			return expand(slice, resolve(slice, ref), required);
		}
		if (choice.has("const")) {
			final String name = choice.get("const").asText();
			rows.add(new ParameterRow(name, choice.path("description").asText(""), required.contains(name)));
			return rows;
		}
		if (choice.has("pattern")) {
			rows.add(new ParameterRow(displayPattern(choice.get("pattern").asText()), choice.path("description").asText(""), false));
		}
		return rows;
	}

	private static JsonNode resolve(final JsonNode slice, final String ref) {
		final int slash = ref.lastIndexOf('/');
		final String tail = ref.substring(ref.indexOf("/parameter_") + 1);
		JsonNode node = slice;
		for (final String part : tail.split("/")) {
			node = node.path(part);
		}
		if (node.isMissingNode() && slash > 0) {
			return slice.at("/" + ref.substring(ref.indexOf('/', 2) + 1));
		}
		return node;
	}

	private static String displayPattern(final String pattern) {
		final Matcher suffix = Pattern.compile("\\.\\(?([a-z]+)\\)?\\$").matcher(pattern);
		if (pattern.contains("options") && suffix.find()) {
			return "options[N]." + suffix.group(1);
		}
		return pattern;
	}

	private static final class ParameterRow {
		private final String name;
		private final String description;
		private final boolean required;

		private ParameterRow(final String name, final String description, final boolean required) {
			this.name = name;
			this.description = description;
			this.required = required;
		}
	}
}
