package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;


public class IOProjectDescriptorTest extends AbstractIOElementsTest {

	private static ProjectDescriptor getDescriptorOnlyRequired() {
		final Project project = Project.builder()
				.name("MyProject")
				.build();

		final ProjectDescriptor descriptor = ProjectDescriptor.builder()
				.project(project)
				.build();

		return descriptor;
	}

	private static ProjectDescriptor getDescriptorRequiredAndOptional() {
		final PopulationPolicy population = PopulationPolicy.builder()
				.name("MyPopulation")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(500)
						.build())
				.build();

		final Scenario scenario = Scenario.builder()
				.name("MyScenario")
				.addPopulations(population)
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addScenarios(scenario)
				.build();

		final ProjectDescriptor descriptor = ProjectDescriptor.builder()
				.addIncludes("C:\\work\\as-codes\\servers.yaml")
				.addIncludes("/as-codes/variables.yaml")
				.project(project)
				.build();
		
		return descriptor;
	}

	@Test
	public void readOnlyRequired() throws IOException {
		final ProjectDescriptor expectedDescriptor = getDescriptorOnlyRequired();
		assertNotNull(expectedDescriptor);

		read("test-descriptor-only-required", expectedDescriptor);
	}

	@Test
	public void readRequiredAndOptional() throws IOException {
		final ProjectDescriptor expectedDescriptor = getDescriptorRequiredAndOptional();
		assertNotNull(expectedDescriptor);

		read("test-descriptor-required-and-optional", expectedDescriptor);
	}
}
