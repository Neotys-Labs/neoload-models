package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.project.variable.Variable.ChangePolicy.EACH_USE;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.RandomStringVariable;
import java.io.IOException;
import org.junit.Test;

public class IORandomStringTest extends AbstractIOElementsTest {

	@Test
	public void readRandomStringOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingRandomStringOnlyRequired();
		assertNotNull(expectedProject);

		read("test-random-string-only-required", expectedProject);
	}

	@Test
	public void writeRandomStringOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingRandomStringOnlyRequired();
		assertNotNull(expectedProject);

		write("test-random-string-only-required", expectedProject);
	}

	@Test
	public void readRandomStringRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingRandomStringRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-random-string-required-and-optional", expectedProject);
	}

	@Test
	public void writeRandomStringRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingRandomStringRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-random-string-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingRandomStringOnlyRequired() {
		final RandomStringVariable minimalRandomStringVariable = RandomStringVariable.builder()
				.name("MyRandomString")
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(minimalRandomStringVariable)
				.build();
	}

	private Project buildProjectContainingRandomStringRequiredAndOptional() {
		final RandomStringVariable fullRandomStringVariable = RandomStringVariable.builder()
				.name("MyRandomString")
				.description("MyRandomStringDescription")
				.minLength(10)
				.maxLength(20)
				.isPredictable(true)
				.changePolicy(EACH_USE)
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(fullRandomStringVariable)
				.build();
	}
}
