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
	private static final long serialVersionUID = -9023562357689565951L;

	public PopulationPolicySerializer() {
		super(PopulationPolicy.class);
	}

	@Override
	public void serialize(final PopulationPolicy populationPolicy, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartObject();
        generator.writeStringField(NAME, populationPolicy.getName());
        final LoadPolicy loadPolicy = populationPolicy.getLoadPolicy();
        if (loadPolicy instanceof ConstantLoadPolicy) {
        	generator.writeObjectField(CONSTANT_LOAD, loadPolicy);
        }
        else if (loadPolicy instanceof RampupLoadPolicy) {
        	generator.writeObjectField(RAMPUP_LOAD, loadPolicy);
        }
        else if (loadPolicy instanceof PeaksLoadPolicy) {
        	generator.writeObjectField(PEAKS_LOAD, loadPolicy);
        }        
        generator.writeEndObject();		
	}
}