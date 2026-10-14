package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import java.io.IOException;

/**
 * Writes the web page {@code think_time} field, which shares the step duration syntax
 * but carries no name or description.
 */
public final class WebPageDelaySerializer extends StdSerializer<Delay> {
	private static final long serialVersionUID = 6902441003337835213L;

	public WebPageDelaySerializer() {
		super(Delay.class);
	}

	@Override
	public void serialize(final Delay delay, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		DelayHelper.write(generator, delay, false);
	}
}
