package com.neotys.neoload.model.v3.binding.io;


import static com.google.common.collect.Lists.newArrayList;
import static com.neotys.neoload.model.v3.project.variable.ChangePolicyVariable.ChangePolicy.*;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.Order.*;
import static com.neotys.neoload.model.v3.project.variable.OutOfValueVariable.OutOfValue.*;
import static com.neotys.neoload.model.v3.project.variable.ScopeVariable.Scope.*;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.*;
import java.io.IOException;
import org.junit.Test;

public class IOVariableTest extends AbstractIOElementsTest {

    @Test
    public void readVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectContainingVariable();
        assertNotNull(expectedProject);

        read("test-variable-only-required", expectedProject);
    }

    @Test
    public void writeVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectContainingVariable();
        assertNotNull(expectedProject);

        write("test-variable-only-required", expectedProject);
    }

    private Project buildProjectContainingVariable() {

        final Variable constantVariable = ConstantVariable.builder()
                .name("constant_variable")
                .value("118218")
                .build();

        final Variable fileVariable = FileVariable.builder()
                .name("cities_file")
                .description("cities variable file description")
                .columnNames(newArrayList("City", "Country", "Population", "Longitude", "Latitude"))
                .isFirstLineColumnNames(false)
                .startFromLine(5)
                .delimiter(";")
                .path("data/list_of_cities.csv")
                .changePolicy(EACH_USER)
                .scope(UNIQUE)
                .order(SEQUENTIAL)
                .outOfValue(STOP)
                .build();

        final Variable fileVariable2 = FileVariable.builder()
                .name("cities2_file")
                .description("cities2 variable file description")
                .isFirstLineColumnNames(true)
                .startFromLine(1)
                .delimiter(";")
                .path("data/list_of_cities.csv")
                .changePolicy(EACH_PAGE)
                .scope(LOCAL)
                .order(RANDOM)
                .outOfValue(NO_VALUE)
                .build();

        final Variable counterVariable = CounterVariable.builder()
                .name("My Counter")
                .start(0)
                .end(1)
                .increment(10)
                .changePolicy(EACH_ITERATION)
                .scope(LOCAL)
                .outOfValue(CYCLE)
                .build();

        final Variable randomNumberVariable = RandomNumberVariable.builder()
                .name("MyRandomNumber")
                .min(9999)
                .max(-1)
                .isPredictable(false)
                .changePolicy(EACH_REQUEST)
                .build();

        final JavaScriptVariable javaScriptVariable = JavaScriptVariable.builder()
                .name("My JSVar")
                .description("This is a js var")
                .script("function evaluate() {\n" +
                        "\tlogger.debug(\"Computing value of js variable\");\n" +
                        "\treturn new function() {\n" +
                        "\t\tthis.firstField = \"a value\";\n" +
                        "\t\tthis.secondField = myLibraryFunction();\n" +
                        "\t};\n" +
                        "}")
                .changePolicy(EACH_ITERATION)
                .build();

        return Project.builder()
                .name("MyProject")
                .addVariables(constantVariable, fileVariable, fileVariable2, counterVariable, randomNumberVariable, javaScriptVariable)
                .build();
    }

    @Test
    public void readCurrentDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalCurrentDateVariable();
        assertNotNull(expectedProject);

        read("test-current-date-variable-only-required", expectedProject);
    }

    @Test
    public void writeCurrentDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalCurrentDateVariable();
        assertNotNull(expectedProject);

        write("test-current-date-variable-only-required", expectedProject);
    }

    @Test
    public void readCurrentDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullCurrentDateVariable();
        assertNotNull(expectedProject);

        read("test-current-date-variable-required-and-optional", expectedProject);
    }

    @Test
    public void writeCurrentDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullCurrentDateVariable();
        assertNotNull(expectedProject);

        write("test-current-date-variable-required-and-optional", expectedProject);
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
                        .incrementValue(5)
                        .incrementTimeUnit(DatePatternVariable.IncrementTimeUnit.MINUTE)
                        .build())
                .build();
    }

    @Test
    public void readCurrentDateVariableRejectsInvalidIncrementTimeUnit() {
        final String yaml = "name: MyProject\n"
                + "variables:\n"
                + "- current_date:\n"
                + "    name: MyCurrentDate\n"
                + "    increment_timeunit: fortnight\n";

        final InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> new IO().read(yaml));

        assertTrue(exception.getMessage().contains("increment_timeunit"));
    }
}
