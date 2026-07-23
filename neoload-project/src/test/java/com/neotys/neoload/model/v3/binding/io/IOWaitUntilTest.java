package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Match;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;


public class IOWaitUntilTest extends AbstractIOElementsTest {

	@Test
	public void readWaitUntilOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getWaitUntilOnlyRequired());
		Assert.assertNotNull(expectedProject);

		read("test-waituntil-only-required", expectedProject);
	}

	@Test
	public void readWaitUntilRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getWaitUntilsRequiredAndOptional(true));
		Assert.assertNotNull(expectedProject);

		read("test-readonly-waituntil-required-and-optional", expectedProject);
	}

	@Test
	public void writeWaitUntilOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getWaitUntilOnlyRequired());
		Assert.assertNotNull(expectedProject);

		write("test-waituntil-only-required", expectedProject);
	}

	@Test
	public void writeWaitUntilRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getWaitUntilsRequiredAndOptional(false));
		Assert.assertNotNull(expectedProject);

		write("test-waituntil-required-and-optional", expectedProject);
	}

	private WaitUntil getWaitUntilOnlyRequired() {
		return WaitUntil.builder()
				.addConditions(Condition.builder()
						.operand1("${status_code}")
						.operator(Condition.Operator.EQUALS)
						.operand2("200")
						.build())
				.build();
	}

	private List<WaitUntil> getWaitUntilsRequiredAndOptional(final boolean readOnly) {
		final WaitUntil myWaitUntil0 = WaitUntil.builder()
				.name("MyWaitUntil0")
				.description("MyWaitUntil0Description")
				.addConditions(Condition.builder()
						.operand1("${status_code}")
						.operator(Condition.Operator.EQUALS)
						.operand2("200")
						.build())
				.match(Match.ALL)
				.timeout("30000")
				.build();
		final WaitUntil myWaitUntil1 = WaitUntil.builder()
				.name("MyWaitUntil1")
				.description("MyWaitUntil1Description")
				.addConditions(Condition.builder()
						.operand1("${status_code}")
						.operator(Condition.Operator.EQUALS)
						.operand2("200")
						.build())
				.match(Match.ALL)
				.timeout("30000")
				.build();
		final WaitUntil myWaitUntil2 = WaitUntil.builder()
				.name("MyWaitUntil2")
				.description("MyWaitUntil2Description")
				.addConditions(Condition.builder()
						.operand1("${status_code}")
						.operator(Condition.Operator.EQUALS)
						.operand2("200")
						.build())
				.match(Match.ALL)
				.timeout("${timeout}")
				.build();

		if (readOnly) {
			return List.of(myWaitUntil0, myWaitUntil1, myWaitUntil2);
		}
		return List.of(myWaitUntil1, myWaitUntil2);
	}
}