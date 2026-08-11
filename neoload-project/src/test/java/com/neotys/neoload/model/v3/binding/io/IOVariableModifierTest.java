package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.VariableModifier;
import java.io.IOException;
import org.junit.Test;

public class IOVariableModifierTest extends AbstractIOElementsTest {

	@Test
	public void read_VariableModifier_onlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingVariableModifier_onlyRequired();
		assertNotNull(expectedProject);

		read("test-variable-modifier-only-required", expectedProject);
	}

	@Test
	public void read_VariableModifier_requiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingVariableModifier_requiredAndOptional();
		assertNotNull(expectedProject);

		read("test-variable-modifier-required-and-optional", expectedProject);
	}

	@Test
	public void write_VariableModifier_onlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingVariableModifier_onlyRequired();
		assertNotNull(expectedProject);

		write("test-variable-modifier-only-required", expectedProject);
	}

	@Test
	public void write_VariableModifier_requiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingVariableModifier_requiredAndOptional();
		assertNotNull(expectedProject);

		write("test-variable-modifier-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingVariableModifier_onlyRequired() {
		// default category / default mode
		final VariableModifier definedNextValue = VariableModifier.builder()
				.variableName("MyVariableToModify")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(definedNextValue)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(container)
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private Project buildProjectContainingVariableModifier_requiredAndOptional() {
		// predefined / init_value
		final VariableModifier predefinedInitValue = VariableModifier.builder()
				.name("MyVariableModifier")
				.description("MyVariableModifierDescription")
				.category(VariableModifier.Category.PREDEFINED)
				.mode(VariableModifier.Mode.INIT_VALUE)
				.variableName("MyVariableToModify")
				.build();

		// shared_queue / add_shared_queue_value / value is a plain string
		final VariableModifier sharedAddValueString = VariableModifier.builder()
				.name("MyVariableModifier")
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
				.variableName("MyVariableToModify")
				.value("HelloWorld")
				.build();

		// shared_queue / add_shared_queue_value / value is a variable reference
		final VariableModifier sharedAddValueVariable = VariableModifier.builder()
				.name("MyVariableModifier")
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.ADD_SHARED_QUEUE_VALUE)
				.variableName("MyVariableToModify")
				.value("${MyVariable}")
				.build();

		// shared_queue / poll_shared_queue / value is an existing variable name
		final VariableModifier sharedPollValueExisting = VariableModifier.builder()
				.name("MyVariableModifier")
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
				.variableName("MyVariableToModify")
				.value("MyVariable")
				.build();

		// shared_queue / poll_shared_queue / value is a new variable to create
		final VariableModifier sharedPollValueNew = VariableModifier.builder()
				.name("MyVariableModifier")
				.category(VariableModifier.Category.SHARED_QUEUE)
				.mode(VariableModifier.Mode.POLL_SHARED_QUEUE)
				.variableName("MyVariableToModify")
				.value("MyVariableToCreate")
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(predefinedInitValue, sharedAddValueString, sharedAddValueVariable, sharedPollValueExisting, sharedPollValueNew)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(container)
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}