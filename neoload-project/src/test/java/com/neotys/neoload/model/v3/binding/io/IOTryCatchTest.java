package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOTryCatchTest extends AbstractIOElementsTest {

	@Test
	public void readTryCatch() throws IOException {
		final Project expectedProject = buildProjectContainingTryCatch();
		assertNotNull(expectedProject);

		read("test-try-catch", expectedProject);
	}

	private Project buildProjectContainingTryCatch() {
		final TryCatch tryCatch = TryCatch.builder()
				.name("my_try_catch")
				.policy(TryCatch.Policy.catch_errors)
				.getTry(Container.builder()
						.addSteps(
								Delay.builder().value("1000").build(),
								Delay.builder().value("2000").build()
						)
						.build())
				.getCatch(Container.builder()
						.addSteps(
								Delay.builder().value("500").build()
						)
						.build())
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(tryCatch)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("user_path_1")
				.actions(container)
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}