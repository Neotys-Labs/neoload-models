package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;
import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeConstant.VALUE;
import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom.DEFAULT_MIN;
import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom.MAX;
import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom.MIN;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTime;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeConstant;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom;
import java.io.IOException;

public final class WebPageThinkTimeDeserializer extends StdDeserializer<WebPageThinkTime> {
	private static final long serialVersionUID = -3187534229401672430L;

	public WebPageThinkTimeDeserializer() {
		super(WebPageThinkTime.class);
	}

	@Override
	public WebPageThinkTime deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		final boolean hasValue = node.has(VALUE);
		final boolean hasMin = node.has(MIN);
		final boolean hasMax = node.has(MAX);

		if (hasValue && (hasMin || hasMax)) {
			return ctx.reportInputMismatch(WebPageThinkTime.class,
					"Incorrect value for 'think_time': 'value' cannot be combined with 'min' or 'max'.");
		}
		if (hasValue) {
			return WebPageThinkTimeConstant.builder().value(asText(node, VALUE)).build();
		}
		if (hasMax) {
			return WebPageThinkTimeRandom.builder()
					.min(hasMin ? asText(node, MIN) : DEFAULT_MIN)
					.max(asText(node, MAX))
					.build();
		}
		return ctx.reportInputMismatch(WebPageThinkTime.class,
				"Incorrect value for 'think_time': must specify either 'value', or 'max' (with an optional 'min').");
	}
}
