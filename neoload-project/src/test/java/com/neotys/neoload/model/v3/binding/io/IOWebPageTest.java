package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.ExecuteResources;
import com.neotys.neoload.model.v3.project.userpath.Playback;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeConstant;
import com.neotys.neoload.model.v3.project.userpath.WebPageThinkTimeRandom;
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
				.thinkTime(WebPageThinkTimeConstant.builder().value("2s").build())
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-constant", expectedProject);
	}

	@Test
	public void readWebPageThinkTimeRandom() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.thinkTime(WebPageThinkTimeRandom.builder().min("1s").max("5s").build())
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-random", expectedProject);
	}

	@Test
	public void readWebPageThinkTimeRandomDefaultsMin() throws IOException {
		final Project expectedProject = buildProject(WebPage.builder()
				.thinkTime(WebPageThinkTimeRandom.builder().max("5s").build())
				.addSteps(Request.builder().url("http://host:80/").build())
				.build());
		assertNotNull(expectedProject);

		read("test-web_page-think-time-random-default-min", expectedProject);
	}

	@Test
	public void readWebPageThinkTimeValuesPreservedVerbatim() throws IOException {
		final Project expectedProject = buildProject(
				WebPage.builder()
						.thinkTime(WebPageThinkTimeConstant.builder().value("2").build())
						.addSteps(Request.builder().url("http://host:80/").build())
						.build(),
				WebPage.builder()
						.thinkTime(WebPageThinkTimeConstant.builder().value("500ms").build())
						.addSteps(Request.builder().url("http://host:80/").build())
						.build(),
				WebPage.builder()
						.thinkTime(WebPageThinkTimeConstant.builder().value("${max_think_time}").build())
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
			assertTrue(e.getMessage().contains("value"));
		}
	}

	@Test
	public void readWebPageThinkTimeMinOnlyRejected() {
		final IO io = new IO();
		final File file = getFile("test-web_page-think-time-min-only", "yaml");
		try {
			io.read(file);
			fail("Expected reading a think_time with 'min' but no 'max' to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage().contains("max"));
		}
	}

	@Test
	public void readWebPageThinkTimeInvalidValueRejected() throws IOException {
		final IO io = new IO();
		final File file = getFile("test-web_page-think-time-invalid-value", "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		final Validation validation = VALIDATOR.validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("value"));
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
				.thinkTime(WebPageThinkTimeConstant.builder().value("2s").build())
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
