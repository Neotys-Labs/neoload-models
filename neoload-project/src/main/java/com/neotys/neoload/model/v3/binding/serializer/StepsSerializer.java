package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsOrInVariableToStringConverter.TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.DELAY;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.THINK_TIME;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.TRANSACTION;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;

public class StepsSerializer extends StdSerializer<List<Step>> {
    private static final long serialVersionUID = -4569870233567503685L;

    public StepsSerializer() {
        super(List.class, false);
    }

	@Override
	public void serialize(final List<Step> steps, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartArray();
				
		for (final Step step : steps) {
			if (step instanceof Container) {
				generator.writeStartObject();
				generator.writeObjectField(TRANSACTION, (Container)step);
				generator.writeEndObject();
			}
			else if (step instanceof Delay) {
				generator.writeStartObject();
				generator.writeStringField(DELAY, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(((Delay)step).getValue()));
				generator.writeEndObject();
			}
			else if (step instanceof ThinkTime) {
				generator.writeStartObject();
				generator.writeStringField(THINK_TIME, TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING.convert(((ThinkTime)step).getValue()));
				generator.writeEndObject();
			}
		}
		
		generator.writeEndArray();
    }
}
