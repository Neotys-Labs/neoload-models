package com.neotys.neoload.model.validation.naming;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.validation.naming.SnakeCaseStrategy;


public class SnakeCaseStrategyTest {
	@Test
	public void translate() {
		final SnakeCaseStrategy strategy = new SnakeCaseStrategy();
		
		assertEquals("", strategy.translate(""));
		assertEquals("rampup", strategy.translate("rampup"));
		assertEquals("load_policy", strategy.translate("loadPolicy"));
		assertEquals("load_policy", strategy.translate("load_policy"));
		assertEquals("constant_load_policy", strategy.translate("ConstantLoadPolicy"));
		assertEquals("constant_load_policy", strategy.translate("constantLoadPolicy"));
		assertEquals("constant_load_policy", strategy.translate("constant_load_policy"));		
	}
}
