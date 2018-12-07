package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.project.Element.NAME;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.CONSTANT_LOAD;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.PEAKS_LOAD;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.RAMPUP_LOAD;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;


public final class PopulationPolicySerializer extends StdSerializer<PopulationPolicy> {
	private static final long serialVersionUID = 7048026935384614957L;

	public PopulationPolicySerializer() {
		super(PopulationPolicy.class);
	}

	@Override
	public void serialize(final PopulationPolicy population, final JsonGenerator jgen, final SerializerProvider sp) throws IOException {
		// Start object tag
		jgen.writeStartObject();
		// Name field
		jgen.writeStringField(NAME, population.getName());
		// LoadPolicy field
		String loadPolicyFieldName = null;
		final LoadPolicy loadPolicy = population.getLoadPolicy();
		if (loadPolicy instanceof ConstantLoadPolicy) {
			loadPolicyFieldName = CONSTANT_LOAD;
		} else if (loadPolicy instanceof RampupLoadPolicy) {
			loadPolicyFieldName = RAMPUP_LOAD;
		} else if (loadPolicy instanceof PeaksLoadPolicy) {
			loadPolicyFieldName = PEAKS_LOAD;
		}
		if (loadPolicyFieldName != null) {
			jgen.writeObjectField(loadPolicyFieldName, loadPolicy);
		}
		// End object tag
		jgen.writeEndObject();
	}
}