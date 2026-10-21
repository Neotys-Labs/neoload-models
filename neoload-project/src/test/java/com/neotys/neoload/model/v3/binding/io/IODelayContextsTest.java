package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.MAX_NOT_GREATER_THAN_MIN;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.VALUE_COMBINED_WITH_BOUNDS;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertReadError;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidDuration;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
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
 * The container {@code pacing}, the web page {@code think_time} field, the {@code think_time} step
 * and the {@code delay} step share one duration syntax and one validation.
 */
@RunWith(Parameterized.class)
public class IODelayContextsTest {

	private static final Validator VALIDATOR = new Validator();

	private enum Context {
		PACING("pacing"), WEB_PAGE("think_time"), THINK_TIME_STEP("think_time"), DELAY_STEP("delay");

		private final String key;
		private final String path;

		Context(final String key) {
			this.key = key;
			this.path = "project.user_paths[0].actions.steps[0]." + key;
		}

		String yaml(final String delayYaml) {
			final String header = "name: P\nuser_paths:\n- name: U\n  actions:\n    steps:\n";
			if (this == PACING) {
				return header + "    - container:\n        pacing: " + delayYaml + "\n        steps:\n        - delay: 1s\n";
			}
			if (this == WEB_PAGE) {
				return header + "    - web_page:\n        think_time: " + delayYaml
						+ "\n        steps:\n        - request:\n            url: http://host:80/\n";
			}
			return header + "    - " + key + ": " + delayYaml + "\n";
		}

		Duration duration(final Project project) {
			final Step step = project.getUserPaths().get(0).getActions().getSteps().get(0);
			switch (this) {
				case PACING: return ((Container) step).getPacing().get();
				case WEB_PAGE: return ((WebPage) step).getThinkTime().get();
				default: return ((Delay) step).getDuration();
			}
		}

		Project project(final Duration duration) {
			return IOHelper.buildProject(step(duration));
		}

		private Step step(final Duration duration) {
			switch (this) {
				case PACING: return Container.builder().pacing(duration).addSteps(Delay.builder().duration(DurationConstant.builder().value("1000").build()).build()).build();
				case WEB_PAGE: return WebPage.builder().thinkTime(duration).addSteps(Request.builder().url("http://host:80/").build()).build();
				default: return Delay.builder().isThinkTime(this == THINK_TIME_STEP).duration(duration).build();
			}
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

	private Project read(final String delayYaml) throws IOException {
		return new IO().read(context.yaml(delayYaml)).getProject();
	}

	private String write(final Project project) throws IOException {
		return new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);
	}

	@Test
	public void writeRandomWithoutMinOmitsMin() throws IOException {
		final String yaml = write(context.project(DurationRandom.builder().max("5000").build()));

		assertTrue(yaml, yaml.contains("max: 5s"));
		assertFalse(yaml, yaml.contains("min"));
	}

	@Test
	public void writeRandomWithZeroMinOmitsMin() throws IOException {
		final String yaml = write(context.project(DurationRandom.builder().min("0").max("5000").build()));

		assertTrue(yaml, yaml.contains("max: 5s"));
		assertFalse(yaml, yaml.contains("min"));
	}

	@Test
	public void writeRandomWithMinKeepsMin() throws IOException {
		final String yaml = write(context.project(DurationRandom.builder().min("2000").max("5000").build()));

		assertTrue(yaml, yaml.contains("min: 2s"));
		assertTrue(yaml, yaml.contains("max: 5s"));
	}

	@Test
	public void writeConstantInCompactUnitFormat() throws IOException {
		assertTrue(write(context.project(DurationConstant.builder().value("3601000").build())).contains(context.key + ": 1h1s"));
		assertTrue(write(context.project(DurationConstant.builder().value("1500").build())).contains(context.key + ": 1s500ms"));
		assertTrue(write(context.project(DurationConstant.builder().value("${var}").build())).contains("${var}"));
	}

	@Test
	public void readRandomWithoutMinHasNoMinimum() throws IOException {
		final Project project = read("{max: 5s}");

		assertEquals(DurationRandom.builder().max("5000").build(), context.duration(project));
		assertTrue(VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class).isValid());
	}

	@Test
	public void readConstantInScalarAndObjectForm() throws IOException {
		final Duration expected = DurationConstant.builder().value("90000").build();

		for (final String delayYaml : new String[] {"1m 30s", "{value: 90000}"}) {
			final Project project = read(delayYaml);

			assertEquals(delayYaml, expected, context.duration(project));
			assertTrue(delayYaml, write(project).contains(context.key + ": 1m30s"));
		}
	}

	@Test
	public void readPlainNumberAsMilliseconds() throws IOException {
		final Project project = read("{value: 2000}");

		assertEquals(DurationConstant.builder().value("2000").build(), context.duration(project));
		assertTrue(write(project).contains(context.key + ": 2s"));
	}

	@Test
	public void readConstantWithSpacesBetweenUnits() throws IOException {
		final Project project = read("1h 1m 1s 1ms");

		assertEquals(DurationConstant.builder().value("3661001").build(), context.duration(project));
		assertTrue(VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class).isValid());
	}

	@Test
	public void readValueCombinedWithMaxRejected() {
		try {
			read("{value: 2s, max: 5s}");
			fail("Expected 'value' combined with 'max' to fail");
		} catch (final IOException e) {
			assertReadError(e, VALUE_COMBINED_WITH_BOUNDS, context == Context.WEB_PAGE ? "think_time" : "steps");
		}
	}

	@Test
	public void validateVariableFollowsVariableNameRules() {
		for (final String variable : new String[] {"${my-var}", "${my var}", "${file.column}"}) {
			assertTrue(variable, validate(context.project(DurationConstant.builder().value(variable).build())).isValid());
		}
		for (final String variable : new String[] {"${}", "${a$b}", "${a€b}", "${a#b}", "${a.}", "${.a}", "${a{b}}"}) {
			assertViolations(validate(context.project(DurationConstant.builder().value(variable).build())), invalidDuration(context.path + ".value", variable));
		}
	}

	private static Validation validate(final Project project) {
		return VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	@Test
	public void validateInvalidMaxReported() throws IOException {
		assertViolations(validate(read("{max: 2.5}")), invalidDuration(context.path + ".max", "2.5"));
	}

	@Test
	public void validateMaxNotGreaterThanMinRejected() throws IOException {
		for (final String delayYaml : new String[] {"{min: 5s, max: 5s}", "{min: 10s, max: 5s}", "{max: 0}"}) {
			assertViolations(validate(read(delayYaml)), invalidValue(context.path, MAX_NOT_GREATER_THAN_MIN));
		}
	}
}
