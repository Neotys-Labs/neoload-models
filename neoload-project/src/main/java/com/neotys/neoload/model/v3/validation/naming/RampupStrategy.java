package com.neotys.neoload.model.v3.validation.naming;

import static com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy.RAMPUP;
import static com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy.STEP_RAMPUP;
import static com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy.INCREMENT_RAMPUP;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import javax.validation.Path.Node;
import org.hibernate.validator.internal.engine.path.NodeImpl;

/**
 * Names the ramp-up of a load policy after the key of its policy: {@code rampup}, {@code increment_rampup} or {@code step_rampup}.
 */
public final class RampupStrategy implements PropertyNamingStrategy {
	@Override
	public String apply(final Node node) {
		String name;
		final NodeImpl parent = ((NodeImpl) node).getParent();
		final Object value = parent == null ? null : parent.getValue();
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
