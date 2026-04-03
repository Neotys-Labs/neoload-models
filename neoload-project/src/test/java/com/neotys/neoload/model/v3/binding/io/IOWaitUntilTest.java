package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Match;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;


public class IOWaitUntilTest extends AbstractIOElementsTest {

	@Test
	public void readWaitUntil() throws IOException {
		final Project expectedProject = buildProjectContainingWaitUntil();
		assertNotNull(expectedProject);

		read("test-wait-until", expectedProject);
	}

	private Project buildProjectContainingWaitUntil() {
		final WaitUntil waitUntil = WaitUntil.builder()
				.addConditions(Condition.builder()
						.operand1("${status_code}")
						.operator(Condition.Operator.EQUALS)
						.operand2("200")
						.build())
				.match(Match.ANY)
				.timeout("30000")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(waitUntil)
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