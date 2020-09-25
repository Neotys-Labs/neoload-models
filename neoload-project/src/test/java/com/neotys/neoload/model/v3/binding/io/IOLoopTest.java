package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.UserPath;


public class IOLoopTest extends AbstractIOElementsTest {

    private static Project getLoopOnlyRequired() {
        final UserPath userPath = UserPath.builder()
                .name("MyUserPath")
                .actions(Container.builder()
                        .name("actions")
                        .addSteps(Loop.builder()
                                .name("looper")
                                .description("a simple loop")
                                .count("5")
                                .addSteps(Delay.builder()
                                        .value("200")
                                        .build())
                                .build())
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
        final Project expectedProject = getLoopOnlyRequired();
        assertNotNull(expectedProject);

        read("test-loop-only-required", expectedProject);
    }
}
