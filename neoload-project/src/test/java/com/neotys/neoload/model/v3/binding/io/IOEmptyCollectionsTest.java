package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


/**
 * A project being designed can hold user path containers and transactions with no step yet, and a scenario
 * with no population yet. Such a project must round-trip: the export omits the empty collections and the
 * loader accepts the result.
 */
public class IOEmptyCollectionsTest extends AbstractIOElementsTest {

	@Test
	public void readEmptyCollections() throws IOException {
		final Project expectedProject = buildProjectWithEmptyCollections();
		assertNotNull(expectedProject);

		read("test-empty-collections", expectedProject);
	}

	@Test
	public void writeEmptyCollections() throws IOException {
		final Project expectedProject = buildProjectWithEmptyCollections();
		assertNotNull(expectedProject);

		write("test-empty-collections", expectedProject);
	}

	private Project buildProjectWithEmptyCollections() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.build())
				.build();

		final UserPath userPathWithEmptyContainers = UserPath.builder()
				.name("MyUserPathWithEmptyContainers")
				.init(Container.builder()
						.name("init")
						.build())
				.actions(Container.builder()
						.name("actions")
						.addSteps(Container.builder()
								.name("MyTransaction")
								.build())
						.build())
				.end(Container.builder()
						.name("end")
						.build())
				.build();

		final Population population = Population.builder()
				.name("MyPopulation")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.build())
				.build();

		final Scenario scenario = Scenario.builder()
				.name("MyScenario")
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath, userPathWithEmptyContainers)
				.addPopulations(population)
				.addScenarios(scenario)
				.build();
	}
}
