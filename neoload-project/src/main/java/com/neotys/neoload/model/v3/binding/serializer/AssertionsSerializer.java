package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;

/**
 * Serializes a list of {@link Assertion} using the B-shape discriminator: each
 * entry is wrapped in a key indicating its type ({@code content}, {@code size}, ...).
 * The legacy flat form (no wrapper, fields at the item level) is still accepted on
 * deserialization for backward compatibility but is no longer produced on write.
 */
public class AssertionsSerializer extends StdSerializer<List<Assertion>> {
    private static final long serialVersionUID = -6876213579516249647L;

    public static final String CONTENT = "content";
    public static final String SIZE = "size";
    public static final String DURATION = "duration";

    public AssertionsSerializer() {
        super(List.class, false);
    }

    @Override
    public boolean isEmpty(SerializerProvider prov, List<Assertion> assertions) {
        return (assertions == null) || (assertions.isEmpty());
    }

	@Override
	public void serialize(final List<Assertion> assertions, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		serialize(generator, null, assertions);
    }

	protected static void serialize(final JsonGenerator generator, final String fieldName, final List<Assertion> assertions) throws IOException {
		if ((fieldName != null) && (!fieldName.isEmpty())) {
        	generator.writeArrayFieldStart(fieldName);
		}
		else {
			generator.writeStartArray();
		}

		for (final Assertion assertion : assertions) {
			generator.writeStartObject();
			final String wrapperKey = wrapperKeyFor(assertion);
			if (wrapperKey != null) {
				generator.writeObjectField(wrapperKey, assertion);
			}
			generator.writeEndObject();
		}

		generator.writeEndArray();
    }

	private static String wrapperKeyFor(final Assertion assertion) {
		if (assertion instanceof ContentAssertion) {
			return CONTENT;
		}
		if (assertion instanceof SizeAssertion) {
			return SIZE;
		}
		if (assertion instanceof DurationAssertion) {
			return DURATION;
		}
		return null;
	}
}