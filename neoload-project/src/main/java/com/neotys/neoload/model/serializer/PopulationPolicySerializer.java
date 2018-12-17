package com.neotys.neoload.model.serializer;

import static com.neotys.neoload.model.core.Element.NAME;
import static com.neotys.neoload.model.scenario.PopulationPolicy.CONSTANT_LOAD;
import static com.neotys.neoload.model.scenario.PopulationPolicy.PEAKS_LOAD;
import static com.neotys.neoload.model.scenario.PopulationPolicy.RAMPUP_LOAD;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.LoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;


/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
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
        }
        else if (loadPolicy instanceof RampupLoadPolicy) {
        	loadPolicyFieldName = RAMPUP_LOAD;
        }
        else if (loadPolicy instanceof PeaksLoadPolicy) {
        	loadPolicyFieldName = PEAKS_LOAD;
        }
        if (loadPolicyFieldName != null) {
        	jgen.writeObjectField(loadPolicyFieldName, loadPolicy);
        }
        // End object tag
        jgen.writeEndObject();
    }
}