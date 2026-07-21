package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.serializer.ifthenelse.ConditionHelper;
import com.neotys.neoload.model.v3.project.userpath.Condition;

/**
 * Serializes a {@link Condition} to its compact textual form (e.g. {@code ${var} equals 2}),
 * mirroring {@link ConditionDeserializer}.
 */
public final class ConditionSerializer extends StdSerializer<Condition> {
	private static final long serialVersionUID = -9106185970275309525L;

	public ConditionSerializer() {
		super(Condition.class);
	}

	@Override
	public void serialize(final Condition condition, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(ConditionHelper.convertToString(condition));
	}
}
