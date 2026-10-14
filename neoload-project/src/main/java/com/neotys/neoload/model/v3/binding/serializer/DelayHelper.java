package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsOrInVariableToStringConverter.TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.project.userpath.DelayRandom;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Reads and writes the YAML/JSON form of a {@link Delay}, shared by the {@code delay} and
 * {@code think_time} steps and by the web page {@code think_time} field.
 *
 * <p>Three syntaxes are accepted: a scalar duration, an object with {@code value}, or an object
 * with {@code min} / {@code max}. Durations are normalized to milliseconds on read and formatted
 * back on write. A {@code named} context additionally accepts a {@code name} / {@code description};
 * the web page context is not named.
 */
final class DelayHelper {
	private static final String ZERO = "0";
	private static final Pattern DURATION_LITERAL = Pattern.compile("(\\d+(h|ms|m|s)?\\s*)+");

	private DelayHelper() {
		super();
	}

	static Delay read(final JsonNode node, final boolean thinkTime, final boolean named) throws JsonMappingException {
		if (!node.isObject()) {
			return DelayConstant.builder().isThinkTime(thinkTime).value(convertDuration(node)).build();
		}
		checkObject(node, named);
		if (node.has(DelayRandom.MIN) || node.has(DelayRandom.MAX)) {
			return readRandom(node, thinkTime);
		}
		return readConstant(node, thinkTime);
	}

	private static void checkObject(final JsonNode node, final boolean named) throws JsonMappingException {
		if (node.has(DelayConstant.VALUE) && (node.has(DelayRandom.MIN) || node.has(DelayRandom.MAX))) {
			throw JsonMappingException.from((JsonParser) null,
					"Incorrect value for 'think_time' or 'delay': 'value' cannot be combined with 'min' or 'max'.");
		}
		if (!named && (node.has(Element.NAME) || node.has(Element.DESCRIPTION))) {
			throw JsonMappingException.from((JsonParser) null,
					"Incorrect value for 'think_time': 'name' and 'description' are not allowed.");
		}
	}

	private static Delay readRandom(final JsonNode node, final boolean thinkTime) {
		final DelayRandom.Builder builder = DelayRandom.builder().isThinkTime(thinkTime);
		applyNameDescription(node, builder::name, builder::description);
		if (node.has(DelayRandom.MIN)) {
			builder.min(convertDuration(node.get(DelayRandom.MIN)));
		}
		if (node.has(DelayRandom.MAX)) {
			builder.max(convertDuration(node.get(DelayRandom.MAX)));
		}
		return builder.build();
	}

	private static Delay readConstant(final JsonNode node, final boolean thinkTime) {
		final DelayConstant.Builder builder = DelayConstant.builder().isThinkTime(thinkTime);
		applyNameDescription(node, builder::name, builder::description);
		if (node.has(DelayConstant.VALUE)) {
			builder.value(convertDuration(node.get(DelayConstant.VALUE)));
		}
		return builder.build();
	}

	/**
	 * Writes the value of a delay field: a scalar when it is a constant with no name/description
	 * (or in an unnamed context), an object otherwise. {@code min} is written only when present.
	 */
	static void write(final JsonGenerator generator, final Delay delay, final boolean named) throws IOException {
		if (delay instanceof DelayConstant) {
			writeConstant(generator, (DelayConstant) delay, named);
		} else if (delay instanceof DelayRandom) {
			writeRandom(generator, (DelayRandom) delay, named);
		}
	}

	private static void writeConstant(final JsonGenerator generator, final DelayConstant delay, final boolean named) throws IOException {
		if (!named || isImplicit(delay)) {
			generator.writeString(toDuration(delay.getValue()));
			return;
		}
		generator.writeStartObject();
		writeNameAndDescription(generator, delay);
		generator.writeStringField(DelayConstant.VALUE, toDuration(delay.getValue()));
		generator.writeEndObject();
	}

	private static void writeRandom(final JsonGenerator generator, final DelayRandom delay, final boolean named) throws IOException {
		generator.writeStartObject();
		if (named) {
			writeNameAndDescription(generator, delay);
		}
		if (delay.getMin().isPresent()) {
			generator.writeStringField(DelayRandom.MIN, toDuration(delay.getMin().get()));
		}
		generator.writeStringField(DelayRandom.MAX, toDuration(delay.getMax()));
		generator.writeEndObject();
	}

	private static boolean isImplicit(final Delay delay) {
		return delay.defaultName().equals(delay.getName()) && delay.getDescription().isEmpty();
	}

	private static void writeNameAndDescription(final JsonGenerator generator, final Delay delay) throws IOException {
		if (!delay.defaultName().equals(delay.getName())) {
			generator.writeStringField(Element.NAME, delay.getName());
		}
		final String description = delay.getDescription().orElse(null);
		if (description != null && !description.trim().isEmpty()) {
			generator.writeStringField(Element.DESCRIPTION, description.trim());
		}
	}

	private static String toDuration(final String valueInMs) {
		return TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(valueInMs);
	}

	/**
	 * Normalizes a duration to milliseconds. The converter maps anything it cannot parse to {@code 0};
	 * such text is kept as-is instead, so that validation reports it rather than reading it as zero.
	 */
	private static String convertDuration(final JsonNode node) {
		final String text = node.asText();
		final String milliseconds = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(text);
		return ZERO.equals(milliseconds) && !DURATION_LITERAL.matcher(text).matches() ? text : milliseconds;
	}

	private static void applyNameDescription(final JsonNode node, final Consumer<String> nameSetter, final Consumer<String> descriptionSetter) {
		if (node.has(Element.NAME)) {
			nameSetter.accept(node.get(Element.NAME).asText());
		}
		if (node.has(Element.DESCRIPTION)) {
			descriptionSetter.accept(node.get(Element.DESCRIPTION).asText());
		}
	}
}
