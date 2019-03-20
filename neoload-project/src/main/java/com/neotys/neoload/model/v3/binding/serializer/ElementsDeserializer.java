package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationWithMsConverter.STRING_TO_TIME_DURATION_WITH_MS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.*;

public class ElementsDeserializer extends StdDeserializer<List<Step>> {
	private static final long serialVersionUID = -5696608939252369276L;

	private static final String TRANSACTION = "transaction";
	private static final String REQUEST = "request";
	private static final String DELAY = "delay";
	private static final String THINK_TIME = "think_time";
	private static final String JAVASCRIPT = "javascript";

	public ElementsDeserializer() {
		super(List.class);
	}

	@Override
	public List<Step> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
		final List<Step> actions = new ArrayList<>();

		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		final Iterator<JsonNode> iterator = jsonNode.elements();
		while (iterator.hasNext()) {
			final JsonNode actionNode = iterator.next();

			Step action = null;
			if (actionNode.has(TRANSACTION)) {
				final JsonNode transactionNode = actionNode.get(TRANSACTION);
				action = codec.treeToValue(transactionNode, Container.class);
			} else if (actionNode.has(REQUEST)) {
				final JsonNode requestNode = actionNode.get(REQUEST);
				action = codec.treeToValue(requestNode, Request.class);
			} else if (actionNode.has(DELAY)) {
				final String delayValue = actionNode.get(DELAY).asText();
				final String delay = STRING_TO_TIME_DURATION_WITH_MS.convert(delayValue);
				action = Delay.builder().value(String.valueOf(delay)).build();
			} else if (actionNode.has(THINK_TIME)) {
				final String thinkTimeValue = actionNode.get(THINK_TIME).asText();
				final String thinkTime = STRING_TO_TIME_DURATION_WITH_MS.convert(thinkTimeValue);
				action = ThinkTime.builder().value(String.valueOf(thinkTime)).build();
			} else if (actionNode.has(JAVASCRIPT)) {
				final JsonNode javascriptNode = actionNode.get(JAVASCRIPT);
				action = codec.treeToValue(javascriptNode, Javascript.class);
			}

			if (action != null) {
				actions.add(action);
			}
		}

		return actions;
	}
}
