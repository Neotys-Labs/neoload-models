package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable;
import com.neotys.neoload.model.v3.project.variable.DatePatternElement;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;


public class IOCurrentDateVariableTest extends AbstractIOElementsTest {

    @Test
    public void readCurrentDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalCurrentDateVariable();
        assertNotNull(expectedProject);
        read("test-currentDateVariable-only-required", expectedProject);
    }

    @Test
    public void writeCurrentDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalCurrentDateVariable();
        assertNotNull(expectedProject);
        write("test-currentDateVariable-only-required", expectedProject);
    }

    @Test
    public void readCurrentDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullCurrentDateVariable();
        assertNotNull(expectedProject);
        read("test-currentDateVariable-required-and-optional", expectedProject);
    }

    @Test
    public void writeCurrentDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullCurrentDateVariable();
        assertNotNull(expectedProject);
        write("test-currentDateVariable-required-and-optional", expectedProject);
    }

    private Project buildProjectWithMinimalCurrentDateVariable() {
        return Project.builder()
                .name("MyProject")
                .addVariables(CurrentDateVariable.builder()
                        .name("MyCurrentDate")
                        .build())
                .build();
    }

    private Project buildProjectWithFullCurrentDateVariable() {
        return Project.builder()
                .name("MyProject")
                .addVariables(CurrentDateVariable.builder()
                        .name("MyCurrentDate")
                        .description("now plus 5 minutes")
                        .pattern("yyyy-MM-dd'T'HH:mm:ss")
                        .incrementTimeUnit(DatePatternElement.IncrementTimeUnit.MINUTE)
                        .incrementValue(5)
                        .build())
                .build();
    }
}
