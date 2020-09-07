package com.neotys.neoload.model.v3.validation.naming;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import org.hibernate.validator.internal.engine.path.NodeImpl;

import javax.validation.Path.Node;

import static com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy.RAMPUP;
import static com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy.STEP_RAMPUP;
import static com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy.INCREMENT_RAMPUP;

public final class RampupStrategy implements PropertyNamingStrategy {
	@Override
	public String apply(final Node node) {
		String name;
		Object value = ((NodeImpl) node).getValue();
		if (value instanceof ConstantLoadPolicy || value instanceof CustomLoadPolicy) {
			name = RAMPUP;
		} else if (value instanceof RampupLoadPolicy) {
			name = INCREMENT_RAMPUP;
		} else if (value instanceof PeaksLoadPolicy) {
			name = STEP_RAMPUP;
		} else {
			name = RAMPUP;
		}
		return name;
	}
}
