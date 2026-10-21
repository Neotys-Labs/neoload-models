package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.UserPath.FailurePolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class IOUserPathRuntimeParametersTest extends AbstractIOElementsTest {
	private static final String EXPECTED_POLICIES = "must be 'go_to_next_iteration' or 'stop_and_start_new_vu'";

	private static Container actions() {
		return Container.builder()
				.name("actions")
				.addSteps(Delay.builder().duration(DurationConstant.builder().value("1000").build()).build())
				.build();
	}

	private static Project getRuntimeParameters() {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath1")
						.onError(FailurePolicy.GO_TO_NEXT_ITERATION)
						.onAssertionFailure(FailurePolicy.STOP_AND_START_NEW_VU)
						.thinkTime(UserPathThinkTime.builder().override(DurationConstant.builder().value("5000").build()).random("10%").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath2")
						.onAssertionFailure(FailurePolicy.GO_TO_NEXT_ITERATION)
						.thinkTime(UserPathThinkTime.builder().factor("150%").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath3")
						.thinkTime(UserPathThinkTime.builder().random("20%").build())
						.actions(actions())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath4")
						.onError(FailurePolicy.STOP_AND_START_NEW_VU)
						.thinkTime(UserPathThinkTime.builder().override(DurationConstant.builder().value("90000").build()).build())
						.actions(actions())
						.build())
				.build();
	}

	private static Project getRuntimeParametersAsVariables() {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath1")
						.thinkTime(UserPathThinkTime.builder().override(DurationConstant.builder().value("${my_think_time}").build()).random("${my_random_delay}").build())
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
		assertTrue(message, message.contains("Violation Number: 14."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[0].think_time.override.value': '-100' " + ViolationsAssert.INVALID_DURATION));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[7].think_time.factor': must be a non-negative integer percentage (e.g. 150%) or a variable."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[10].think_time.random': must be a positive integer percentage (e.g. 10%) or a variable."));
		assertTrue(message, message.contains("Incorrect value for 'project.user_paths[13].think_time.random': must be a positive integer percentage (e.g. 10%) or a variable."));
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
	public void readUnknownOnErrorPolicyFailsWithAClearMessage() throws IOException {
		assertReadFails(getContent(getFile("test-userpaths-invalid-on-error", "yaml"), StandardCharsets.UTF_8),
				"Incorrect value for 'on_error' in User Path 'MyUserPath': " + EXPECTED_POLICIES + " (found: \"restart_vu\").");
	}

	@Test
	public void readDoNothingOnAssertionFailurePolicyFailsWithAClearMessage() throws IOException {
		assertReadFails(getContent(getFile("test-userpaths-invalid-on-assertion-failure", "yaml"), StandardCharsets.UTF_8),
				"Incorrect value for 'on_assertion_failure' in User Path 'MyUserPath': " + EXPECTED_POLICIES + " (found: \"do_nothing\").");
	}

	@Test
	public void readPolicyThatIsNotANameFailsWithAClearMessage() {
		assertReadFails(userPathWith("on_error: 1"),
				"Incorrect value for 'on_error' in User Path 'MyUserPath': " + EXPECTED_POLICIES + " (found: 1).");
		assertReadFails(userPathWith("on_error: \"\""),
				"Incorrect value for 'on_error' in User Path 'MyUserPath': " + EXPECTED_POLICIES + " (found: \"\").");
		assertReadFails(userPathWith("on_assertion_failure: true"),
				"Incorrect value for 'on_assertion_failure' in User Path 'MyUserPath': " + EXPECTED_POLICIES + " (found: true).");
	}

	@Test
	public void readPolicyWithoutValueLeavesItUnset() throws IOException {
		final UserPath userPath = new IO().read(userPathWith("on_error:")).getProject().getUserPaths().get(0);

		assertFalse(userPath.getOnError().isPresent());
	}

	@Test
	public void readThinkTimeThatIsNotAnObjectFailsWithAClearMessage() {
		assertReadFails(userPathWith("think_time: 5s"),
				"Incorrect value for 'think_time' in User Path 'MyUserPath': must be an object with 'override', 'factor' and/or 'random' (found: \"5s\").");
	}

	@Test
	public void readThinkTimeOverrideThatIsNotASingleDurationFailsWithAClearMessage() {
		assertReadFails(userPathWith("think_time:\n    override:\n      min: 1s\n      max: 2s"),
				"Incorrect value for 'think_time.override' in User Path 'MyUserPath': must be a single duration (found: {\"min\":\"1s\",\"max\":\"2s\"}).");
	}

	@Test
	public void readUnknownThinkTimeSettingFailsDeserialization() {
		try {
			new IO().read(getFile("test-userpaths-think-time-unknown-key", "yaml"));
			fail("Expected reading an unknown think_time setting to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("random_delay"));
		}
	}

	private static String userPathWith(final String setting) {
		return "name: MyProject\nuser_paths:\n- name: MyUserPath\n  " + setting + "\n  actions:\n    steps:\n    - delay: 1s\n";
	}

	/**
	 * Checks the message NeoLoad shows when loading the project: the one of the cause, when there is one.
	 */
	private static void assertReadFails(final String content, final String expectedMessage) {
		try {
			new IO().read(content);
			fail("Expected reading to fail with: " + expectedMessage);
		} catch (final IOException e) {
			final Throwable shown = e.getCause() != null ? e.getCause() : e;
			assertEquals(expectedMessage, shown.getMessage());
		}
	}
}
