package com.neotys.neoload.model.validation.naming;


import static org.junit.Assert.assertEquals;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.junit.Test;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;


public class RampupStrategyTest {
	@Test
	public void translate() {
		final RampupStrategy strategy = new RampupStrategy();
		
		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), null)));

		assertEquals(ConstantLoadPolicy.RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), ConstantLoadPolicy.builder().build())));
		assertEquals(RampupLoadPolicy.INCREMENT_RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), RampupLoadPolicy.builder().build())));
		assertEquals(PeaksLoadPolicy.STEP_RAMPUP, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), PeaksLoadPolicy.builder().build())));

	}
}
