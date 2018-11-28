package com.neotys.neoload.model.v3.project;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ProjectTest {
	@Test
	public void name() {
		assertEquals("servers", Project.SERVERS);
		assertEquals("user_paths", Project.USER_PATHS);
		assertEquals("scenarios", Project.SCENARIOS);
		
		assertEquals("MyProject", Project.DEFAULT_NAME);
		
		final Project project1 = Project.builder().build();
		assertEquals(Project.DEFAULT_NAME, project1.getName());

		final Project project3 = Project.builder().name("").build();
		assertEquals("", project3.getName());

		final Project project4 = Project.builder().name("Test").build();
		assertEquals("Test", project4.getName());
	}
}
