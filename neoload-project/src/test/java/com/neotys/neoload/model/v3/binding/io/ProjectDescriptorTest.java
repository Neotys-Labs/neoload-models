package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;


public class ProjectDescriptorTest {
	@Test
	public void name() {
		assertEquals("includes", ProjectDescriptor.INCLUDES);
		
		assertEquals(Project.builder().build(), ProjectDescriptor.DEFAULT_PROJECT);
	}
}
