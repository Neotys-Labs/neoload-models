package com.neotys.neoload.model.validation.naming;

import static com.neotys.neoload.model.scenario.RampupLoadPolicy.INCREMENT_RAMPUP;
import static com.neotys.neoload.model.scenario.ConstantLoadPolicy.RAMPUP;
import static com.neotys.neoload.model.scenario.PeaksLoadPolicy.STEP_RAMPUP;

import javax.validation.Path.Node;

import org.hibernate.validator.internal.engine.path.NodeImpl;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;

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
