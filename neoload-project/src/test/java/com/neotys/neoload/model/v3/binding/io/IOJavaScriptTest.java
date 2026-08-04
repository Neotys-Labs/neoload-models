package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import com.neotys.neoload.model.v3.project.userpath.JavaScript;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;

public class IOJavaScriptTest extends AbstractIOElementsTest {

	@Test
	public void readJavaScriptOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getJavaScriptOnlyRequired());
		assertNotNull(expectedProject);

		read("test-javascript-only-required", expectedProject);
	}

	@Test
	public void readJavaScriptRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getJavaScriptRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-javascript-required-and-optional", expectedProject);
	}

	@Test
	public void writeJavaScriptOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getJavaScriptOnlyRequired());
		assertNotNull(expectedProject);

		write("test-javascript-only-required", expectedProject);
	}

	@Test
	public void writeJavaScriptRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getJavaScriptRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-javascript-required-and-optional", expectedProject);
	}

	private JavaScript getJavaScriptOnlyRequired() {
		return JavaScript.builder()
				.name("My Javascript")
				.script("// Get variable value from VariableManager\nvar myVar = context.variableManager.getValue(\"CounterVariable_1\");\nlogger.debug(\"ComputedValue=\"+myVar);\n")
				.build();
	}

	private JavaScript getJavaScriptRequiredAndOptional() {
		return JavaScript.builder()
				.name("My Javascript")
				.description("My description")
				.script("// Get variable value from VariableManager\nvar myVar = context.variableManager.getValue(\"CounterVariable_1\");\nlogger.debug(\"ComputedValue=\"+myVar);")
				.build();
	}
}
