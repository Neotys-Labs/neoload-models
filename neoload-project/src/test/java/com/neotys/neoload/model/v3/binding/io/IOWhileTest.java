package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;


public class IOWhileTest extends AbstractIOElementsTest {

    private static Project getIfOnlyRequired() {
        final UserPath userPath = UserPath.builder()
                .name("MyUserPath")
                .actions(Container.builder()
                        .name("actions")
                        .addSteps(While.builder()
                                .name("My While")
                                .description("My description")
                                .conditions(getConditions())
                                .addSteps(
                                        Request.builder()
                                                .url("http://www.neotys.com/select")
                                                .build()
                                ).build()
                        ).build()
                ).build();

        final Project project = Project.builder()
                .name("MyProject")
                .addUserPaths(userPath)
                .build();

        return project;
    }

    private static final List<Condition> getConditions() {
        return Arrays.asList(
                getCondition("operand1", Condition.Operator.EQUALS, "operand2"),
                getCondition("0", Condition.Operator.EQUALS, "operand2"),
                getCondition("operand1", Condition.Operator.NOT_EQUALS, "${parameter}"),
                getCondition("${parameter}", Condition.Operator.NOT_EQUALS, "10"),
                getCondition("${parameter}", Condition.Operator.CONTAINS, "contains"),
                getCondition("operand1", Condition.Operator.NOT_CONTAINS, "operand2"),
                getCondition("operand1", Condition.Operator.STARTS_WITH, "=="),
                getCondition("operand1", Condition.Operator.NOT_STARTS_WITH, "operand2"),
                getCondition("operand1", Condition.Operator.ENDS_WITH, "operand2"),
                getCondition("operand1", Condition.Operator.NOT_ENDS_WITH, "operand2"),
                getCondition("operand1", Condition.Operator.MATCH_REGEXP, "operand2"),
                getCondition("operand1", Condition.Operator.NOT_MATCH_REGEXP, "operand2"),
                getCondition("operand1", Condition.Operator.GREATER, "operand2"),
                getCondition("operand1", Condition.Operator.GREATER, "operand2"),
                getCondition("operand1", Condition.Operator.GREATER_EQUAL, "operand2"),
                getCondition("operand1", Condition.Operator.GREATER_EQUAL, "operand2"),
                getCondition("operand1", Condition.Operator.LESS, "operand2"),
                getCondition("operand1", Condition.Operator.LESS, "operand2"),
                getCondition("operand1", Condition.Operator.LESS_EQUAL, "operand2"),
                getCondition("operand1", Condition.Operator.LESS_EQUAL, "operand2"),
                getCondition("operand1", Condition.Operator.EXISTS),
                getCondition("operand1", Condition.Operator.NOT_EXISTS),
                getCondition("${myVariable1}", Condition.Operator.EQUALS, "value"),
                getCondition("${myVariable1}", Condition.Operator.EQUALS, "value"),
                getCondition("${myVariable1}", Condition.Operator.NOT_EQUALS, "5"),
                getCondition("${myVariable2}", Condition.Operator.NOT_EXISTS),
                getCondition("${myVariable3}", Condition.Operator.EXISTS),
                getCondition("${myVariable4}", Condition.Operator.EQUALS, ""),
                getCondition("${myVariable6}", Condition.Operator.EQUALS, "value'with'simple'quote"),
                getCondition("${myVariable7}", Condition.Operator.EQUALS, "value\"with\"double\"quote"),
                getCondition("", Condition.Operator.EQUALS, "")
        );
    }

    private static final Condition getCondition(final String operand1, final Condition.Operator operator, final String operand2) {
        return Condition
                .builder()
                .operand1(operand1)
                .operator(operator)
                .operand2(operand2)
                .build();
    }

    private static final Condition getCondition(final String operand1, final Condition.Operator operator) {
        return Condition
                .builder()
                .operand1(operand1)
                .operator(operator)
                .build();
    }

    @Test
    public void readIfOnlyRequired() throws IOException {
        final Project expectedProject = getIfOnlyRequired();
        assertNotNull(expectedProject);

        read("test-while-only-required", expectedProject);
    }
}
