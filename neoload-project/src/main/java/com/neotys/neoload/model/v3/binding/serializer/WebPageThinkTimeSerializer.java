package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeConstant.VALUE;
import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom.MAX;
import static com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom.MIN;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTime;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeConstant;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom;
import java.io.IOException;

public final class WebPageThinkTimeSerializer extends StdSerializer<WebPageThinkTime> {
	private static final long serialVersionUID = 6902441003337835213L;

	public WebPageThinkTimeSerializer() {
		super(WebPageThinkTime.class);
	}

	@Override
	public void serialize(final WebPageThinkTime thinkTime, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartObject();
		if (thinkTime instanceof WebPageThinkTimeConstant) {
			generator.writeStringField(VALUE, ((WebPageThinkTimeConstant) thinkTime).getValue());
		} else if (thinkTime instanceof WebPageThinkTimeRandom) {
			final WebPageThinkTimeRandom random = (WebPageThinkTimeRandom) thinkTime;
			generator.writeStringField(MIN, random.getMin());
			generator.writeStringField(MAX, random.getMax());
		}
		generator.writeEndObject();
	}
}
