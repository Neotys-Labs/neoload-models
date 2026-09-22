package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.PasswordVariable;
import java.io.IOException;
import org.junit.Test;

public class IOPasswordTest extends AbstractIOElementsTest {

	@Test
	public void readPasswordOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingPasswordOnlyRequired();
		assertNotNull(expectedProject);

		read("test-password-only-required", expectedProject);
	}

	@Test
	public void writePasswordOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingPasswordOnlyRequired();
		assertNotNull(expectedProject);

		write("test-password-only-required", expectedProject);
	}

	@Test
	public void readPasswordRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingPasswordRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-password-required-and-optional", expectedProject);
	}

	@Test
	public void writePasswordRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingPasswordRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-password-required-and-optional", expectedProject);
	}

	@Test
	public void readPasswordRejectsValueChangeProperties() {
		for (final String property : new String[]{"change_policy: each_use", "scope: unique", "order: sequential", "out_of_value: stop_test"}) {
			final String content = "name: MyProject\nvariables:\n- password:\n    name: MyPassword\n    value: s3cr3t\n    " + property + "\n";

			assertThatThrownBy(() -> new IO().read(content))
					.isInstanceOf(UnrecognizedPropertyException.class);
		}
	}

	private Project buildProjectContainingPasswordOnlyRequired() {
		final PasswordVariable minimalPasswordVariable = PasswordVariable.builder()
				.name("MyPassword")
				.value("s3cr3t")
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(minimalPasswordVariable)
				.build();
	}

	private Project buildProjectContainingPasswordRequiredAndOptional() {
		final PasswordVariable fullPasswordVariable = PasswordVariable.builder()
				.name("MyPassword")
				.description("MyPasswordDescription")
				.value("s3cr3t")
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(fullPasswordVariable)
				.build();
	}
}
