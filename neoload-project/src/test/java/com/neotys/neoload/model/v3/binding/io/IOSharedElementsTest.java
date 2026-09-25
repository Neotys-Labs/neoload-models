package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.SharedElementRef;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import java.io.IOException;
import org.junit.Test;

public class IOSharedElementsTest extends AbstractIOElementsTest {

	private static Project projectWithoutSharedElements() {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.name("actions")
								.addSteps(Request.builder()
										.url("http://www.neotys.com/login")
										.build())
								.build())
						.build())
				.build();
	}

	private static Project projectWithSharedElement() {
		return Project.builder()
				.name("MyProject")
				.addSharedElements(Container.builder()
						.name("Login")
						.addSteps(Request.builder()
								.url("http://www.neotys.com/login")
								.build())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.name("actions")
								.addSteps(SharedElementRef.builder().name("Login").build())
								.build())
						.build())
				.build();
	}

	private static Project projectWithNestedSharedElement() {
		return Project.builder()
				.name("MyProject")
				.addSharedElements(Loop.builder()
						.name("RetryLogin")
						.count("3")
						.addSteps(SharedElementRef.builder().name("Login").build())
						.build())
				.addSharedElements(Container.builder()
						.name("Login")
						.addSteps(Request.builder()
								.url("http://www.neotys.com/login")
								.build())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.name("actions")
								.addSteps(SharedElementRef.builder().name("RetryLogin").build())
								.build())
						.build())
				.build();
	}

	@Test
	public void readProjectWithoutSharedElements() throws IOException {
		final Project expectedProject = projectWithoutSharedElements();
		assertNotNull(expectedProject);
		assertTrue(expectedProject.getSharedElements().isEmpty());

		read("test-shared-elements-absent", expectedProject);
	}

	@Test
	public void readSharedElement() throws IOException {
		final Project expectedProject = projectWithSharedElement();
		assertNotNull(expectedProject);

		read("test-shared-elements", expectedProject);
	}

	@Test
	public void writeSharedElement() throws IOException {
		final Project expectedProject = projectWithSharedElement();
		assertNotNull(expectedProject);

		write("test-shared-elements", expectedProject);
	}

	@Test
	public void readNestedSharedElement() throws IOException {
		final Project expectedProject = projectWithNestedSharedElement();
		assertNotNull(expectedProject);

		read("test-shared-elements-nested", expectedProject);
	}

	@Test
	public void writeNestedSharedElement() throws IOException {
		final Project expectedProject = projectWithNestedSharedElement();
		assertNotNull(expectedProject);

		write("test-shared-elements-nested", expectedProject);
	}

	@Test
	public void writeOmitsSharedElementsWhenEmpty() throws IOException {
		final Project expectedProject = projectWithoutSharedElements();

		final IO mapper = new IO();
		final String yaml = mapper.write(ProjectDescriptor.builder().project(expectedProject).build(), IO.Format.YAML);
		assertFalse(yaml.contains("shared_elements"));
	}
}
