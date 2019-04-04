package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ProjectDescriptorTest {
	@Test
	public void name() {
		assertEquals("includes", ProjectDescriptor.INCLUDES);
		
		assertEquals(ProjectDescriptor.builder().build().getProject(), ProjectDescriptor.DEFAULT_PROJECT);
	}
}
