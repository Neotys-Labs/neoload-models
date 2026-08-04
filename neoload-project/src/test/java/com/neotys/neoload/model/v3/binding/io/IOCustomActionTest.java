package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static com.neotys.neoload.model.v3.project.userpath.CustomActionParameter.Type.PASSWORD;
import static junit.framework.TestCase.assertNotNull;

public class IOCustomActionTest extends AbstractIOElementsTest {
    @Test
    public void readCustomActionOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getCustomActionOnlyRequired());
        assertNotNull(expectedProject);

        read("test-custom-action-only-required", expectedProject);
    }

	@Test
	public void readCustomActionRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getCustomActionRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-custom-action-required-and-optional", expectedProject);
	}

	@Test
    public void writeCustomActionOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getCustomActionOnlyRequired());
        assertNotNull(expectedProject);

        write("test-custom-action-only-required", expectedProject);
    }

	@Test
	public void writeCustomActionRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getCustomActionRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-custom-action-required-and-optional", expectedProject);
	}


	private CustomAction getCustomActionOnlyRequired() {
		return CustomAction.builder()
				.name("sql action")
				.type("SQL")
				.build();
	}

	private CustomAction getCustomActionRequiredAndOptional() {
		return CustomAction.builder()
				.name("sql action")
				.description("This is a SQL action")
				.type("SQL")
				.addParameters(
						CustomActionParameter.builder()
								.name("connectionURL")
								.value("jdbc:mysql://localhost:3306/")
								.build(),
						CustomActionParameter.builder()
								.name("connection.password")
								.value("myPassword")
								.type(PASSWORD)
								.build()
				)
				.asRequest(true)
				.libraryPath(Path.of("/path/to/library"))
				.build();
	}
}
