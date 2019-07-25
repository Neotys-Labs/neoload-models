package com.neotys.neoload.model.readers;

import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import org.junit.Test;

public class TestCreateProject {

    @Test
    public void test() {

        ImmutableRequest build = Request.builder()
                .method("POST")
                .body("toto=oui")
                .build();

        ImmutableContainer container = Container.builder()
                .name("a")
                .addSteps(build)
                .build();

        ImmutableUserPath up = UserPath.builder()
                .name("up")
                .actions(container)
                .build();

        Project project = Project.builder().name("aaaa")
                .addUserPaths(up)
                .build();

        NeoLoadWriter neoLoadWriter = new NeoLoadWriter(ImmutableProject.copyOf(project).withName("test"),"C:/Users/tmartinez/Desktop",null);
        neoLoadWriter.write(true);
    }

}
