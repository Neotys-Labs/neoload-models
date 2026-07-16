package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.*;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static com.neotys.neoload.model.v3.project.variable.Variable.ChangePolicy.*;
import static com.neotys.neoload.model.v3.project.variable.Variable.Order.*;
import static com.neotys.neoload.model.v3.project.variable.Variable.OutOfValue.*;
import static com.neotys.neoload.model.v3.project.variable.Variable.Scope.*;
import static junit.framework.TestCase.assertNotNull;


public class IOVariableTest extends AbstractIOElementsTest {

    @Test
    public void readVariableOnlyRequired() throws IOException {
        final Project expectedProject = buildProjectContainingVariable();
        assertNotNull(expectedProject);

        read("test-variable-only-required", expectedProject);
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

        final Variable randomStringVariable = RandomStringVariable.builder()
                .name("MyRandomString")
                .minLength(5)
                .maxLength(20)
                .isPredictable(false)
                .changePolicy(EACH_USE)
                .build();

        final Variable passwordVariable = PasswordVariable.builder()
                .name("MyPassword")
                .value("s3cr3t")
                .changePolicy(EACH_ITERATION)
                .build();

        final Variable dateVariable = DateVariable.builder()
                .name("MyDate")
                .pattern("yyyy-MM-dd")
                .startDate("2026-01-01")
                .incType(DateVariable.IncType.DAY)
                .incValue(1)
                .changePolicy(EACH_ITERATION)
                .build();

        final Variable minimalCurrentDateVariable = CurrentDateVariable.builder()
                .name("MyMinimalCurrentDate")
                .pattern("yyyy-MM-dd")
                .build();

        final Variable fullCurrentDateVariable = CurrentDateVariable.builder()
                .name("MyFullCurrentDate")
                .description("now plus 5 minutes")
                .pattern("yyyy-MM-dd'T'HH:mm:ss")
                .incType(DateVariable.IncType.MINUTE)
                .incValue(5)
                .changePolicy(EACH_USE)
                .build();

        final Variable listVariable = ListVariable.builder()
                .name("MyList")
                .addColumnNames("city", "country")
                .addValues(newArrayList("Paris", "France"), newArrayList("London", "UK"))
                .order(SEQUENTIAL)
                .build();

        final Variable sqlVariable = SqlVariable.builder()
                .name("MySqlVar")
                .query("SELECT username, email FROM users")
                .driver("com.mysql.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/mydb")
                .login("admin")
                .password("pass")
                .addColumnNames("username", "email")
                .order(SEQUENTIAL)
                .build();

        final Variable minimalRandomUuidVariable = RandomUUIDVariable.builder()
                .name("MyMinimalRandomUUID")
                .build();

        final Variable fullRandomUuidVariable = RandomUUIDVariable.builder()
                .name("MyFullRandomUUID")
                .description("Uppercase predictable UUID")
                .isUpperCase(true)
                .isPredictable(true)
                .changePolicy(EACH_USE)
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

        final SharedQueueVariable minimalSharedQueueVariable = SharedQueueVariable.builder()
                .name("MyMinimalSharedQueue")
                .build();

        final SharedQueueVariable fullSharedQueueVariable = SharedQueueVariable.builder()
                .name("MyFullSharedQueue")
                .description("A producer/consumer shared queue")
                .queueSize(5000)
                .consumerTimeout(2000L)
                .isSwapActivated(true)
                .swapFile("data/my_queue_swap.csv")
                .isSwapLoaded(true)
                .isSwapDump(false)
                .delimiter(",")
                .changePolicy(EACH_REQUEST)
                .build();

        final Variable secretVaultVariable = SecretVaultVariable.builder()
                .name("db_password")
                .providerId("665f1a2b3c4d5e6f7a8b9c0d")
                .secretIdentifier("my-app/db")
                .build();

        return Project.builder()
                .name("MyProject")
                .addVariables(constantVariable, fileVariable, fileVariable2, counterVariable, randomNumberVariable, randomStringVariable, passwordVariable, dateVariable, minimalCurrentDateVariable, fullCurrentDateVariable, listVariable, sqlVariable, minimalRandomUuidVariable, fullRandomUuidVariable, javaScriptVariable, minimalSharedQueueVariable, fullSharedQueueVariable, secretVaultVariable)
                .build();
    }
}
