package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class IOSwitchTest extends AbstractIOElementsTest  {

    private static Project getSwitchOnlyRequired() {
        final Switch aSwitch = Switch.builder()
                .description("a simple switch")
                .name("switch")
                .addCases(Case.builder()
                        .addSteps(Delay.builder().value("3").build())
                        .value("0")
                        .isBreak(true)
                        .build())
                .getDefault(Container.builder().addSteps(Delay.builder().value("3").build()).build())
                .value("1")
                .build();
        final UserPath userPath = UserPath.builder()
                .name("MyUserPath")
                .actions(Container.builder()
                        .name("actions")
                        .addSteps(aSwitch)
                        .build())
                .build();

        final Project project = Project.builder()
                .name("MyProject")
                .addUserPaths(userPath)
                .build();

        return project;
    }

    @Test
    public void readIfOnlyRequired() throws IOException {
        final Project expectedProject = getSwitchOnlyRequired();
        assertNotNull(expectedProject);

        read("test-switch-only-required", expectedProject);
    }
}
