package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class IOSwitchTest extends AbstractIOElementsTest  {

    private static Project getSwitchOnlyRequired() {
        final Switch aSwitch = Switch.builder()
        		.value("1")
                .addCases(Case.builder()
                		.value("0")
                        .isBreak(true)
                        .addSteps(Delay.builder()
                        		.value("3")
                        		.build())                        
                        .build())
                .getDefault(Container.builder()
                		.addSteps(Delay.builder()
                				.value("3")
                				.build())
                		.build())                
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
    
    private static Project getSwitchRequiredAndOptional() {
        final Switch aSwitch = Switch.builder()
        		.name("MySwitch")
                .description("My Switch")    
                .value("0")
                .addCases(Case.builder()
                		.name("MyCase0")
                		.description("My Case 0")
                		.value("0")
                        .isBreak(false)
                        .addSteps(Delay.builder()
                        		.value("3")
                        		.build())
                        .addAssertions(ContentAssertion.builder()
                        		.contains("My Assertion on Content")
                        		.build())
                        .build())
                .getDefault(Container.builder()
                		.addSteps(Delay.builder()
                				.value("3")
                				.build())
                		.build())                
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
    public void readSwitchOnlyRequired() throws IOException {
        final Project expectedProject = getSwitchOnlyRequired();
        assertNotNull(expectedProject);

        read("test-switch-only-required", expectedProject);
    }
    
    @Test
    public void readSwitchRequiredAndOptional() throws IOException {
        final Project expectedProject = getSwitchRequiredAndOptional();
        assertNotNull(expectedProject);

        read("test-switch-required-and-optional", expectedProject);
    }

}
