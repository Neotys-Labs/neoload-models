package com.neotys.neoload.model.v3.binding.io;

import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RendezvousPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;

public class IOIntegrationTest extends AbstractIOElementsTest {

	@Test
	public void readVariableOnlyRequired() throws IOException {
		final Project expectedProject = buildProject();
		assertNotNull(expectedProject);

		read("test-integration", expectedProject);
	}

	private Project buildProject() {
		return Project.builder()
				.name("MyProject")
				.addServers(buildServer())
				.addVariables(buildVariable())
				.addPopulations(buildPopulation())
				.addScenarios(buildScenario())
				.addUserPaths(buildUserPath())
				.build();
	}

	private Server buildServer() {
		return Server.builder()
				.name("serverName")
				.host("mypc.intranet.company.com")
				.port("80")
				.build();
	}

	private Variable buildVariable() {
		return FileVariable.builder()
				.name("products_file")
				.path("data/list_of_products.csv")
				.isFirstLineColumnNames(true)
				.build();
	}

	private UserPath buildUserPath() {
		return UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Container.builder()
								.name("MyTransaction")
								.addSteps(Request.builder()
												.url("http://www.company.com/select?name:product")
												.build(),
										ThinkTime.builder().value("1000")
												.build())
								.build())
						.build())
				.build();
	}


	private Population buildPopulation() {
		return Population.builder().name("MyPopulation")
				.addUserPaths(UserPathPolicy.builder().name("MyUserPath")
						.distribution(100)
						.build())
				.build();
	}

	private Scenario buildScenario() {
		RendezvousPolicy rendezvousPolicy = RendezvousPolicy.builder().name("rdv").timeout(100).when(WhenRelease.builder().type(WhenRelease.Type.VU_NUMBER).value(200).build()).build();
		return Scenario.builder()
				.name("MyScenario")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(500)
								.build())
						.build())
				.addRendezvousPolicies(rendezvousPolicy)
				.build();
	}

}
