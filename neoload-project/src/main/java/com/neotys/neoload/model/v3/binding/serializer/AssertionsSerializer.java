package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;

public class AssertionsSerializer extends StdSerializer<List<Assertion>> {
    private static final long serialVersionUID = -6876213579516249647L;

    public AssertionsSerializer() {
        super(List.class, false);
    }

    @Override
    public boolean isEmpty(SerializerProvider prov, List<Assertion> assertions) {
        return (assertions == null) || (assertions.isEmpty());
    }

	@Override
	public void serialize(final List<Assertion> assertions, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		serialize(generator, null	, assertions);
    }	
	
	protected static void serialize(final JsonGenerator generator, final String fieldName, final List<Assertion> assertions) throws IOException {
		if ((fieldName != null) && (!fieldName.isEmpty())) {
        	generator.writeArrayFieldStart(fieldName);
		}
		else {
			generator.writeStartArray();
		}
        
		for (final Assertion assertion : assertions) {
        	generator.writeObject(assertion);				
		}
        
		generator.writeEndArray();             
    }
}
