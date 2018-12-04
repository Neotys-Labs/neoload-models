package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.ImmutableConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.TestCase.assertNotNull;


public class IOVariableTest extends AbstractIOElementsTest {

	@Test
	public void readVariableOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = buildProjectContainingVariable();
		assertNotNull(expectedProject);

		read("test-variable-only-required", expectedProject);
	}

	private Project buildProjectContainingVariable() {
		final Variable variable = ImmutableConstantVariable.builder()
				.name("variable_name")
				.value("variable_value")
				.description("variable description")
				.build();
		return Project.builder()
				.name("MyProject")
				.addVariables(variable)
				.build();
	}
}
