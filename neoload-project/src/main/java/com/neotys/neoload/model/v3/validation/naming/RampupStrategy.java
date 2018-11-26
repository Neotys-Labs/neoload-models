package com.neotys.neoload.model.v3.validation.naming;

import static com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy.RAMPUP;
import static com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy.STEP_RAMPUP;
import static com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy.INCREMENT_RAMPUP;

import javax.validation.Path.Node;

import org.hibernate.validator.internal.engine.path.NodeImpl;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;

public final class RampupStrategy implements PropertyNamingStrategy {
    @Override
    public String apply(final Node node) {
    	String name = null;
        Object value = ((NodeImpl) node).getValue();
    	if (value instanceof ConstantLoadPolicy) {
    		name = RAMPUP;
        }
        else if (value instanceof RampupLoadPolicy) {
        	name = INCREMENT_RAMPUP;
        }
        else if (value instanceof PeaksLoadPolicy) {
        	name = STEP_RAMPUP;
        }
    	if (name == null) {
    		name = RAMPUP;
    	}
    	return name;
    }		
}
