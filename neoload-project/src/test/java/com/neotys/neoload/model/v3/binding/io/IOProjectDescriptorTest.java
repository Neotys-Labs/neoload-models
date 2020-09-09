package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import com.neotys.neoload.model.v3.project.scenario.*;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;


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
		final RendezvousPolicy rdv1 = RendezvousPolicy.builder().name("rdv1").timeout(200).when(WhenRelease.builder().type(WhenRelease.Type.MANUAL).value("manual").build()).build();
		final RendezvousPolicy rdv2 = RendezvousPolicy.builder().name("rdv2").timeout(100).when(WhenRelease.builder().type(WhenRelease.Type.PERCENTAGE).value("50").build()).build();;
		final RendezvousPolicy rdv3 = RendezvousPolicy.builder().name("rdv3").timeout(20).when(WhenRelease.builder().type(WhenRelease.Type.VU_NUMBER).value("20").build()).build();;

		final Scenario scenario = Scenario.builder()
				.name("MyScenario")
				.addPopulations(population)
				.addRendezvousPolicies(rdv1, rdv2, rdv3)
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
