package com.neotys.neoload.model.v3.validation.naming;


import static org.junit.Assert.assertEquals;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.ContainerElement;
import com.neotys.neoload.model.v3.project.userpath.Container;


public class ElementsStrategyTest {
	@Test
	public void apply() {
		final ElementsStrategy strategy = new ElementsStrategy();
		
		assertEquals(ContainerElement.DO, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), null)));

		assertEquals(ContainerElement.DO, strategy.apply(NodeImpl.setPropertyValue(NodeImpl.createBeanNode(null), Container.builder().build())));
	}
}
