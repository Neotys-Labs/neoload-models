package com.neotys.neoload.model.v3.validation.naming;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import org.hibernate.validator.internal.engine.path.NodeImpl;

import javax.validation.Path.Node;

import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.*;

public final class LoadPolicyStrategy implements PropertyNamingStrategy {
	@Override
	public String apply(final Node node) {
		String name;
		Object value = ((NodeImpl) node).getValue();
		if (value instanceof ConstantLoadPolicy) {
			name = CONSTANT_LOAD;
		} else if (value instanceof RampupLoadPolicy) {
			name = RAMPUP_LOAD;
		} else if (value instanceof PeaksLoadPolicy) {
			name = PEAKS_LOAD;
		} else if (value instanceof CustomLoadPolicy) {
			name = CUSTOM_LOAD;
		} else {
			name = LOAD_POLICY;
		}
		return name;
	}
}
