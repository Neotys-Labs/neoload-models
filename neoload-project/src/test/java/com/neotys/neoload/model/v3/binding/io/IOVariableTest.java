package com.neotys.neoload.model.v3.binding.io;


import static com.google.common.collect.Lists.newArrayList;
import static com.neotys.neoload.model.v3.project.variable.ChangePolicyVariable.ChangePolicy.*;
import static com.neotys.neoload.model.v3.project.variable.OrderVariable.Order.*;
import static com.neotys.neoload.model.v3.project.variable.OutOfValueVariable.OutOfValue.*;
import static com.neotys.neoload.model.v3.project.variable.ScopeVariable.Scope.*;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.*;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
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

        final Variable passwordVariable = PasswordVariable.builder()
                .name("MyPassword")
                .value("s3cr3t")
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

        final Variable randomStringVariable = RandomStringVariable.builder()
                .name("MyRandomString")
                .minLength(5)
                .maxLength(20)
                .isPredictable(false)
                .changePolicy(EACH_USE)
                .build();

        final Variable randomUUIDVariable = RandomUUIDVariable.builder()
                .name("MyRandomUUID")
                .build();

        final Variable listVariable = ListVariable.builder()
                .name("MyList")
                .addColumnNames("city", "country")
                .addValues(newArrayList("Paris", "France"), newArrayList("London", "UK"))
                .order(SEQUENTIAL)
                .build();

        final Variable listVariable2 = ListVariable.builder()
                .name("MyList2")
                .description("list variable description")
                .addColumnNames("city", "country")
                .addValues(newArrayList("Paris", "France"), newArrayList("London", "UK"))
                .startFromLine(2)
                .changePolicy(EACH_USER)
                .scope(UNIQUE)
                .order(RANDOM)
                .outOfValue(STOP)
                .build();

        final Variable sqlVariable = SqlVariable.builder()
                .name("MySqlVar")
                .driver("com.mysql.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/mydb")
                .login("admin")
                .password("pass")
                .query("SELECT username, email FROM users")
                .addColumnNames("username", "email")
                .order(SEQUENTIAL)
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

        final Variable secretVaultVariable = SecretVaultVariable.builder()
                .name("db_password")
                .providerId("665f1a2b3c4d5e6f7a8b9c0d")
                .secretId("my-app/db")
                .build();

        return Project.builder()
                .name("MyProject")
                .addVariables(constantVariable, passwordVariable, fileVariable, fileVariable2, counterVariable, randomNumberVariable, randomStringVariable,
                        randomUUIDVariable, listVariable, listVariable2, sqlVariable, javaScriptVariable, secretVaultVariable)
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
                        .offset(Offset.parse("5m").get())
                        .build())
                .build();
    }

    @Test
    public void readCurrentDateVariableAcceptsEveryValidOffset() throws IOException {
        final ProjectDescriptor descriptor = new IO().read(getFile("test-current-date-variable-offsets-valid", "yaml"));

        final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
        assertTrue(validation.getMessage().orElse(""), validation.isValid());
    }

    @Test
    public void readCurrentDateVariableReadsEveryUnitAndKeepsNegativeAmounts() throws IOException {
        final String[] offsets = {"-5d", "3ms", "10s", "2m", "4h", "1mo", "2y"};
        final int[] amounts = {-5, 3, 10, 2, 4, 1, 2};
        final IncrementTimeUnit[] units = {IncrementTimeUnit.DAY, IncrementTimeUnit.MILLISECOND, IncrementTimeUnit.SECOND, IncrementTimeUnit.MINUTE,
                IncrementTimeUnit.HOUR, IncrementTimeUnit.MONTH, IncrementTimeUnit.YEAR};

        for (int i = 0; i < offsets.length; i++) {
            final Project project = new IO().read(currentDateYaml(offsets[i])).getProject();

            final Offset offset = ((CurrentDateVariable) project.getVariables().get(0)).getOffset().get();
            assertEquals(offsets[i], amounts[i], offset.getAmount());
            assertEquals(offsets[i], units[i], offset.getUnit());
        }
    }

    @Test
    public void readCurrentDateVariableFailsTheReadOnEveryInvalidOffset() throws IOException {
        final String fixture = new String(Files.readAllBytes(getFile("test-current-date-variable-offsets-invalid", "yaml").toPath()), StandardCharsets.UTF_8);
        final String[] variables = fixture.split("(?m)^(?=- current_date:)");

        for (final String variable : Arrays.copyOfRange(variables, 1, variables.length)) {
            final String yaml = "name: MyProject\nvariables:\n" + variable;
            final String message = assertThrows(yaml, JsonMappingException.class, () -> new IO().read(yaml)).getMessage();
            assertTrue(message, message.startsWith("Incorrect offset: '"));
            assertTrue(message, message.contains("[\"offset\"]"));
        }
    }

    @Test
    public void readCurrentDateVariableNamesTheInvalidOffsetInTheError() {
        for (final String offset : new String[] {"5x", "abc", "1h30m", "99999999999d"}) {
            final String message = assertThrows(JsonMappingException.class, () -> new IO().read(currentDateYaml(offset))).getMessage();
            assertTrue(message, message.contains("'" + offset + "'"));
            assertTrue(message, message.contains("[\"offset\"]"));
        }
    }

    @Test
    public void currentDateOffsetIsWrittenBackUnchanged() throws IOException {
        final ProjectDescriptor descriptor = new IO().read(currentDateYaml("-5d"));

        final String written = new IO().write(descriptor, IO.Format.YAML);

        assertTrue(written, written.contains("offset: -5d"));
        assertEquals(descriptor, new IO().read(written));
    }

    private static String currentDateYaml(final String offset) {
        return "name: P\nvariables:\n- current_date:\n    name: D\n    offset: " + offset + "\n";
    }

    @Test
    public void readDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalDateVariable();
        assertNotNull(expectedProject);

        read("test-date-variable-only-required", expectedProject);
    }

    @Test
    public void writeDateVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectWithMinimalDateVariable();
        assertNotNull(expectedProject);

        write("test-date-variable-only-required", expectedProject);
    }

    @Test
    public void readDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullDateVariable();
        assertNotNull(expectedProject);

        read("test-date-variable-required-and-optional", expectedProject);
    }

    @Test
    public void writeDateVariableRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProjectWithFullDateVariable();
        assertNotNull(expectedProject);

        write("test-date-variable-required-and-optional", expectedProject);
    }

    private Project buildProjectWithMinimalDateVariable() {
        return Project.builder()
                .name("MyProject")
                .addVariables(DateVariable.builder()
                        .name("MyDate")
                        .startDate("01/01/2024 00:00:00")
                        .build())
                .build();
    }

    private Project buildProjectWithFullDateVariable() {
        return Project.builder()
                .name("MyProject")
                .addVariables(DateVariable.builder()
                        .name("MyDate")
                        .description("start of year plus 5 minutes")
                        .pattern("yyyy-MM-dd'T'HH:mm:ss")
                        .startDate("2024-01-01T00:00:00")
                        .changeStep(Offset.parse("5m").get())
                        .changePolicy(EACH_USE)
                        .scope(LOCAL)
                        .build())
                .build();
    }

    @Test
    public void readDateVariableFailsTheReadOnInvalidChangeStep() {
        final String message = assertThrows(JsonMappingException.class, () -> new IO().read(getFile("test-date-variable-invalid-change-step", "yaml"))).getMessage();

        assertTrue(message, message.contains("'bogus'"));
        assertTrue(message, message.contains("[\"change_step\"]"));
    }

    @Test
    public void readDateVariableReadsANegativeChangeStepAndWritesItBack() throws IOException {
        final ProjectDescriptor descriptor = new IO().read("name: P\nvariables:\n- date:\n    name: D\n    start_date: 01/01/2024 00:00:00\n    change_step: -5d\n");

        final Offset changeStep = ((DateVariable) descriptor.getProject().getVariables().get(0)).getChangeStep().get();
        assertEquals(-5, changeStep.getAmount());
        assertEquals(IncrementTimeUnit.DAY, changeStep.getUnit());
        final String written = new IO().write(descriptor, IO.Format.YAML);
        assertTrue(written, written.contains("change_step: -5d"));
        assertEquals(descriptor, new IO().read(written));
    }

    @Test
    public void readDateVariableRejectsMismatchedStartDate() throws IOException {
        final ProjectDescriptor descriptor = new IO().read(getFile("test-date-variable-mismatched-start-date", "yaml"));

        final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
        assertFalse(validation.isValid());
        assertTrue(validation.getMessage().get(), validation.getMessage().get().contains("variables[0]"));
    }
}
