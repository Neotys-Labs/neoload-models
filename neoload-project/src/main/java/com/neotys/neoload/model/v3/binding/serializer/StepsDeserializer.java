package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.binding.converter.InvalidDurationException;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StepsDeserializer extends StdDeserializer<List<Step>> {
	private static final long serialVersionUID = -5696608939252369276L;

	private static final Map<String, Class<? extends Step>> STEPS;
	static {
		final ImmutableMap.Builder<String, Class<? extends Step>> builder = new ImmutableMap.Builder<>();
		builder.put(TRANSACTION, Container.class);
		builder.put(REQUEST, Request.class);
		builder.put(SOAP_REQUEST, SoapRequest.class);
		builder.put(JAVASCRIPT, JavaScript.class);
		builder.put(IF, If.class);
		builder.put(LOOP, Loop.class);
		builder.put(WHILE, While.class);
		builder.put(SWITCH, Switch.class);
		builder.put(CUSTOM_ACTION, CustomAction.class);
		builder.put(TRY_CATCH, TryCatch.class);
		builder.put(FORK, Fork.class);
		builder.put(VARIABLE_MODIFIER, VariableModifier.class);
		builder.put(RENDEZVOUS, Rendezvous.class);
		builder.put(DEBUG_LOGGER, DebugLogger.class);
		builder.put(STOP_VU, StopVU.class);
		builder.put(WEB_PAGE, WebPage.class);
		builder.put(WAIT_UNTIL, WaitUntil.class);
		STEPS = builder.build();
	}

	private static final List<StepParser> SIMPLE_STEP_PARSERS = Arrays.asList(
			StepsDeserializer::parseGoToNextIteration,
			StepsDeserializer::parseDelay,
			StepsDeserializer::parseThinkTime,
			StepsDeserializer::parseRendezvous,
			StepsDeserializer::parseStopVU,
			StepsDeserializer::parseSharedElementRef
	);

	public StepsDeserializer() {
		super(List.class);
	}

	@Override
	public List<Step> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
		final List<Step> steps = new ArrayList<>();

		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		final Iterator<JsonNode> iterator = jsonNode.elements();
		while (iterator.hasNext()) {
			final JsonNode stepNode = iterator.next();
			final Step step = parseStep(codec, stepNode);
			if (step != null) {
				steps.add(step);
			}
		}

		return steps;
	}

	private Step parseStep(final ObjectCodec codec, final JsonNode stepNode) throws IOException {
		final Step simpleStep = parseSimpleStep(stepNode);
		if (simpleStep != null) {
			return simpleStep;
		}
		return parseRegisteredStep(codec, stepNode);
	}

	private Step parseSimpleStep(final JsonNode stepNode) throws IOException {
		for (final StepParser parser : SIMPLE_STEP_PARSERS) {
			final Step step = parser.parse(stepNode);
			if (step != null) {
				return step;
			}
		}
		return null;
	}

	@FunctionalInterface
	private interface StepParser {
		Step parse(JsonNode stepNode) throws IOException;
	}

	private static Step parseGoToNextIteration(final JsonNode stepNode) {
		if ((stepNode.isTextual() && GO_TO_NEXT_ITERATION.equals(stepNode.asText())) || stepNode.has(GO_TO_NEXT_ITERATION)) {
			return GoToNextIteration.builder().build();
		}
		return null;
	}

	private static Step parseDelay(final JsonNode stepNode) throws IOException {
		return parseDelay(stepNode, DELAY, false);
	}

	private static Step parseThinkTime(final JsonNode stepNode) throws IOException {
		return parseDelay(stepNode, THINK_TIME, true);
	}

	private static Step parseDelay(final JsonNode stepNode, final String key, final boolean thinkTime) throws IOException {
		if (!stepNode.has(key)) {
			return null;
		}
		final JsonNode delayNode = stepNode.get(key);
		final Delay.Builder builder = Delay.builder().isThinkTime(thinkTime).duration(readDuration(delayNode, key));
		if (delayNode.has(Element.NAME)) {
			builder.name(delayNode.get(Element.NAME).asText());
		}
		if (delayNode.has(Element.DESCRIPTION)) {
			builder.description(delayNode.get(Element.DESCRIPTION).asText());
		}
		return builder.build();
	}

	private static Duration readDuration(final JsonNode delayNode, final String key) throws JsonMappingException {
		try {
			return DurationDeserializer.read(delayNode);
		} catch (final InvalidDurationException e) {
			final JsonMappingException exception = JsonMappingException.from((JsonParser) null, e.getMessage(), e);
			exception.prependPath(Delay.class, key);
			throw exception;
		}
	}

	private static Step parseRendezvous(final JsonNode stepNode) {
		if (stepNode.isTextual() && RENDEZVOUS.equals(stepNode.asText())) {
			return Rendezvous.builder().build();
		}
		return null;
	}

	private static Step parseStopVU(final JsonNode stepNode) {
		if (stepNode.isTextual() && STOP_VU.equals(stepNode.asText())) {
			return StopVU.builder().build();
		}
		return null;
	}

	private static Step parseSharedElementRef(final JsonNode stepNode) {
		if (stepNode.has(SHARED_ELEMENT)) {
			return SharedElementRef.builder().name(stepNode.get(SHARED_ELEMENT).asText()).build();
		}
		return null;
	}

	private Step parseRegisteredStep(final ObjectCodec codec, final JsonNode stepNode) throws IOException {
		final String stepName = stepNode.fieldNames().next();
		final Class<? extends Step> stepClass = STEPS.get(stepName);
		if (stepClass == null) {
			return null;
		}
		final JsonNode stepValue = stepNode.get(stepName);
		return codec.treeToValue(stepValue, stepClass);
	}
}
