package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.InvalidDurationException;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads a {@link Duration} from three syntaxes: a scalar, an object with {@code value}, or an object
 * with {@code min} / {@code max}. Durations are normalized to milliseconds, a plain number meaning
 * milliseconds. Text that cannot be parsed is kept as-is so that validation rejects it.
 */
public final class DurationDeserializer extends StdDeserializer<Duration> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DurationDeserializer.class);

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
			return readConstant(DeserializerHelper.asText(node));
		}
		final Optional<String> value = DeserializerHelper.asText(node, DurationConstant.VALUE);
		final Optional<String> min = DeserializerHelper.asText(node, DurationRandom.MIN);
		final Optional<String> max = DeserializerHelper.asText(node, DurationRandom.MAX);
		if (value.isPresent() && (min.isPresent() || max.isPresent())) {
			throw JsonMappingException.from((JsonParser) null, "Incorrect duration: 'value' cannot be combined with 'min' or 'max'.");
		}
		if (min.isPresent() || max.isPresent()) {
			return readRandom(min, max);
		}
		return readConstant(value);
	}

	private static Duration readRandom(final Optional<String> min, final Optional<String> max) {
		final DurationRandom.Builder builder = DurationRandom.builder();
		min.map(DurationDeserializer::convertDuration).ifPresent(builder::min);
		max.map(DurationDeserializer::convertDuration).ifPresent(builder::max);
		return builder.build();
	}

	private static Duration readConstant(final Optional<String> value) {
		final DurationConstant.Builder builder = DurationConstant.builder();
		value.map(DurationDeserializer::convertDuration).ifPresent(builder::value);
		return builder.build();
	}

	/**
	 * Text the converter cannot parse is kept as-is, so that validation reports it rather than reading it as zero.
	 */
	private static String convertDuration(final String text) {
		try {
			return STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(text);
		} catch (final InvalidDurationException e) {
			LOGGER.debug("Duration kept as-is for validation", e);
			return text;
		}
	}
}
