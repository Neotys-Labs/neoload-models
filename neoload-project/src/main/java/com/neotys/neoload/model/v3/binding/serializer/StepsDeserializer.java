package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

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
import java.util.function.Consumer;

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
			builder.put(DEBUG_LOGGER, DebugLogger.class);
			builder.put(STOP_VU, StopVU.class);
    	STEPS = builder.build();
    }
    
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
        if (stepNode.isTextual() && GO_TO_NEXT_ITERATION.equals(stepNode.asText())) {
            return GoToNextIteration.builder().build();
        } else if (stepNode.has(GO_TO_NEXT_ITERATION)) {
            return GoToNextIteration.builder().build();
        } else if (stepNode.has(DELAY)) {
            return parseDelay(stepNode.get(DELAY));
        } else if (stepNode.has(THINK_TIME)) {
            return parseThinkTime(stepNode.get(THINK_TIME));
		} else if (stepNode.isTextual() && STOP_VU.equals(stepNode.asText())) {
            return StopVU.builder().build();
        }
        return null;
    }

    // delay is polymorphic: a scalar is a DelayConstant; an object with min/max is a DelayRandom,
    // otherwise a DelayConstant (with its value under the `value` key). Both object forms may carry
    // an optional name/description.
    private Step parseDelay(final JsonNode node) {
        if (node.isObject()) {
            if (node.has(DelayRandom.MIN) || node.has(DelayRandom.MAX)) {
                final DelayRandom.Builder builder = DelayRandom.builder();
                applyNameDescription(node, builder::name, builder::description);
                if (node.has(DelayRandom.MIN)) {
                    builder.min(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.get(DelayRandom.MIN).asText()));
                }
                if (node.has(DelayRandom.MAX)) {
                    builder.max(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.get(DelayRandom.MAX).asText()));
                }
                return builder.build();
            }
            final DelayConstant.Builder builder = DelayConstant.builder();
            applyNameDescription(node, builder::name, builder::description);
            if (node.has("value")) {
                builder.value(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.get("value").asText()));
            }
            return builder.build();
        }
        return DelayConstant.builder()
                .value(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.asText()))
                .build();
    }

    // think_time is polymorphic: a scalar is a ThinkTimeConstant; an object with min/max is a
    // ThinkTimeRandom, otherwise a ThinkTimeConstant (with its value under the `value` key). Both
    // object forms may carry an optional name/description.
    private Step parseThinkTime(final JsonNode node) {
        if (node.isObject()) {
            if (node.has(ThinkTimeRandom.MIN) || node.has(ThinkTimeRandom.MAX)) {
                final ThinkTimeRandom.Builder builder = ThinkTimeRandom.builder();
                applyNameDescription(node, builder::name, builder::description);
                if (node.has(ThinkTimeRandom.MIN)) {
                    builder.min(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.get(ThinkTimeRandom.MIN).asText()));
                }
                if (node.has(ThinkTimeRandom.MAX)) {
                    builder.max(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.get(ThinkTimeRandom.MAX).asText()));
                }
                return builder.build();
            }
            final ThinkTimeConstant.Builder builder = ThinkTimeConstant.builder();
            applyNameDescription(node, builder::name, builder::description);
            if (node.has("value")) {
                builder.value(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.get("value").asText()));
            }
            return builder.build();
        }
        return ThinkTimeConstant.builder()
                .value(STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(node.asText()))
                .build();
    }

    private void applyNameDescription(final JsonNode node, final Consumer<String> nameSetter, final Consumer<String> descriptionSetter) {
        if (node.has("name")) {
            nameSetter.accept(node.get("name").asText());
        }
        if (node.has("description")) {
            descriptionSetter.accept(node.get("description").asText());
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
