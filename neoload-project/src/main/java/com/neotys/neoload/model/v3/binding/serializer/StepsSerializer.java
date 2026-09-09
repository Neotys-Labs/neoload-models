package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsOrInVariableToStringConverter.TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StepsSerializer extends StdSerializer<List<Step>> {
    private static final long serialVersionUID = -4569870233567503685L;

    private static final Map<Class<? extends Step>, String> STEPS;
    static {
    	final ImmutableMap.Builder<Class<? extends Step>, String> builder = new ImmutableMap.Builder<>();
    	builder.put(ImmutableContainer.class, TRANSACTION);
    	builder.put(ImmutableRequest.class, REQUEST);
    	builder.put(ImmutableJavaScript.class, JAVASCRIPT);
    	builder.put(ImmutableIf.class, IF);
    	builder.put(ImmutableLoop.class, LOOP);
    	builder.put(ImmutableWhile.class, WHILE);
    	builder.put(ImmutableSwitch.class, SWITCH);
		builder.put(ImmutableCustomAction.class, CUSTOM_ACTION);
		builder.put(ImmutableTryCatch.class, TRY_CATCH);
		builder.put(ImmutableGoToNextIteration.class, GO_TO_NEXT_ITERATION);
		builder.put(ImmutableFork.class, FORK);
		builder.put(ImmutableVariableModifier.class, VARIABLE_MODIFIER);
		builder.put(ImmutableDebugLogger.class, DEBUG_LOGGER);
    	STEPS = builder.build();
    }

    public StepsSerializer() {
        super(List.class, false);
    }

	@Override
	public void serialize(final List<Step> steps, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartArray();
				
		for (final Step step : steps) {
			if (step instanceof DelayConstant) {
				final DelayConstant delay = (DelayConstant) step;
				generator.writeStartObject();
				// Simplified scalar syntax when there is no name/description; expanded object otherwise.
				if (Delay.DEFAULT_NAME.equals(delay.getName()) && delay.getDescription().isEmpty()) {
					generator.writeStringField(DELAY, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(delay.getValue()));
				} else {
					generator.writeObjectFieldStart(DELAY);
					if (!Delay.DEFAULT_NAME.equals(delay.getName())) {
						generator.writeStringField("name", delay.getName());
					}
					if (delay.getDescription().isPresent()) {
						generator.writeStringField("description", delay.getDescription().get());
					}
					generator.writeStringField("value", TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(delay.getValue()));
					generator.writeEndObject();
				}
				generator.writeEndObject();
			}
			else if (step instanceof DelayRandom) {
				final DelayRandom delay = (DelayRandom) step;
				generator.writeStartObject();
				generator.writeObjectFieldStart(DELAY);
				if (!Delay.DEFAULT_NAME.equals(delay.getName())) {
					generator.writeStringField("name", delay.getName());
				}
				if (delay.getDescription().isPresent()) {
					generator.writeStringField("description", delay.getDescription().get());
				}
				if (!DelayRandom.DEFAULT_MIN.equals(delay.getMin())) {
					generator.writeStringField(DelayRandom.MIN, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(delay.getMin()));
				}
				generator.writeStringField(DelayRandom.MAX, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(delay.getMax()));
				generator.writeEndObject();
				generator.writeEndObject();
			}
			else if (step instanceof ThinkTimeConstant) {
				final ThinkTimeConstant thinkTime = (ThinkTimeConstant) step;
				generator.writeStartObject();
				// Simplified scalar syntax when there is no name/description; expanded object otherwise.
				if (ThinkTime.DEFAULT_NAME.equals(thinkTime.getName()) && thinkTime.getDescription().isEmpty()) {
					generator.writeStringField(THINK_TIME, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(thinkTime.getValue()));
				} else {
					generator.writeObjectFieldStart(THINK_TIME);
					if (!ThinkTime.DEFAULT_NAME.equals(thinkTime.getName())) {
						generator.writeStringField("name", thinkTime.getName());
					}
					if (thinkTime.getDescription().isPresent()) {
						generator.writeStringField("description", thinkTime.getDescription().get());
					}
					generator.writeStringField("value", TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(thinkTime.getValue()));
					generator.writeEndObject();
				}
				generator.writeEndObject();
			}
			else if (step instanceof ThinkTimeRandom) {
				final ThinkTimeRandom thinkTime = (ThinkTimeRandom) step;
				generator.writeStartObject();
				generator.writeObjectFieldStart(THINK_TIME);
				if (!ThinkTime.DEFAULT_NAME.equals(thinkTime.getName())) {
					generator.writeStringField("name", thinkTime.getName());
				}
				if (thinkTime.getDescription().isPresent()) {
					generator.writeStringField("description", thinkTime.getDescription().get());
				}
				if (!ThinkTimeRandom.DEFAULT_MIN.equals(thinkTime.getMin())) {
					generator.writeStringField(ThinkTimeRandom.MIN, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(thinkTime.getMin()));
				}
				generator.writeStringField(ThinkTimeRandom.MAX, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(thinkTime.getMax()));
				generator.writeEndObject();
				generator.writeEndObject();
			}
			// Since GoToNextIteration has no properties, we chose to serialize as a bare scalar string
			else if (step instanceof GoToNextIteration) {
				generator.writeString(GO_TO_NEXT_ITERATION);
			}
			// A StopVU keeping the default start_new_vu is serialized as a bare scalar string
			else if (step instanceof StopVU) {
				final StopVU stopVU = (StopVU) step;
				if (stopVU.getStartNewVU()) {
					generator.writeString(STOP_VU);
				}
				else {
					generator.writeStartObject();
					generator.writeObjectFieldStart(STOP_VU);
					generator.writeBooleanField(StopVU.START_NEW_VU, stopVU.getStartNewVU());
					generator.writeEndObject();
					generator.writeEndObject();
				}
			}
			else {
				final String stepName = STEPS.get(step.getClass());
				if (stepName != null) {
					generator.writeStartObject();
					generator.writeObjectField(stepName, step);
					generator.writeEndObject();
				} 
			}
		}
		
		generator.writeEndArray();
    }
}
