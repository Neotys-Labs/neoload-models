package com.neotys.neoload.model.v3.project;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;


public class ProjectTest {
	@Test
	public void name() {
		assertEquals("MyProject", Project.DEFAULT_NAME);
		
		final Project project1 = Project.builder().build();
		assertEquals(Project.DEFAULT_NAME, project1.getName());

		final Project project3 = Project.builder().name("").build();
		assertEquals("", project3.getName());

		final Project project4 = Project.builder().name("Test").build();
		assertEquals("Test", project4.getName());
	}
}
