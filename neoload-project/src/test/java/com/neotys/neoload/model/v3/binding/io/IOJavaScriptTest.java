package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import com.neotys.neoload.model.v3.project.userpath.JavaScript;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

public class IOJavaScriptTest extends AbstractIOElementsTest {

	@Test
	public void readJavaScriptOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingJavaScript();
		assertNotNull(expectedProject);

		read("test-javascript-only-required", expectedProject);
	}

	private Project buildProjectContainingJavaScript() {

		final JavaScript javascript = JavaScript.builder()
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
