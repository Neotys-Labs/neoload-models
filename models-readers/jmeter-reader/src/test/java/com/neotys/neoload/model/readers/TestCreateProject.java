package com.neotys.neoload.model.readers;

import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import org.junit.Test;

public class TestCreateProject {

    @Test
    public void test() {

        ImmutableRequest r1 = Request.builder()
                .name("POST url form encoded")
                .method("PUT")
                .url("https://www.server.com/ajax/multiple?session=${session}&columns=toto")
                .body("toto=oui")
                .addHeaders(Header.builder().name("Content-Type").value("application/x-www-form-urlencoded; charset=UTF-8").build())
                .build();

        ImmutableRequest r2 = Request.builder()
                .name("POST javascript text")
                .method("PUT")
                .url("https://www.server.com/ajax/multiple?session=${session}&columns=toto")
                .body("{\"id\": \"OriginalView\", \"label\": \"Original View\"}")
                .addHeaders(Header.builder().name("Content-Type").value("text/javascript; charset=UTF-8").build())
                .build();

        ImmutableContainer container = Container.builder()
                .name("a")
                .addSteps(r1,r2)
                .build();

        ImmutableUserPath up = UserPath.builder()
                .name("up")
                .actions(container)
                .build();

        Project project = Project.builder().name("aaaa")
                .addUserPaths(up)
                .build();

        NeoLoadWriter neoLoadWriter = new NeoLoadWriter(ImmutableProject.copyOf(project).withName("test"),"C:/Users/tmartinez/Desktop/my-test-project",null);
        neoLoadWriter.write(true);
    }

}
