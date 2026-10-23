package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.MISSING_VALUE;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.NAME_NOT_ALLOWED;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.VALUE_COMBINED_WITH_BOUNDS;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertReadError;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidDuration;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.ExecuteResources;
import com.neotys.neoload.model.v3.project.userpath.Playback;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.SoapRequestContent;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class IOWebPageTest extends AbstractIOElementsTest {

	private static final Validator VALIDATOR = new Validator();

	private static Step getWebPageOnlyRequired() {
		return WebPage.builder()
				.addSteps(Request.builder()
						.url("http://host:80/")
						.build())
				.build();
	}

	@Test
	public void readWebPageOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getWebPageOnlyRequired());
		assertNotNull(expectedProject);

		read("test-web_page-only-required", expectedProject);
	}

	@Test
	public void readWebPageNoStepsRejected() throws IOException {
		final IO io = new IO();
		final File file = getFile("test-web_page-no-steps", "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		final Validation validation = VALIDATOR.validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("steps"));
	}

	@Test
	public void readWebPageEmptyStepsRejected() throws IOException {
		final IO io = new IO();
		final File file = getFile("test-web_page-empty-steps", "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		final Validation validation = VALIDATOR.validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("steps"));
	}

	@Test
	public void readWebPageThinkTimeConstant() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.thinkTime(DurationConstant.builder().valueInMsOrVariable("2000").build())
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-constant", expectedProject);
	}

	@Test
	public void readWebPageThinkTimeRandom() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.thinkTime(DurationRandom.builder().minInMsOrVariable("1000").maxInMsOrVariable("5000").build())
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-random", expectedProject);
	}

	@Test
	public void readWebPageThinkTimeRandomDefaultsMin() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.thinkTime(DurationRandom.builder().maxInMsOrVariable("5000").build())
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-random-default-min", expectedProject);
	}

	@Test
	public void readWebPageThinkTimeValuesPreservedVerbatim() throws IOException {
		final Project expectedProject = buildProject(
				WebPage.builder()
						.thinkTime(DurationConstant.builder().valueInMsOrVariable("2").build())
						.addSteps(Request.builder().url("http://host:80/").build())
						.build(),
				WebPage.builder()
						.thinkTime(DurationConstant.builder().valueInMsOrVariable("500").build())
						.addSteps(Request.builder().url("http://host:80/").build())
						.build(),
				WebPage.builder()
						.thinkTime(DurationConstant.builder().valueInMsOrVariable("${max_think_time}").build())
						.addSteps(Request.builder().url("http://host:80/").build())
						.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-values", expectedProject);
	}

	@Test
	public void readWebPageUnsupportedStepRejected() throws IOException {
		final IO io = new IO();
		final File file = getFile("test-web_page-unsupported-step", "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		final Validation validation = VALIDATOR.validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("steps"));
	}

	@Test
	public void readWebPageThinkTimeMixedRejected() {
		final IO io = new IO();
		final File file = getFile("test-web_page-think-time-mixed", "yaml");
		try {
			io.read(file);
			fail("Expected reading a think_time mixing 'value' with 'max' to fail");
		} catch (final IOException e) {
			assertReadError(e, VALUE_COMBINED_WITH_BOUNDS, "think_time");
		}
	}

	@Test
	public void readWebPageThinkTimeMinOnlyRejected() throws IOException {
		final IO io = new IO();
		final File file = getFile("test-web_page-think-time-min-only", "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		assertViolations(VALIDATOR.validate(descriptor, NeoLoad.class),
				invalidValue("project.user_paths[0].actions.steps[0].think_time.max", MISSING_VALUE));
	}

	@Test
	public void readWebPageThinkTimeWithNameRejected() {
		final IO io = new IO();
		final File file = getFile("test-web_page-think-time-with-name", "yaml");
		try {
			io.read(file);
			fail("Expected reading a web page think_time with a name to fail");
		} catch (final IOException e) {
			assertReadError(e, NAME_NOT_ALLOWED, "think_time");
		}
	}

	@Test
	public void readWebPageThinkTimeInvalidValueRejected() throws IOException {
		final IO io = new IO();
		final File file = getFile("test-web_page-think-time-invalid-value", "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		assertViolations(VALIDATOR.validate(descriptor, NeoLoad.class),
				invalidDuration("project.user_paths[0].actions.steps[0].think_time.value", "soon"));
	}

	@Test
	public void readWebPagePlaybackAndExecuteResources() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.playback(Playback.SEQUENTIAL)
				.executeResources(ExecuteResources.DYNAMIC)
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-playback-and-execute-resources", expectedProject);
	}

	@Test
	public void readWebPageExecuteResourcesDynamicForcedEncoding() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.executeResources(ExecuteResources.DYNAMIC_FORCED_ENCODING)
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-execute-resources-dynamic-forced-encoding", expectedProject);
	}

	@Test
	public void readWebPagePlaybackInvalidRejected() {
		final IO io = new IO();
		final File file = getFile("test-web_page-playback-invalid", "yaml");
		try {
			io.read(file);
			fail("Expected reading an unknown playback value to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage().contains("fastest"));
		}
	}

	@Test
	public void readWebPageWithSoapRequestChild() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.addSteps(SoapRequest.builder()
						.url("http://host:80/")
						.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
						.build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-soap-request", expectedProject);
	}

	@Test
	public void readWebPageNested() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.addSteps(
						Request.builder().url("http://host:80/").build(),
						WebPage.builder()
								.addSteps(Request.builder().url("http://host:80/resource").build())
								.build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-nested", expectedProject);
	}

	@Test
	public void readWebPageExecuteResourcesInvalidRejected() {
		final IO io = new IO();
		final File file = getFile("test-web_page-execute-resources-invalid", "yaml");
		try {
			io.read(file);
			fail("Expected reading an unknown execute_resources value to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage().contains("always"));
		}
	}

	private static Step getWebPageRequiredAndOptional() {
		return WebPage.builder()
				.name("MyWebPage")
				.description("My Web Page")
				.slaProfile("MySlaProfile")
				.thinkTime(DurationConstant.builder().valueInMsOrVariable("2000").build())
				.playback(Playback.SEQUENTIAL)
				.executeResources(ExecuteResources.DYNAMIC)
				.addSteps(
						Request.builder().url("http://host:80/").build(),
						Request.builder().url("http://host:80/resource").build())
				.build();
	}

	private static Step getWebPageAllDefaults() {
		return WebPage.builder()
				.addSteps(Request.builder().url("http://host:80/").build())
				.build();
	}

	@Test
	public void readWebPageRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getWebPageRequiredAndOptional(), getWebPageAllDefaults());
		assertNotNull(expectedProject);

		read("test-web_page-required-and-optional", expectedProject);
	}

	@Test
	public void writeWebPageRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getWebPageRequiredAndOptional(), getWebPageAllDefaults());
		assertNotNull(expectedProject);

		write("test-web_page-required-and-optional", expectedProject);
	}
}
