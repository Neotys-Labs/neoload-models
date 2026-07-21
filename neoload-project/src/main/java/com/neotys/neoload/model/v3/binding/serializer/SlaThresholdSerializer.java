package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdHelper;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;

/**
 * Serializes a {@link SlaThreshold} to its compact textual form (e.g.
 * {@code avg-request-resp-time warn >= 1 per test}), mirroring {@link SlaThresholdDeserializer}.
 */
public final class SlaThresholdSerializer extends StdSerializer<SlaThreshold> {
	private static final long serialVersionUID = -9106590279518753025L;

	public SlaThresholdSerializer() {
		super(SlaThreshold.class);
	}

	@Override
	public void serialize(final SlaThreshold threshold, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeString(SlaThresholdHelper.convertToString(threshold));
	}
}
