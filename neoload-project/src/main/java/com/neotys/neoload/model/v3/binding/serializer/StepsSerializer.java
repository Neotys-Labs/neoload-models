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
		builder.put(ImmutableGoToNextIteration.class, GO_TO_NEXT_ITERATION);
		builder.put(ImmutableFork.class, FORK);
		builder.put(ImmutableVariableModifier.class, VARIABLE_MODIFIER);
		builder.put(ImmutableRendezvous.class, RENDEZVOUS);
    	STEPS = builder.build();
    }

    public StepsSerializer() {
        super(List.class, false);
    }

	@Override
	public void serialize(final List<Step> steps, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartArray();
				
		for (final Step step : steps) {
			if (step instanceof Delay) {
				generator.writeStartObject();
				generator.writeStringField(DELAY, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(((Delay)step).getValue()));
				generator.writeEndObject();
			}
			else if (step instanceof ThinkTime) {
				generator.writeStartObject();
				generator.writeStringField(THINK_TIME, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(((ThinkTime)step).getValue()));
				generator.writeEndObject();
			}
			// Since GoToNextIteration has no properties, we chose to serialize as a bare scalar string
			else if (step instanceof GoToNextIteration) {
				generator.writeString(GO_TO_NEXT_ITERATION);
			}
			else if (step instanceof Rendezvous) {
				final Rendezvous rdv = (Rendezvous) step;
				if (RENDEZVOUS.equals(rdv.getName()) && rdv.getDescription().isEmpty()) {
					generator.writeString(RENDEZVOUS);
				} else {
					generator.writeStartObject();
					generator.writeObjectField(RENDEZVOUS, rdv);
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
