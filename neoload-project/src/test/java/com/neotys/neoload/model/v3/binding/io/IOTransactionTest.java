package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import com.neotys.neoload.model.v3.project.userpath.Step;
import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;


public class IOTransactionTest extends AbstractIOElementsTest {

	private static Step getTransactionOnlyRequired() {
		return Container.builder()
				.name("MyTransaction")
				.addSteps(Delay.builder().value("1000")
						.build())
				.build();
	}

	private static Step getTransactionRequiredAndOptional() {
		return Container.builder()
				.name("MyTransaction")
				.description("My Transaction")
				.slaProfile("MySlaProfile")
				.addSteps(Delay.builder().value("1000")
						.build())
				.addAssertions(ContentAssertion.builder()
						.contains("MyUserPath_actions_MyTransaction")
						.build())
				.build();
	}

	@Test
	public void readTransactionOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getTransactionOnlyRequired());
		assertNotNull(expectedProject);

		read("test-transaction-only-required", expectedProject);
	}

	@Test
	public void readTransactionRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getTransactionRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-transaction-required-and-optional", expectedProject);
	}
	
	@Test
	public void writeTransactionOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getTransactionOnlyRequired());
		assertNotNull(expectedProject);

		write("test-transaction-only-required", expectedProject);
	}

	@Test
	public void writeTransactionRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getTransactionRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-transaction-required-and-optional", expectedProject);
	}
}
