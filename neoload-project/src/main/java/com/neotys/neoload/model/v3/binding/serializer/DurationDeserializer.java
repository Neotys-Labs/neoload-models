package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import java.io.IOException;

/**
 * Reads a {@link Duration} from three syntaxes: a scalar, an object with {@code value}, or an object
 * with {@code min} / {@code max}. Each value is read by {@link DurationOrVariableDeserializer}, a plain number
 * meaning milliseconds. A value that is neither a variable nor a valid duration fails the read with an
 * {@link com.neotys.neoload.model.v3.binding.converter.InvalidDurationException}.
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
			return DurationConstant.builder().value(toValue(node)).build();
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
			builder.min(toValue(node.get(DurationRandom.MIN)));
		}
		if (node.has(DurationRandom.MAX)) {
			builder.max(toValue(node.get(DurationRandom.MAX)));
		}
		return builder.build();
	}

	private static Duration readConstant(final JsonNode node) {
		final DurationConstant.Builder builder = DurationConstant.builder();
		if (node.has(DurationConstant.VALUE)) {
			builder.value(toValue(node.get(DurationConstant.VALUE)));
		}
		return builder.build();
	}

	private static DurationOrVariable toValue(final JsonNode node) {
		return DurationOrVariableDeserializer.parse(node.asText());
	}
}
