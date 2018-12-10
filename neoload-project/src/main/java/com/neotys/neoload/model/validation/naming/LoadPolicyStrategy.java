package com.neotys.neoload.model.validation.naming;

import static com.neotys.neoload.model.scenario.PopulationPolicy.LOAD_POLICY;
import static com.neotys.neoload.model.scenario.PopulationPolicy.CONSTANT_LOAD;
import static com.neotys.neoload.model.scenario.PopulationPolicy.RAMPUP_LOAD;
import static com.neotys.neoload.model.scenario.PopulationPolicy.PEAKS_LOAD;

import javax.validation.Path.Node;

import org.hibernate.validator.internal.engine.path.NodeImpl;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;

@Deprecated
public final class LoadPolicyStrategy implements PropertyNamingStrategy {
    @Override
    public String apply(final Node node) {
    	String name = null;
        Object value = ((NodeImpl) node).getValue();
    	if (value instanceof ConstantLoadPolicy) {
    		name = CONSTANT_LOAD;
        }
        else if (value instanceof RampupLoadPolicy) {
        	name = RAMPUP_LOAD;
        }
        else if (value instanceof PeaksLoadPolicy) {
        	name = PEAKS_LOAD;
        }
    	if (name == null) {
    		name = LOAD_POLICY;
    	}
    	return name;
    }		
}
