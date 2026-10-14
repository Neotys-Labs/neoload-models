package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.project.userpath.DelayRandom;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * The web page {@code think_time} field, the {@code think_time} step and the {@code delay} step
 * share one duration syntax and one validation.
 */
@RunWith(Parameterized.class)
public class IODelayContextsTest {

	private static final Validator VALIDATOR = new Validator();

	private enum Context {
		WEB_PAGE("think_time"), THINK_TIME_STEP("think_time"), DELAY_STEP("delay");

		private final String key;

		Context(final String key) {
			this.key = key;
		}

		String yaml(final String delayYaml) {
			final String header = "name: P\nuser_paths:\n- name: U\n  actions:\n    steps:\n";
			if (this == WEB_PAGE) {
				return header + "    - web_page:\n        think_time: " + delayYaml
						+ "\n        steps:\n        - request:\n            url: http://host:80/\n";
			}
			return header + "    - " + key + ": " + delayYaml + "\n";
		}

		Delay delay(final Project project) {
			final Step step = project.getUserPaths().get(0).getActions().getSteps().get(0);
			return this == WEB_PAGE ? ((WebPage) step).getThinkTime().get() : (Delay) step;
		}

		Project project(final Delay delay) {
			final Step step = this == WEB_PAGE
					? WebPage.builder().thinkTime(delay).addSteps(Request.builder().url("http://host:80/").build()).build()
					: delay;
			return IOHelper.buildProject(step);
		}
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> contexts() {
		return Arrays.asList(new Object[][] {{Context.WEB_PAGE}, {Context.THINK_TIME_STEP}, {Context.DELAY_STEP}});
	}

	private final Context context;

	public IODelayContextsTest(final Context context) {
		this.context = context;
	}

	private boolean thinkTime() {
		return context != Context.DELAY_STEP;
	}

	private Project read(final String delayYaml) throws IOException {
		return new IO().read(context.yaml(delayYaml)).getProject();
	}

	private String write(final Project project) throws IOException {
		return new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);
	}

	@Test
	public void writeRandomWithoutMinOmitsMin() throws IOException {
		final Delay delay = DelayRandom.builder().isThinkTime(thinkTime()).max("5000").build();

		final String yaml = write(context.project(delay));

		assertTrue(yaml, yaml.contains("max: 5s"));
		assertFalse(yaml, yaml.contains("min"));
	}

	@Test
	public void writeRandomWithMinKeepsMin() throws IOException {
		final Delay delay = DelayRandom.builder().isThinkTime(thinkTime()).min("1000").max("5000").build();

		final String yaml = write(context.project(delay));

		assertTrue(yaml, yaml.contains("min: 1s"));
		assertTrue(yaml, yaml.contains("max: 5s"));
	}

	@Test
	public void readRandomWithoutMinHasNoMinimum() throws IOException {
		final Project project = read("{max: 5s}");

		assertEquals(DelayRandom.builder().isThinkTime(thinkTime()).max("5000").build(), context.delay(project));
		assertTrue(VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class).isValid());
	}

	@Test
	public void readConstantInScalarAndObjectForm() throws IOException {
		final Delay expected = DelayConstant.builder().isThinkTime(thinkTime()).value("90000").build();

		for (final String delayYaml : new String[] {"1m 30s", "{value: 90000}"}) {
			final Project project = read(delayYaml);

			assertEquals(delayYaml, expected, context.delay(project));
			assertTrue(delayYaml, write(project).contains(context.key + ": 1m 30s"));
		}
	}

	@Test
	public void readPlainNumberAsMilliseconds() throws IOException {
		final Project project = read("{value: 2000}");

		assertEquals(DelayConstant.builder().isThinkTime(thinkTime()).value("2000").build(), context.delay(project));
		assertTrue(write(project).contains(context.key + ": 2s"));
	}

	@Test
	public void readValueCombinedWithMaxRejected() {
		try {
			read("{value: 2s, max: 5s}");
			fail("Expected 'value' combined with 'max' to fail");
		} catch (final IOException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("'value' cannot be combined with 'min' or 'max'"));
		}
	}

	@Test
	public void validateInvalidMaxReported() throws IOException {
		final Project project = read("{max: 2.5}");

		final Validation validation = VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);

		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("max"));
	}
}
