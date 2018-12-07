package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


public class IOPopulationsTest extends AbstractIOElementsTest {

	private static Project getPopulationsOnlyRequired() {
		final Population population = Population.builder()
				.name("MyPopulation1")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath11")
						.build())
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath12")
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addPopulations(population)
				.build();

		return project;
	}

	private static Project getPopulationsRequiredAndOptional() {
		final Population population = Population.builder()
				.name("MyPopulation1")
				.description("My population 1 with 2 user paths")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath11")
						.distribution(75.0)
						.build())
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath12")
						.distribution(25.0)
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addPopulations(population)
				.build();

		return project;
	}

	@Test
	public void readPopulationsOnlyRequired() throws IOException {
		final Project expectedProject = getPopulationsOnlyRequired();
		assertNotNull(expectedProject);

		read("test-populations-only-required", expectedProject);
		read("test-populations-only-required", expectedProject);
	}

	@Test
	public void readPopulationsRequiredAndOptional() throws IOException {
		final Project expectedProject = getPopulationsRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-populations-required-and-optional", expectedProject);
		read("test-populations-required-and-optional", expectedProject);
	}

	@Test
	public void writePopulationsOnlyRequired() throws IOException {
		final Project expectedProject = getPopulationsOnlyRequired();
		assertNotNull(expectedProject);

		write("test-populations-only-required", expectedProject);
		write("test-populations-only-required", expectedProject);
	}

	@Test
	public void writePopulationsRequiredAndOptional() throws IOException {
		final Project expectedProject = getPopulationsRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-populations-required-and-optional", expectedProject);
		write("test-populations-required-and-optional", expectedProject);
	}
}
