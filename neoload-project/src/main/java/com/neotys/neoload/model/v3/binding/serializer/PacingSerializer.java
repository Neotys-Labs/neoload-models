package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.Pacing;
import com.neotys.neoload.model.v3.project.userpath.PacingConstant;
import com.neotys.neoload.model.v3.project.userpath.PacingRandom;
import java.io.IOException;

public final class PacingSerializer extends StdSerializer<Pacing> {
	private static final long serialVersionUID = 1L;

	public PacingSerializer() {
		super(Pacing.class);
	}

	@Override
	public void serialize(final Pacing pacing, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		if (pacing instanceof PacingConstant) {
			generator.writeString(((PacingConstant) pacing).getValue());
		} else if (pacing instanceof PacingRandom) {
			final PacingRandom pacingRandom = (PacingRandom) pacing;
			generator.writeStartObject();
			generator.writeStringField(PacingRandom.MIN, pacingRandom.getMin());
			generator.writeStringField(PacingRandom.MAX, pacingRandom.getMax());
			generator.writeEndObject();
		}
	}
}
