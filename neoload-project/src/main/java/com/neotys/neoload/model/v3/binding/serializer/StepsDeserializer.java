package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.userpath.*;

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
    	builder.put(STOP_VU, StopVU.class);
    	builder.put(FORK, Fork.class);
    	builder.put(WAIT_UNTIL, WaitUntil.class);
    	builder.put(VARIABLE_MODIFIER, VariableModifier.class);
    	builder.put(DEBUG_LOGGER, DebugLogger.class);
    	builder.put(RENDEZVOUS, Rendezvous.class);
    	builder.put(TRY_CATCH, TryCatch.class);
    	builder.put(HTTP_PAGE, HttpPage.class);
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
           
            Step step = null;
            if (stepNode.isTextual() && GO_TO_NEXT_ITERATION.equals(stepNode.asText())) {
                step = GoToNextIteration.builder().build();
            } else if (stepNode.has(GO_TO_NEXT_ITERATION)) {
                step = GoToNextIteration.builder().build();
            } else if (stepNode.has(DELAY)) {
                final String delayValue = stepNode.get(DELAY).asText();
                final String delay = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(delayValue);
                step = Delay.builder().value(String.valueOf(delay)).build();
            } else if (stepNode.has(THINK_TIME)) {
                final String thinkTimeValue = stepNode.get(THINK_TIME).asText();
                final String thinkTime = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(thinkTimeValue);
                step = ThinkTime.builder().value(String.valueOf(thinkTime)).build();
            } else {
            	final String stepName = stepNode.fieldNames().next();
            	final Class<? extends Step> stepClass = STEPS.get(stepName);
            	if (stepClass != null) {
            		final JsonNode stepValue = stepNode.get(stepName);
            		step = codec.treeToValue(stepValue, stepClass);
            	}
            }

            if (step != null) {
                steps.add(step);
            }
        }

        return steps;
    }
}
