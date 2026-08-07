package com.neotys.neoload.model.v3.binding.io;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.ChangePolicyElement;
import com.neotys.neoload.model.v3.project.variable.DatePatternElement;
import com.neotys.neoload.model.v3.project.variable.DateVariable;
import com.neotys.neoload.model.v3.project.variable.ScopeElement;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;


public class IODateVariableTest extends AbstractIOElementsTest {

    @Test
    public void readDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalDateVariable();
        assertNotNull(expectedProject);
        read("test-dateVariable-only-required", expectedProject);
    }

    @Test
    public void writeDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalDateVariable();
        assertNotNull(expectedProject);
        write("test-dateVariable-only-required", expectedProject);
    }

    @Test
    public void readDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullDateVariable();
        assertNotNull(expectedProject);
        read("test-dateVariable-required-and-optional", expectedProject);
    }

    @Test
    public void writeDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullDateVariable();
        assertNotNull(expectedProject);
        write("test-dateVariable-required-and-optional", expectedProject);
    }

    private Project buildProjectWithMinimalDateVariable() {
        return Project.builder()
                .name("MyProject")
                .addVariables(DateVariable.builder()
                        .name("MyDate")
                        .startDate("24/07/2026 17:55:30")
                        .build())
                .build();
    }

    private Project buildProjectWithFullDateVariable() {
        return Project.builder()
                .name("MyProject")
                .addVariables(DateVariable.builder()
                        .name("MyDate")
                        .description("MyDateDescription")
                        .pattern("dd/MM/yyyy HH:mm:ss.S")
                        .startDate("24/07/2026 17:55:30.500")
                        .incrementValue(1)
                        .incrementTimeUnit(DatePatternElement.IncrementTimeUnit.MINUTE)
                        .changePolicy(ChangePolicyElement.ChangePolicy.EACH_ITERATION)
                        .scope(ScopeElement.Scope.LOCAL)
                        .build())
                .build();
    }
}