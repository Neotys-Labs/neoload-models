package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

import org.junit.Test;

import java.io.IOException;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertNotNull;

public class IOSwitchTest extends AbstractIOElementsTest  {

    private static Switch getSwitchOnlyRequired() {
        return Switch.builder()
        		.value("${MySwitchVariable}")
				.addCases(Case.builder()
						.value("0")
						.isBreak(true)
						.addSteps(Delay.builder()
								.value("1000")
								.build())
						.build())
                .getDefault(Container.builder()
                		.addSteps(Delay.builder()
                				.value("3000")
                				.build())
                		.build())                
                .build();
    }
    
    private static Switch getSwitchRequiredAndOptional() {
        return Switch.builder()
        		.name("MySwitch")
                .description("MySwitchDescription")
                .value("${MySwitchVariable}")
                .addCases(Case.builder()
                		.value("0")
                        .isBreak(true)
                        .addSteps(Delay.builder()
                        		.value("1000")
                        		.build())
                        .build())
				.addCases(Case.builder()
						.description("MyCase1Description")
						.value("1")
						.isBreak(false)
						.addSteps(Delay.builder()
								.value("1000")
								.build())
						.addAssertions(ContentAssertion.builder()
								.contains("MyCase1Assertion on Content")
								.build())
						.build())
                .getDefault(Container.builder()
                		.addSteps(Delay.builder()
                				.value("3000")
                				.build())
                		.build())                
                .build();
    }


    @Test
    public void readSwitchOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getSwitchOnlyRequired());
        assertNotNull(expectedProject);

        read("test-switch-only-required", expectedProject);
    }
    
    @Test
    public void readSwitchRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProject(getSwitchRequiredAndOptional());
        assertNotNull(expectedProject);

        read("test-switch-required-and-optional", expectedProject);
    }

    @Test
    public void writeSwitchOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getSwitchOnlyRequired());
        assertNotNull(expectedProject);

        write("test-switch-only-required", expectedProject);
    }

    @Test
    public void writeSwitchRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProject(getSwitchRequiredAndOptional());
        assertNotNull(expectedProject);

        write("test-switch-required-and-optional", expectedProject);
    }

}
