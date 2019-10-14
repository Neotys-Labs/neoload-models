package com.neotys.neoload.model.readers;

import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import org.junit.Test;

public class TestCreateProject {

    @Test
    public void test() {

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

        NeoLoadWriter neoLoadWriter = new NeoLoadWriter( ImmutableProject.copyOf(project).withName("test"), System.getProperty("user.dir"));
        neoLoadWriter.write(true);
    }

}
