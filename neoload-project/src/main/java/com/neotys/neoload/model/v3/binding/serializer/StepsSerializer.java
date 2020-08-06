package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsOrInVariableToStringConverter.TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.DELAY;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.IF;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.JAVASCRIPT;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.LOOP;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.REQUEST;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.SWITCH;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.THINK_TIME;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.TRANSACTION;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.WHILE;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.ImmutableContainer;
import com.neotys.neoload.model.v3.project.userpath.ImmutableIf;
import com.neotys.neoload.model.v3.project.userpath.ImmutableJavaScript;
import com.neotys.neoload.model.v3.project.userpath.ImmutableLoop;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.ImmutableSwitch;
import com.neotys.neoload.model.v3.project.userpath.ImmutableWhile;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;

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
