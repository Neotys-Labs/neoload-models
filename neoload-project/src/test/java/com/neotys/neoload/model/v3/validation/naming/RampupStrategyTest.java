package com.neotys.neoload.model.v3.validation.naming;


import static org.junit.Assert.assertEquals;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.validation.naming.RampupStrategy;


public class RampupStrategyTest {
	@Test
	public void apply() {
		final RampupStrategy strategy = new RampupStrategy();
		
		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), null)));

		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), ConstantLoadPolicy.builder().build())));
		assertEquals(RampupLoadPolicy.INCREMENT_RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), RampupLoadPolicy.builder().build())));
		assertEquals(PeaksLoadPolicy.STEP_RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), PeaksLoadPolicy.builder().build())));

	}
}
