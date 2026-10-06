package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.security.WsDerivedPasswordType;
import com.neotys.neoload.model.v3.project.security.WsPasswordType;
import com.neotys.neoload.model.v3.project.security.WsSimplePasswordType;
import java.io.IOException;

public final class WsPasswordTypeSerializer extends StdSerializer<WsPasswordType> {
	private static final long serialVersionUID = 4521904785632148893L;

	public WsPasswordTypeSerializer() {
		super(WsPasswordType.class);
	}

	@Override
	public void serialize(final WsPasswordType passwordType, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		if (passwordType instanceof WsSimplePasswordType) {
			generator.writeString(((WsSimplePasswordType) passwordType).getValue());
		} else if (passwordType instanceof WsDerivedPasswordType) {
			final WsDerivedPasswordType derived = (WsDerivedPasswordType) passwordType;
			generator.writeStartObject();
			generator.writeObjectFieldStart(WsDerivedPasswordType.DERIVED_KEY);
			generator.writeStringField(WsDerivedPasswordType.SALT, derived.getSalt());
			final String iteration = derived.getIteration();
			if (iteration.matches("\\d+")) {
				generator.writeFieldName(WsDerivedPasswordType.ITERATION);
				generator.writeNumber(iteration);
			} else {
				generator.writeStringField(WsDerivedPasswordType.ITERATION, iteration);
			}
			generator.writeEndObject();
			generator.writeEndObject();
		}
	}
}
