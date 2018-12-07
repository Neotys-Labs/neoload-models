package com.neotys.neoload.model.v3.validation.naming;

import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.CONSTANT_LOAD;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.LOAD_POLICY;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.PEAKS_LOAD;
import static com.neotys.neoload.model.v3.project.scenario.PopulationPolicy.RAMPUP_LOAD;

import javax.validation.Path.Node;

import org.hibernate.validator.internal.engine.path.NodeImpl;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;

public final class LoadPolicyStrategy implements PropertyNamingStrategy {
	@Override
	public String apply(final Node node) {
		String name = null;
		Object value = ((NodeImpl) node).getValue();
		if (value instanceof ConstantLoadPolicy) {
			name = CONSTANT_LOAD;
		} else if (value instanceof RampupLoadPolicy) {
			name = RAMPUP_LOAD;
		} else if (value instanceof PeaksLoadPolicy) {
			name = PEAKS_LOAD;
		}
		if (name == null) {
			name = LOAD_POLICY;
		}
		return name;
	}
}
