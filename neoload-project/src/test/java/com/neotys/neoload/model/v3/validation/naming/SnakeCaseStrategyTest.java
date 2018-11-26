package com.neotys.neoload.model.v3.validation.naming;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.validation.naming.SnakeCaseStrategy;


public class SnakeCaseStrategyTest {
	@Test
	public void apply() {
		final SnakeCaseStrategy strategy = new SnakeCaseStrategy();
		
		assertNull(strategy.apply(null));
	}

	@Test
	public void translate() {
		final SnakeCaseStrategy strategy = new SnakeCaseStrategy();
		
		assertEquals("", strategy.translate(""));
		assertEquals("rampup", strategy.translate("rampup"));
		assertEquals("rampup", strategy.translate("_rampup"));
		assertEquals("load_policy", strategy.translate("loadPolicy"));
		assertEquals("load_policy", strategy.translate("load_policy"));
		assertEquals("constant_load_policy", strategy.translate("ConstantLoadPolicy"));
		assertEquals("constant_load_policy", strategy.translate("constantLoadPolicy"));
		assertEquals("constant_load_policy", strategy.translate("constant_load_policy"));		
	}
}
