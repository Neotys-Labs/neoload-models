package com.neotys.neoload.model.v3.validation.naming;


import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.junit.Test;

public class RampupStrategyTest {
	private static NodeImpl rampupOf(final Object policy) {
		return NodeImpl.createPropertyNode("rampup", NodeImpl.setPropertyValue(NodeImpl.createPropertyNode("loadPolicy", null), policy));
	}

	@Test
	public void apply() {
		final RampupStrategy strategy = new RampupStrategy();
		
		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(NodeImpl.createPropertyNode("rampup", null)));
		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(rampupOf(null)));

		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(rampupOf(ConstantLoadPolicy.builder().build())));
		assertEquals(RampupLoadPolicy.INCREMENT_RAMPUP, strategy.apply(rampupOf(RampupLoadPolicy.builder().build())));
		assertEquals(PeaksLoadPolicy.STEP_RAMPUP, strategy.apply(rampupOf(PeaksLoadPolicy.builder().build())));
		assertEquals(CustomLoadPolicy.RAMPUP, strategy.apply(rampupOf(CustomLoadPolicy.builder().build())));
	}
}
