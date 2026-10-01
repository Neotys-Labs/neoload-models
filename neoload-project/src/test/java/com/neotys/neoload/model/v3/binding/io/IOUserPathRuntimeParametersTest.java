package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.UserPath.FailurePolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

public class IOUserPathRuntimeParametersTest extends AbstractIOElementsTest {

	private static Container actions() {
		return Container.builder()
				.name("actions")
				.addSteps(Delay.builder().value("1000").build())
				.build();
	}

	private static Project getRuntimeParameters() {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath1")
						.onError(FailurePolicy.GO_TO_NEXT_ITERATION)
						.onAssertionFailure(FailurePolicy.STOP_AND_START_NEW_VU)
						.thinkTime(UserPathThinkTime.builder().override("5s").random("10%").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath2")
						.onError(FailurePolicy.DO_NOTHING)
						.onAssertionFailure(FailurePolicy.GO_TO_NEXT_ITERATION)
						.thinkTime(UserPathThinkTime.builder().factor("150%").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath3")
						.onAssertionFailure(FailurePolicy.DO_NOTHING)
						.thinkTime(UserPathThinkTime.builder().random("20%").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath4")
						.onError(FailurePolicy.STOP_AND_START_NEW_VU)
						.thinkTime(UserPathThinkTime.builder().override("1m 30s").build())
						.actions(actions())
						.build())
				.build();
	}

	private static Project getRuntimeParametersAsVariables() {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath1")
						.thinkTime(UserPathThinkTime.builder().override("${my_think_time}").random("${my_random_delay}").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath2")
						.thinkTime(UserPathThinkTime.builder().factor("${my_think_time_factor}").build())
						.actions(actions())
						.build())
				.build();
	}

	@Test
	public void readRuntimeParameters() throws IOException {
		final Project expectedProject = getRuntimeParameters();
		assertNotNull(expectedProject);

		read("test-userpaths-runtime-parameters", expectedProject);
	}

	@Test
	public void writeRuntimeParameters() throws IOException {
		final Project expectedProject = getRuntimeParameters();
		assertNotNull(expectedProject);

		write("test-userpaths-runtime-parameters", expectedProject);
	}

	@Test
	public void readRuntimeParametersAsVariables() throws IOException {
		final Project expectedProject = getRuntimeParametersAsVariables();
		assertNotNull(expectedProject);

		read("test-userpaths-runtime-parameters-variables", expectedProject);
	}

	@Test
	public void writeRuntimeParametersAsVariables() throws IOException {
		final Project expectedProject = getRuntimeParametersAsVariables();
		assertNotNull(expectedProject);

		write("test-userpaths-runtime-parameters-variables", expectedProject);
	}

	@Test
	public void readUserPathWithoutRuntimeParametersLeavesThemUnset() throws IOException {
		final UserPath userPath = new IO().read(getFile("test-userpaths-only-required", "yaml")).getProject().getUserPaths().get(0);

		assertFalse(userPath.getOnError().isPresent());
		assertFalse(userPath.getOnAssertionFailure().isPresent());
		assertFalse(userPath.getThinkTime().isPresent());
	}

	@Test
	public void readThinkTimeValuesAcceptedByThePatterns() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-userpaths-think-time-valid-values", "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void readThinkTimeValuesRejectedByThePatterns() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-userpaths-think-time-invalid-values", "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());

		final String message = validation.getMessage().get();
		assertTrue(message, message.contains("Violation Number: 13."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[0].think_time.override': must be a non-negative duration (e.g. 100 for 100 milliseconds, 5s, 1m 30s) or a variable."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[7].think_time.factor': must be a non-negative integer percentage (e.g. 150%) or a variable."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[10].think_time.random': must be a non-negative integer percentage (e.g. 150%) or a variable."));
	}

	@Test
	public void readThinkTimeWithOverrideAndFactorOrWithoutAnySettingIsRejected() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-userpaths-think-time-invalid-usage", "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());

		final String message = validation.getMessage().get();
		assertTrue(message, message.contains("Violation Number: 2."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[0].think_time': invalid attributes usage (override and factor cannot be used simultaneously)."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[1].think_time': invalid attributes usage (at least one of override, factor or random is required)."));
	}

	@Test
	public void readUnknownOnErrorPolicyFailsDeserialization() {
		assertReadFails("test-userpaths-invalid-on-error", "restart_vu", "do_nothing", "go_to_next_iteration", "stop_and_start_new_vu");
	}

	@Test
	public void readUnknownOnAssertionFailurePolicyFailsDeserialization() {
		assertReadFails("test-userpaths-invalid-on-assertion-failure", "Do_Nothing", "do_nothing", "go_to_next_iteration", "stop_and_start_new_vu");
	}

	@Test
	public void readUnknownThinkTimeSettingFailsDeserialization() {
		assertReadFails("test-userpaths-think-time-unknown-key", "random_delay");
	}

	private void assertReadFails(final String fixture, final String... expectedInMessage) {
		try {
			new IO().read(getFile(fixture, "yaml"));
			fail("Expected reading '" + fixture + "' to fail");
		} catch (final IOException e) {
			for (final String expected : expectedInMessage) {
				assertTrue(e.getMessage(), e.getMessage().contains(expected));
			}
		}
	}
}
