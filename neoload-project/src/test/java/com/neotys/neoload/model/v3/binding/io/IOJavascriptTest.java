package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Javascript;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

public class IOJavascriptTest extends AbstractIOElementsTest {

	@Test
	public void readJavascriptOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingJavascript();
		assertNotNull(expectedProject);

		read("test-javascript-only-required", expectedProject);
	}

	private Project buildProjectContainingJavascript() {

		final Javascript javascript = Javascript.builder()
				.name("My Javascript")
				.description("My description")
				.script("// Get variable value from VariableManager\nvar myVar = context.variableManager.getValue(\"CounterVariable_1\");\nlogger.debug(\"ComputedValue=\"+myVar);")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(javascript)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(container)
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}
