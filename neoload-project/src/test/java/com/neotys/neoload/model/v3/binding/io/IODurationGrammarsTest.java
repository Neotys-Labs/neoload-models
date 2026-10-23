package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.converter.ParsedDuration;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.MonitoringParameters;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class IODurationGrammarsTest {
	private static final String NO_VIOLATION = "";

	private static final String SECONDS_PROJECT = String.join("\n",
			"name: MyProject",
			"scenarios:",
			"- name: MyScenario",
			"  monitoring:",
			"    before: %1$s",
			"    after: %1$s",
			"  populations:",
			"  - name: Constant",
			"    constant_load:",
			"      users: 5",
			"      duration: %1$s",
			"      start_after: %1$s",
			"      rampup: %1$s",
			"      stop_after: %1$s",
			"  - name: Rampup",
			"    rampup_load:",
			"      min_users: 1",
			"      max_users: 5",
			"      increment_users: 1",
			"      increment_every: 1 iteration",
			"      duration: 5 iterations",
			"      increment_rampup: %1$s",
			"  - name: Peaks",
			"    peaks_load:",
			"      minimum:",
			"        users: 1",
			"        duration: 1 iteration",
			"      maximum:",
			"        users: 5",
			"        duration: 1 iteration",
			"      start: maximum",
			"      duration: 5 iterations",
			"      step_rampup: %1$s",
			"  rendezvous_policies:",
			"  - name: rdv",
			"    timeout: %1$s",
			"");

	private static final String CONSTANT_RAMPUP_PROJECT = String.join("\n",
			"name: MyProject",
			"scenarios:",
			"- name: MyScenario",
			"  populations:",
			"  - name: Constant",
			"    constant_load:",
			"      users: 5",
			"      duration: 1m",
			"      rampup: %s",
			"");

	private static final String RENDEZVOUS_PROJECT = String.join("\n",
			"name: MyProject",
			"scenarios:",
			"- name: MyScenario",
			"  populations:",
			"  - name: Constant",
			"    constant_load:",
			"      users: 5",
			"      duration: 1m",
			"  rendezvous_policies:",
			"  - name: rdv",
			"    timeout: %s",
			"");

	private static final String MILLISECONDS_PROJECT = String.join("\n",
			"name: MyProject",
			"user_paths:",
			"- name: MyUserPath",
			"  actions:",
			"    steps:",
			"    - delay: %1$s",
			"    - think_time: %2$s",
			"    - transaction:",
			"        name: MyTransaction",
			"        pacing: %3$s",
			"        steps:",
			"        - delay: 1",
			"    - web_page:",
			"        think_time: %4$s",
			"        steps:",
			"        - request:",
			"            url: http://host:80/",
			"    - wait_until:",
			"        conditions:",
			"        - \"'${status_code}' == '200'\"",
			"        timeout: %5$s",
			"");

	private static final String THINK_TIME_PROJECT = String.join("\n",
			"name: MyProject",
			"user_paths:",
			"- name: MyUserPath",
			"  actions:",
			"    steps:",
			"    - think_time: %s",
			"");

	private static final String WAIT_UNTIL_PROJECT = String.join("\n",
			"name: MyProject",
			"user_paths:",
			"- name: MyUserPath",
			"  actions:",
			"    steps:",
			"    - wait_until:",
			"        conditions:",
			"        - \"'${status_code}' == '200'\"",
			"        timeout: %s",
			"");

	private final IO io = new IO();

	private Project read(final String yaml) throws IOException {
		return io.read(yaml).getProject();
	}

	private String write(final Project project) throws IOException {
		return io.write(ProjectDescriptor.builder().project(project).build(), IO.Format.YAML);
	}

	private static String violations(final Project project) {
		final Validation validation = new Validator().validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
		return validation.getMessage().orElse(NO_VIOLATION);
	}

	private static Scenario scenario(final Project project) {
		return project.getScenarios().get(0);
	}

	private static List<Step> steps(final Project project) {
		return project.getUserPaths().get(0).getActions().getSteps();
	}

	private static DurationConstant delay(final Step step) {
		return (DurationConstant) ((Delay) step).getDuration();
	}

	private static long millis(final DurationConstant duration) {
		final ParsedDuration parsed = duration.parseValue();
		assertFalse(parsed.isVariable());
		assertEquals("ms", parsed.getUnit());
		return parsed.getAmount();
	}

	@Test
	public void plainNumbersOnSecondBasedFieldsMeanSeconds() throws IOException {
		final Project project = read(String.format(SECONDS_PROJECT, "90"));

		final Scenario scenario = scenario(project);
		final ConstantLoadPolicy constant = (ConstantLoadPolicy) scenario.getPopulations().get(0).getLoadPolicy();
		assertEquals(Integer.valueOf(90), constant.getDuration().getValue());
		assertEquals(90, constant.getStartAfter().getValue());
		assertEquals(90, constant.getStopAfter().getValue());
		assertEquals(Integer.valueOf(90), constant.getRampup());
		assertEquals(Integer.valueOf(90), ((RampupLoadPolicy) scenario.getPopulations().get(1).getLoadPolicy()).getRampup());
		assertEquals(Integer.valueOf(90), ((PeaksLoadPolicy) scenario.getPopulations().get(2).getLoadPolicy()).getRampup());
		final MonitoringParameters monitoring = scenario.getMonitoringParameters().get();
		assertEquals(Integer.valueOf(90), monitoring.getBeforeFirstVu());
		assertEquals(Integer.valueOf(90), monitoring.getAfterLastVus());
		assertEquals(Integer.valueOf(90), scenario.getRendezvousPolicies().get(0).getTimeout());
		assertEquals(NO_VIOLATION, violations(project));
	}

	@Test
	public void plainNumbersOnMillisecondFieldsMeanMilliseconds() throws IOException {
		final Project project = read(String.format(MILLISECONDS_PROJECT, "1500", "1500", "1500", "1500", "1500"));

		final List<Step> steps = steps(project);
		assertEquals(1500, millis(delay(steps.get(0))));
		assertEquals(1500, millis(delay(steps.get(1))));
		assertEquals(1500, millis((DurationConstant) ((Container) steps.get(2)).getPacing().get()));
		assertEquals(1500, millis((DurationConstant) ((WebPage) steps.get(3)).getThinkTime().get()));
		assertEquals(1500, ((WaitUntil) steps.get(4)).parseTimeout().getAmount());
		assertEquals(NO_VIOLATION, violations(project));
	}

	@Test
	public void secondBasedFieldsAcceptOrderedUnitsWithAnyNumberOfDigits() throws IOException {
		final String[] texts = {"90s", "1h30m", "120m", "100h"};
		final int[] expected = {90, 5400, 7200, 360000};
		for (int i = 0; i < texts.length; i++) {
			final Project project = read(String.format(CONSTANT_RAMPUP_PROJECT, texts[i]));

			final ConstantLoadPolicy constant = (ConstantLoadPolicy) scenario(project).getPopulations().get(0).getLoadPolicy();
			assertEquals(texts[i], Integer.valueOf(expected[i]), constant.getRampup());
			assertEquals(texts[i], NO_VIOLATION, violations(project));
		}
	}

	@Test
	public void secondBasedFieldsRejectMillisecondsAndOutOfOrderUnits() throws IOException {
		for (final String text : Arrays.asList("500ms", "1s1h")) {
			final Project project = read(String.format(RENDEZVOUS_PROJECT, text));

			final String message = violations(project);
			assertTrue(message, message.contains("'project.scenarios[0].rendezvous_policies[0].timeout': '" + text + "' is not a valid duration"));
			assertNull(scenario(project).getRendezvousPolicies().get(0).getTimeout());
		}
	}

	@Test
	public void millisecondFieldsAcceptOrderedUnitsAndRejectOutOfOrderUnits() throws IOException {
		final Project hours = read(String.format(THINK_TIME_PROJECT, "1h2m3s4ms"));
		assertEquals(3723004, millis(delay(steps(hours).get(0))));
		assertEquals(NO_VIOLATION, violations(hours));

		final Project millis = read(String.format(THINK_TIME_PROJECT, "250ms"));
		assertEquals(250, millis(delay(steps(millis).get(0))));
		assertEquals(NO_VIOLATION, violations(millis));

		for (final String text : Arrays.asList("1s1h", "5ms1s")) {
			final String message = violations(read(String.format(THINK_TIME_PROJECT, text)));
			assertTrue(message, message.contains("think_time.value': '" + text + "' is not a valid duration"));
		}
	}

	@Test
	public void waitUntilTimeoutBehavesLikeThinkTime() throws IOException {
		for (final String text : Arrays.asList("1500", "1s500ms", "2m", "${timeout}", "abc", "5x", "-1", "1s1h", "99999999999999999999")) {
			final Project thinkTime = read(String.format(THINK_TIME_PROJECT, text));
			final Project waitUntil = read(String.format(WAIT_UNTIL_PROJECT, text));

			final boolean thinkTimeValid = violations(thinkTime).isEmpty();
			assertEquals(text, thinkTimeValid, violations(waitUntil).isEmpty());
			if (thinkTimeValid) {
				assertEquals(text, delay(steps(thinkTime).get(0)).parseValue(), ((WaitUntil) steps(waitUntil).get(0)).parseTimeout());
			}
		}
	}

	@Test
	public void variableReferencesRoundTripUnchanged() throws IOException {
		final Project project = read(String.format(MILLISECONDS_PROJECT, "${d}", "${t}", "${p}", "${w}", "${u}"));
		assertEquals(NO_VIOLATION, violations(project));

		final Project reread = read(write(project));

		assertEquals(NO_VIOLATION, violations(reread));
		final List<Step> steps = steps(reread);
		assertEquals("${d}", delay(steps.get(0)).getValueInMsOrVariable());
		assertEquals("${t}", delay(steps.get(1)).getValueInMsOrVariable());
		assertEquals("${p}", ((DurationConstant) ((Container) steps.get(2)).getPacing().get()).getValueInMsOrVariable());
		assertEquals("${w}", ((DurationConstant) ((WebPage) steps.get(3)).getThinkTime().get()).getValueInMsOrVariable());
		assertEquals("${u}", ((WaitUntil) steps.get(4)).getTimeoutInMsOrVariable());
	}

	@Test
	public void invalidDurationsAreReportedAtLoadAndNeverReadAsZero() throws IOException {
		final String yaml = String.join("\n",
				"name: MyProject",
				"user_paths:",
				"- name: MyUserPath",
				"  actions:",
				"    steps:",
				"    - delay: 99999999999999999999",
				"    - web_page:",
				"        think_time: abc",
				"        steps:",
				"        - request:",
				"            url: http://host:80/",
				"scenarios:",
				"- name: MyScenario",
				"  monitoring:",
				"    before: 5x",
				"  populations:",
				"  - name: Constant",
				"    constant_load:",
				"      users: 5",
				"      duration: 1m",
				"      rampup: -5",
				"      stop_after: abc",
				"");

		final Project project = read(yaml);

		final String message = violations(project);
		assertTrue(message, message.contains("Violation Number: 5."));
		assertTrue(message, message.contains("'project.scenarios[0].populations[0].constant_load.stop_after': 'abc' is not a valid duration"));
		assertTrue(message, message.contains("'project.scenarios[0].monitoring.before': '5x' is not a valid duration"));
		assertTrue(message, message.contains("'project.scenarios[0].populations[0].constant_load.rampup': '-5' is not a valid duration"));
		assertTrue(message, message.contains("steps[0].delay.value': '99999999999999999999' is not a valid duration"));
		assertTrue(message, message.contains("steps[1].think_time.value': 'abc' is not a valid duration"));
		final ConstantLoadPolicy constant = (ConstantLoadPolicy) scenario(project).getPopulations().get(0).getLoadPolicy();
		assertNull(constant.getRampup());
		assertNull(scenario(project).getMonitoringParameters().get().getBeforeFirstVu());
		assertNull(constant.getStopAfter().getValue());
	}

	@Test
	public void savingInvalidDurationTextDoesNotCrash() throws IOException {
		final Project project = read(String.format(MILLISECONDS_PROJECT, "abc", "1", "1", "1", "5x"));

		final String written = write(project);

		assertTrue(written, written.contains("delay: abc"));
		assertTrue(written, written.contains("timeout: 5x"));
	}

	@Test
	public void savingAndReopeningKeepsDurationsInCompactForm() throws IOException {
		final String yaml = String.join("\n",
				"name: MyProject",
				"variables:",
				"- current_date:",
				"    name: MyDate",
				"    offset: -5d",
				"user_paths:",
				"- name: MyUserPath",
				"  actions:",
				"    steps:",
				"    - delay: 90000",
				"scenarios:",
				"- name: MyScenario",
				"  populations:",
				"  - name: Constant",
				"    constant_load:",
				"      users: 5",
				"      duration: 3600",
				"      rampup: 90",
				"  rendezvous_policies:",
				"  - name: rdv",
				"    timeout: 61",
				"");
		final Project project = read(yaml);

		final String written = write(project);

		assertTrue(written, written.contains("rampup: 1m30s"));
		assertTrue(written, written.contains("duration: 1h"));
		assertTrue(written, written.contains("timeout: 1m1s"));
		assertTrue(written, written.contains("delay: 1m30s"));
		assertTrue(written, written.contains("offset: -5d"));
		assertEquals(project, read(written));
	}

	@Test
	public void dateOffsetsKeepAcceptingEveryUnitAndNegativeValues() throws IOException {
		for (final String offset : Arrays.asList("-5d", "10ms", "3s", "-2m", "4h", "1mo", "-1y")) {
			final String currentDate = String.join("\n", "name: MyProject", "variables:", "- current_date:", "    name: MyDate", "    offset: " + offset, "");
			final String date = String.join("\n", "name: MyProject", "variables:", "- date:", "    name: MyDate", "    start_date: 01/01/2024 00:00:00", "    change_step: " + offset, "");

			for (final String yaml : Arrays.asList(currentDate, date)) {
				final Project project = read(yaml);
				assertEquals(offset, NO_VIOLATION, violations(project));
				final String written = write(project);
				assertTrue(written, written.contains(": " + offset));
				assertEquals(project, read(written));
			}
		}
	}

	@Test
	public void renamedGettersExposeStoredValueAndTypedAccessorsTheParsedValue() throws IOException {
		final String yaml = String.join("\n",
				"name: MyProject",
				"user_paths:",
				"- name: MyUserPath",
				"  actions:",
				"    steps:",
				"    - delay: 1s",
				"    - delay: ${d}",
				"");

		final List<Step> steps = steps(read(yaml));

		final DurationConstant literal = delay(steps.get(0));
		assertEquals("1000", literal.getValueInMsOrVariable());
		assertEquals(ParsedDuration.literal(1000, "ms"), literal.parseValue());
		final DurationConstant variable = delay(steps.get(1));
		assertEquals("${d}", variable.getValueInMsOrVariable());
		assertEquals(ParsedDuration.variable("d"), variable.parseValue());
	}
}
