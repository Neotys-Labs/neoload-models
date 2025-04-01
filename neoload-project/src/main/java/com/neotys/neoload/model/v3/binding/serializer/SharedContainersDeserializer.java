package com.neotys.neoload.model.v3.binding.serializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SharedContainersDeserializer extends StdDeserializer<List<Step>> {

	private static final Map<String, Class<? extends Step>> STEPS = new ImmutableMap.Builder<String, Class<? extends Step>>()
		.put(StepsConstants.TRANSACTION, Container.class)
		.put(StepsConstants.LOOP, Loop.class)
		.put(StepsConstants.WHILE, While.class)
		.build();

	protected SharedContainersDeserializer() {
		super(List.class);
	}

	@Override
	public List<Step> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
		final List<Step> steps = new ArrayList<>();

		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		final Iterator<JsonNode> iterator = jsonNode.elements();
		while (iterator.hasNext()) {
			final JsonNode stepNode = iterator.next();

			final String stepName = stepNode.fieldNames().next();
			final Class<? extends Step> stepClass = STEPS.get(stepName);
			if (stepClass != null) {
				final JsonNode stepValue = stepNode.get(stepName);
				steps.add(codec.treeToValue(stepValue, stepClass));
			} else {
				throw new IllegalArgumentException("Not valid step type: " + stepName+" for shared container");
			}

		}

		return steps;
	}
}
