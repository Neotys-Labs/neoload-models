package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import java.io.IOException;

/**
 * Reads a {@link Duration} from three syntaxes: a scalar, an object with {@code value}, or an object
 * with {@code min} / {@code max}. Durations are normalized to milliseconds, a plain number meaning
 * milliseconds. Text that cannot be parsed is kept as-is so that validation rejects it.
 */
public final class DurationDeserializer extends StdDeserializer<Duration> {
	private static final long serialVersionUID = 1L;

	public DurationDeserializer() {
		super(Duration.class);
	}

	@Override
	public Duration deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);
		if (node.has(Element.NAME) || node.has(Element.DESCRIPTION)) {
			throw JsonMappingException.from(parser, "Incorrect duration: 'name' and 'description' are not allowed.");
		}
		return read(node);
	}

	static Duration read(final JsonNode node) throws JsonMappingException {
		if (!node.isObject()) {
			return DurationConstant.builder().valueInMsOrVariable(convertDuration(node)).build();
		}
		if (node.has(DurationConstant.VALUE) && (node.has(DurationRandom.MIN) || node.has(DurationRandom.MAX))) {
			throw JsonMappingException.from((JsonParser) null, "Incorrect duration: 'value' cannot be combined with 'min' or 'max'.");
		}
		if (node.has(DurationRandom.MIN) || node.has(DurationRandom.MAX)) {
			return readRandom(node);
		}
		return readConstant(node);
	}

	private static Duration readRandom(final JsonNode node) {
		final DurationRandom.Builder builder = DurationRandom.builder();
		if (node.has(DurationRandom.MIN)) {
			builder.minInMsOrVariable(convertDuration(node.get(DurationRandom.MIN)));
		}
		if (node.has(DurationRandom.MAX)) {
			builder.maxInMsOrVariable(convertDuration(node.get(DurationRandom.MAX)));
		}
		return builder.build();
	}

	private static Duration readConstant(final JsonNode node) {
		final DurationConstant.Builder builder = DurationConstant.builder();
		if (node.has(DurationConstant.VALUE)) {
			builder.valueInMsOrVariable(convertDuration(node.get(DurationConstant.VALUE)));
		}
		return builder.build();
	}

	/**
	 * Text the converter cannot parse is kept as-is, so that validation reports it rather than reading it as zero.
	 */
	private static String convertDuration(final JsonNode node) {
		return STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.asText());
	}
}
