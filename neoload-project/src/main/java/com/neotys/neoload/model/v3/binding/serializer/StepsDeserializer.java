package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class StepsDeserializer extends StdDeserializer<List<Step>> {
	private static final long serialVersionUID = -5696608939252369276L;

	private static final Map<String, Class<? extends Step>> STEPS;
	static {
		final ImmutableMap.Builder<String, Class<? extends Step>> builder = new ImmutableMap.Builder<>();
		builder.put(TRANSACTION, Container.class);
		builder.put(REQUEST, Request.class);
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
		builder.put(WEBSOCKET_CHANNEL, WebSocketChannel.class);
		STEPS = builder.build();
	}

	private static final List<Function<JsonNode, Step>> SIMPLE_STEP_PARSERS = Arrays.asList(
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

	private Step parseSimpleStep(final JsonNode stepNode) {
		return SIMPLE_STEP_PARSERS.stream()
				.map(parser -> parser.apply(stepNode))
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}

	private static Step parseGoToNextIteration(final JsonNode stepNode) {
		if ((stepNode.isTextual() && GO_TO_NEXT_ITERATION.equals(stepNode.asText())) || stepNode.has(GO_TO_NEXT_ITERATION)) {
			return GoToNextIteration.builder().build();
		}
		return null;
	}

	// delay is polymorphic: a scalar is a DelayConstant; an object with min/max is a DelayRandom,
	// otherwise a DelayConstant (with its value under the `value` key). Both object forms may carry
	// an optional name/description.
	private static Step parseDelay(final JsonNode stepNode) {
		if (!stepNode.has(DELAY)) {
			return null;
		}
		final JsonNode delayNode = stepNode.get(DELAY);
		if (!delayNode.isObject()) {
			return DelayConstant.builder().value(convertDuration(delayNode)).build();
		}
		if (delayNode.has(DelayRandom.MIN) || delayNode.has(DelayRandom.MAX)) {
			final DelayRandom.Builder builder = DelayRandom.builder();
			applyNameDescription(delayNode, builder::name, builder::description);
			if (delayNode.has(DelayRandom.MIN)) {
				builder.min(convertDuration(delayNode.get(DelayRandom.MIN)));
			}
			if (delayNode.has(DelayRandom.MAX)) {
				builder.max(convertDuration(delayNode.get(DelayRandom.MAX)));
			}
			return builder.build();
		}
		final DelayConstant.Builder builder = DelayConstant.builder();
		applyNameDescription(delayNode, builder::name, builder::description);
		if (delayNode.has(DelayConstant.VALUE)) {
			builder.value(convertDuration(delayNode.get(DelayConstant.VALUE)));
		}
		return builder.build();
	}

	// think_time is polymorphic: a scalar is a ThinkTimeConstant; an object with min/max is a
	// ThinkTimeRandom, otherwise a ThinkTimeConstant (with its value under the `value` key). Both
	// object forms may carry an optional name/description.
	private static Step parseThinkTime(final JsonNode stepNode) {
		if (!stepNode.has(THINK_TIME)) {
			return null;
		}
		final JsonNode thinkTimeNode = stepNode.get(THINK_TIME);
		if (!thinkTimeNode.isObject()) {
			return ThinkTimeConstant.builder().value(convertDuration(thinkTimeNode)).build();
		}
		if (thinkTimeNode.has(ThinkTimeRandom.MIN) || thinkTimeNode.has(ThinkTimeRandom.MAX)) {
			final ThinkTimeRandom.Builder builder = ThinkTimeRandom.builder();
			applyNameDescription(thinkTimeNode, builder::name, builder::description);
			if (thinkTimeNode.has(ThinkTimeRandom.MIN)) {
				builder.min(convertDuration(thinkTimeNode.get(ThinkTimeRandom.MIN)));
			}
			if (thinkTimeNode.has(ThinkTimeRandom.MAX)) {
				builder.max(convertDuration(thinkTimeNode.get(ThinkTimeRandom.MAX)));
			}
			return builder.build();
		}
		final ThinkTimeConstant.Builder builder = ThinkTimeConstant.builder();
		applyNameDescription(thinkTimeNode, builder::name, builder::description);
		if (thinkTimeNode.has(ThinkTimeConstant.VALUE)) {
			builder.value(convertDuration(thinkTimeNode.get(ThinkTimeConstant.VALUE)));
		}
		return builder.build();
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

	private static String convertDuration(final JsonNode node) {
		return STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.asText());
	}

	private static void applyNameDescription(final JsonNode node, final Consumer<String> nameSetter, final Consumer<String> descriptionSetter) {
		if (node.has(Element.NAME)) {
			nameSetter.accept(node.get(Element.NAME).asText());
		}
		if (node.has(Element.DESCRIPTION)) {
			descriptionSetter.accept(node.get(Element.DESCRIPTION).asText());
		}
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
