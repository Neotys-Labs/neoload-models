package com.neotys.neoload.model.v3.util;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ProjectUtilsTest {

	@Test
	public void asCodeProjectUniqueName() {
		final ImmutableProject project = Project.builder()
				.name("ascode-_Project")
				.scenarios(Arrays.asList(Scenario.builder().name("nlweb_scenario").slaProfile("nlweb_scenario_sla_profile").build()))
				.build();
		assertEquals("ascode-_Project", ProjectUtils.checkUniqueName(Arrays.asList(project)));
	}

	@Test
	public void asCodeProjectSeveralSameName() {
		final ImmutableProject project = Project.builder()
				.name("ascode-_Project")
				.scenarios(Arrays.asList(Scenario.builder().name("nlweb_scenario").slaProfile("nlweb_scenario_sla_profile").build()))
				.build();
		final ImmutableProject project2 = Project.builder()
				.name("ascode-_Project")
				.scenarios(Arrays.asList(Scenario.builder().name("nlweb_scenario").slaProfile("nlweb_scenario_sla_profile").build()))
				.build();

		assertEquals("ascode-_Project", ProjectUtils.checkUniqueName(ImmutableList.of(project, project2)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void asCodeProjectDefaultName() {
		final ImmutableProject project = Project.builder()
				.scenarios(Arrays.asList(Scenario.builder().slaProfile("nlweb_scenario_sla_profile").build()))
				.build();

		ProjectUtils.checkUniqueName(ImmutableList.of(project));
	}

	@Test(expected = RuntimeException.class)
	public void asCodeProjectNameFailsBecauseNameNotUnique() {
		final ImmutableProject project = Project.builder()
				.name("ascode-_Project")
				.scenarios(Arrays.asList(Scenario.builder().name("nlweb_scenario").slaProfile("nlweb_scenario_sla_profile").build()))
				.build();
		final ImmutableProject project2 = Project.builder()
				.name("failure")
				.build();
		ProjectUtils.checkUniqueName(ImmutableList.of(project, project2));
	}
}
