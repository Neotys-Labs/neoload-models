package com.neotys.neoload.model.readers;

import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testCreateProject() {
        ImmutableProject project = Project.builder()
                .name("project")
                .addUserPaths(createUp())
                .build();

        new NeoLoadWriter(project, "C:\\Users\\tmartinez\\Documents\\GitHub\\Convertisseur\\nlproject", Collections.emptyMap()).write(true);
        createUp();
    }

    private UserPath createUp() {
        return UserPath.builder()
                .name("My UP")
                .description("test")
                .actions(Container.builder()
                        .name("actions")
                        .addSteps(Container.builder()
                                .name("transaction")
                                .build())
                        .addSteps(Request.builder()
                                .method("GET")
                                .name("http request")
                                .server("https://totot.com:445")
                                .build())
                        .build())
                .description("test")
                .build();
    }

}