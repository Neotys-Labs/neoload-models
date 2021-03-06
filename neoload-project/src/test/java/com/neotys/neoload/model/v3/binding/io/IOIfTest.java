package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.Match;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;


public class IOIfTest extends AbstractIOElementsTest {

	private static Project getIfOnlyRequired() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(If.builder()
								.conditions(Arrays.asList(Condition
										.builder()
										.operand1("${variable}")
										.operator(Condition.Operator.EQUALS)
										.operand2("2")
										.build(),Condition
										.builder()
										.operand1("operand1")
										.operator(Condition.Operator.EQUALS)
										.operand2("operand2")
										.build()))
								.then(Container.builder()
										.name("container")
										.addSteps(Delay
												.builder()
												.value(String.valueOf(3*60*1000+200)) // "3m 200ms"
												.build()
										).build())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	private static Project getIfRequiredAndOptional() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(If.builder()
								.name("My If-Then-Else")
								.description("My description")
								.conditions(getConditions())
								.match(Match.ANY)
								.then(getThen())
								.getElse(getElse())
								.build())
						.build())
				.build();

		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();

		return project;
	}

	private static final List<Condition> getConditions(){
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

	private static final Condition getCondition(final String operand1, final Condition.Operator operator, final String operand2){
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.operand2(operand2)
				.build();
	}

	private static final Condition getCondition(final String operand1, final Condition.Operator operator){
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.build();
	}

	private static Container getThen() {
		return Container.builder()
				.name("container")
				.description("My then description")
				.slaProfile("MySLAProfile")
				.addSteps(Request
						.builder()
						.url("http://www.neotys.com/select")
						.build())
				.addAssertions(ContentAssertion.builder()
						.contains("ThenAssertion")
						.build())
				.build();
	}

	private static Container getElse() {
		return Container.builder()
				.name("container")
				.description("My else description")
				.slaProfile("MySLAProfile")
				.addSteps(Delay
						.builder()
						.value(String.valueOf(3*60*1000+200)) // "3m 200ms"
						.build())
				.addAssertions(ContentAssertion.builder()
						.contains("ElseAssertion")
						.build())
				.build();
	}

	@Test
	public void readIfOnlyRequired() throws IOException {
		final Project expectedProject = getIfOnlyRequired();
		assertNotNull(expectedProject);

		read("test-if-only-required", expectedProject);
	}

	@Test
	public void readIfRequiredAndOptional() throws IOException {
		final Project expectedProject = getIfRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-if-required-and-optional", expectedProject);
	}
}
