package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsOrInVariableToStringConverter.TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
				writeDelayConstant(generator, (DelayConstant) step);
			}
			else if (step instanceof DelayRandom) {
				writeDelayRandom(generator, (DelayRandom) step);
			}
			else if (step instanceof ThinkTimeConstant) {
				writeThinkTimeConstant(generator, (ThinkTimeConstant) step);
			}
			else if (step instanceof ThinkTimeRandom) {
				writeThinkTimeRandom(generator, (ThinkTimeRandom) step);
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

	// A constant duration uses the simplified scalar syntax when it has no name/description,
	// and the expanded object syntax (value under the `value` key) otherwise.
	private void writeDelayConstant(final JsonGenerator generator, final DelayConstant delay) throws IOException {
		generator.writeStartObject();
		if (Delay.DEFAULT_NAME.equals(delay.getName()) && delay.getDescription().isEmpty()) {
			generator.writeStringField(DELAY, toDuration(delay.getValue()));
		} else {
			generator.writeObjectFieldStart(DELAY);
			writeNameAndDescription(generator, Delay.DEFAULT_NAME, delay.getName(), delay.getDescription());
			generator.writeStringField(DelayConstant.VALUE, toDuration(delay.getValue()));
			generator.writeEndObject();
		}
		generator.writeEndObject();
	}

	// A random duration is always an object with min/max (min omitted when it keeps its default).
	private void writeDelayRandom(final JsonGenerator generator, final DelayRandom delay) throws IOException {
		generator.writeStartObject();
		generator.writeObjectFieldStart(DELAY);
		writeNameAndDescription(generator, Delay.DEFAULT_NAME, delay.getName(), delay.getDescription());
		if (!DelayRandom.DEFAULT_MIN.equals(delay.getMin())) {
			generator.writeStringField(DelayRandom.MIN, toDuration(delay.getMin()));
		}
		generator.writeStringField(DelayRandom.MAX, toDuration(delay.getMax()));
		generator.writeEndObject();
		generator.writeEndObject();
	}

	private void writeThinkTimeConstant(final JsonGenerator generator, final ThinkTimeConstant thinkTime) throws IOException {
		generator.writeStartObject();
		if (ThinkTime.DEFAULT_NAME.equals(thinkTime.getName()) && thinkTime.getDescription().isEmpty()) {
			generator.writeStringField(THINK_TIME, toDuration(thinkTime.getValue()));
		} else {
			generator.writeObjectFieldStart(THINK_TIME);
			writeNameAndDescription(generator, ThinkTime.DEFAULT_NAME, thinkTime.getName(), thinkTime.getDescription());
			generator.writeStringField(ThinkTimeConstant.VALUE, toDuration(thinkTime.getValue()));
			generator.writeEndObject();
		}
		generator.writeEndObject();
	}

	private void writeThinkTimeRandom(final JsonGenerator generator, final ThinkTimeRandom thinkTime) throws IOException {
		generator.writeStartObject();
		generator.writeObjectFieldStart(THINK_TIME);
		writeNameAndDescription(generator, ThinkTime.DEFAULT_NAME, thinkTime.getName(), thinkTime.getDescription());
		if (!ThinkTimeRandom.DEFAULT_MIN.equals(thinkTime.getMin())) {
			generator.writeStringField(ThinkTimeRandom.MIN, toDuration(thinkTime.getMin()));
		}
		generator.writeStringField(ThinkTimeRandom.MAX, toDuration(thinkTime.getMax()));
		generator.writeEndObject();
		generator.writeEndObject();
	}

	private static void writeNameAndDescription(final JsonGenerator generator, final String defaultName,
			final String name, final Optional<String> description) throws IOException {
		if (!defaultName.equals(name)) {
			generator.writeStringField(Element.NAME, name);
		}
		if (description.isPresent()) {
			generator.writeStringField(Element.DESCRIPTION, description.get());
		}
	}

	private static String toDuration(final String valueInMs) {
		return TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(valueInMs);
	}
}
