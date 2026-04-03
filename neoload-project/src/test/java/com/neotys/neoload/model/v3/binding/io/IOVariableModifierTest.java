package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.VariableModifier;


public class IOVariableModifierTest extends AbstractIOElementsTest {

	@Test
	public void readVariableModifier() throws IOException {
		final Project expectedProject = buildProjectContainingVariableModifier();
		assertNotNull(expectedProject);

		read("test-variable-modifier", expectedProject);
	}

	private Project buildProjectContainingVariableModifier() {
		// defined / next_value
		final VariableModifier definedNextValue = VariableModifier.builder()
				.category(VariableModifier.Category.DEFINED)
				.variableName("myVariable")
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.build();

		// defined / init_value
		final VariableModifier definedInitValue = VariableModifier.builder()
				.category(VariableModifier.Category.DEFINED)
				.variableName("myVariable")
				.mode(VariableModifier.Mode.INIT_VALUE)
				.build();

		// shared / add_value
		final VariableModifier sharedAddValue = VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.sharedVariableName("mySharedVar")
				.mode(VariableModifier.Mode.ADD_VALUE)
				.srcVariableName("${localVar}")
				.build();

		// shared / get_value
		final VariableModifier sharedGetValue = VariableModifier.builder()
				.category(VariableModifier.Category.SHARED)
				.sharedVariableName("mySharedVar")
				.mode(VariableModifier.Mode.GET_VALUE)
				.destVariableName("localVar")
				.build();

		// default category (defined) / next_value
		final VariableModifier defaultCategory = VariableModifier.builder()
				.variableName("myVariable")
				.mode(VariableModifier.Mode.NEXT_VALUE)
				.build();

		final Container container = Container.builder()
				.name("actions")
				.addSteps(definedNextValue, definedInitValue, sharedAddValue, sharedGetValue, defaultCategory)
				.build();

		final UserPath userPath = UserPath.builder()
				.name("user_path_1")
				.actions(container)
				.build();
		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}
}