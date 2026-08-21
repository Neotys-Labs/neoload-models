package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.project.variable.ChangePolicyVariable.ChangePolicy.EACH_USE;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.RandomUUIDVariable;
import java.io.IOException;
import org.junit.Test;

public class IORandomUUIDTest extends AbstractIOElementsTest {

	@Test
	public void readRandomUUIDOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingRandomUUIDOnlyRequired();
		assertNotNull(expectedProject);

		read("test-random-uuid-only-required", expectedProject);
	}

	@Test
	public void writeRandomUUIDOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingRandomUUIDOnlyRequired();
		assertNotNull(expectedProject);

		write("test-random-uuid-only-required", expectedProject);
	}

	@Test
	public void readRandomUUIDRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingRandomUUIDRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-random-uuid-required-and-optional", expectedProject);
	}

	@Test
	public void writeRandomUUIDRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingRandomUUIDRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-random-uuid-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingRandomUUIDOnlyRequired() {
		final RandomUUIDVariable minimalRandomUUIDVariable = RandomUUIDVariable.builder()
				.name("MyRandomUUID")
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(minimalRandomUUIDVariable)
				.build();
	}

	private Project buildProjectContainingRandomUUIDRequiredAndOptional() {
		final RandomUUIDVariable fullRandomUUIDVariable = RandomUUIDVariable.builder()
				.name("MyRandomUUID")
				.description("MyRandomUUIDDescription")
				.isUpperCase(true)
				.isPredictable(true)
				.changePolicy(EACH_USE)
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(fullRandomUUIDVariable)
				.build();
	}
}
