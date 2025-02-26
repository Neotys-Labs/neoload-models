package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.junit.Test;

import java.io.IOException;

import static com.neotys.neoload.model.v3.project.userpath.CustomActionParameter.Type.PASSWORD;
import static junit.framework.TestCase.assertNotNull;

public class IOCustomActionTest extends AbstractIOElementsTest {
    @Test
    public void readCustomActionOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectContainingCustomAction();
        assertNotNull(expectedProject);

        read("test-custom-action-only-required", expectedProject);
    }

    private Project buildProjectContainingCustomAction() {
        final CustomAction customAction1 = CustomAction.builder()
                .name("sql action")
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
                .build();

        final Container container = Container.builder()
                .name("actions")
                .addSteps(customAction1)
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
