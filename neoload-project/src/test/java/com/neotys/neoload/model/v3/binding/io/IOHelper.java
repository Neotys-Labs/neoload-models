package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

import java.util.List;


/**
 * Shared helpers for the unit tests.
 */
final class IOHelper {

    private IOHelper() {
    }

	static Project buildProject(final Container actions) {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(actions)
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	static Project buildProject(final Step... steps) {
		final Container actions = Container.builder()
				.name("actions")
				.addSteps(steps)
				.build();

		return buildProject(actions);
	}

	static Project buildProject(final List<? extends Step> steps) {
		final Container actions = Container.builder()
				.name("actions")
				.addAllSteps(steps)
				.build();

		return buildProject(actions);
	}

	static Project buildProject(final Scenario scenario) {
		return Project.builder()
				.name("MyProject")
				.addScenarios(scenario)
				.build();
	}

	static Project buildProject(final Server... server) {
		return Project.builder()
				.name("MyProject")
				.addServers(server)
				.build();
	}
}
