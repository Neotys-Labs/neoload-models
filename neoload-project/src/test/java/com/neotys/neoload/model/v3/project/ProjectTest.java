package com.neotys.neoload.model.v3.project;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ProjectTest {
	@Test
	public void name() {
		assertEquals("name", Project.NAME);
		assertEquals("sla_profiles", Project.SLA_PROFILES);
		assertEquals("variables", Project.VARIABLES);
		assertEquals("servers", Project.SERVERS);
		assertEquals("user_paths", Project.USER_PATHS);
		assertEquals("populations", Project.POPULATIONS);
		assertEquals("scenarios", Project.SCENARIOS);
	}
}
