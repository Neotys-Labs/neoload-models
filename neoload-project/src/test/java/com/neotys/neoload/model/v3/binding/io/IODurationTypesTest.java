package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidDurationRead;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidDuration;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.MonitoringParameters;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RendezvousPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationVariable;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Durations read from the v3 as-code YAML: second-based fields are a {@link Duration}, millisecond-based
 * fields a {@link DurationOrVariable}, and an invalid text fails the read.
 */
public class IODurationTypesTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String SECONDS_SCENARIO = DurationFields.scenario(
			"  monitoring:\n    before: %1$s\n    after: %1$s\n"
					+ "  populations:\n"
					+ "  - name: C\n    constant_load:\n      users: 1\n      start_after: %1$s\n      rampup: %1$s\n      stop_after: %1$s\n"
					+ "  - name: R\n    rampup_load:\n      min_users: 1\n      max_users: 5\n      increment_users: 1\n      increment_every: 1 iteration\n"
					+ "      duration: 15 iterations\n      increment_rampup: %1$s\n"
					+ "  - name: K\n    peaks_load:\n      minimum:\n        users: 1\n        duration: 1 iteration\n      maximum:\n        users: 5\n"
					+ "        duration: 1 iteration\n      start: maximum\n      duration: 15 iterations\n      step_rampup: %1$s\n"
					+ "  rendezvous_policies:\n  - name: r\n    timeout: %1$s\n");

	private static final String MILLISECONDS_ACTIONS = DurationFields.actions(
			"    - delay: %1$s\n    - think_time: %1$s\n"
					+ "    - transaction:\n        name: T\n        pacing: %1$s\n        steps:\n        - delay: 1s\n"
					+ "    - web_page:\n        think_time: %1$s\n        steps:\n        - request:\n            url: http://host:80/\n"
					+ DurationFields.waitUntil("        timeout: %1$s\n"));

	private static Project read(final String yaml) throws IOException {
		return new IO().read(yaml).getProject();
	}

	private static Validation validate(final Project project) {
		return VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	private static String write(final Project project) throws IOException {
		return new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);
	}

	private static Scenario scenario(final Project project) {
		return project.getScenarios().get(0);
	}

	private static LoadPolicy loadPolicy(final Project project, final int population) {
		return scenario(project).getPopulations().get(population).getLoadPolicy();
	}

	private static List<Step> steps(final Project project) {
		return project.getUserPaths().get(0).getActions().getSteps();
	}

	private static DurationOrVariable valueOf(final Step step) {
		return ((DurationConstant) ((Delay) step).getDuration()).getValue();
	}

	private static DurationOrVariable millis(final long milliseconds) {
		return DurationLiteral.builder().value(Duration.ofMillis(milliseconds)).build();
	}

	private static DurationOrVariable variable(final String reference) {
		return DurationVariable.builder().value(reference).build();
	}

	@Test
	public void plainNumbersOnSecondFieldsAreSeconds() throws IOException {
		final Project project = read(String.format(SECONDS_SCENARIO, "90"));

		final ConstantLoadPolicy constant = (ConstantLoadPolicy) loadPolicy(project, 0);
		assertEquals(Duration.ofSeconds(90), constant.getRampup());
		assertEquals(StartAfter.builder().type(StartAfter.Type.TIME).value(Duration.ofSeconds(90)).build(), constant.getStartAfter());
		assertEquals(StopAfter.builder().type(StopAfter.Type.TIME).value(Duration.ofSeconds(90)).build(), constant.getStopAfter());
		assertEquals(Duration.ofSeconds(90), ((RampupLoadPolicy) loadPolicy(project, 1)).getRampup());
		assertEquals(Duration.ofSeconds(90), ((PeaksLoadPolicy) loadPolicy(project, 2)).getRampup());
		final MonitoringParameters monitoring = scenario(project).getMonitoringParameters().get();
		assertEquals(Duration.ofSeconds(90), monitoring.getBeforeFirstVu());
		assertEquals(Duration.ofSeconds(90), monitoring.getAfterLastVus());
		assertEquals(Duration.ofSeconds(90), scenario(project).getRendezvousPolicies().get(0).getTimeout());
		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
	}

	@Test
	public void plainNumbersOnMillisecondFieldsAreMilliseconds() throws IOException {
		final Project project = read(String.format(MILLISECONDS_ACTIONS, "1500"));
		final List<Step> steps = steps(project);

		assertEquals(millis(1500), valueOf(steps.get(0)));
		assertEquals(millis(1500), valueOf(steps.get(1)));
		assertEquals(DurationConstant.builder().value(millis(1500)).build(), ((Container) steps.get(2)).getPacing().get());
		assertEquals(DurationConstant.builder().value(millis(1500)).build(), ((WebPage) steps.get(3)).getThinkTime().get());
		assertEquals(millis(1500), ((WaitUntil) steps.get(4)).getTimeout());
		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
	}

	@Test
	public void secondFieldsAcceptOrderedUnitsWithAnyNumberOfDigits() throws IOException {
		final String[] texts = {"90s", "1h30m", "120m", "100h"};
		final long[] seconds = {90, 5400, 7200, 360000};

		for (int i = 0; i < texts.length; i++) {
			final Project project = read(DurationFields.CONSTANT_RAMPUP.yaml(texts[i]));

			assertEquals(texts[i], Duration.ofSeconds(seconds[i]), ((ConstantLoadPolicy) loadPolicy(project, 0)).getRampup());
			assertTrue(texts[i], validate(project).isValid());
		}
	}

	@Test
	public void secondFieldsReadMillisecondsThatValidationRejects() throws IOException {
		final Project project = read(DurationFields.RENDEZVOUS_TIMEOUT.yaml("1s500ms"));

		assertEquals(Duration.ofMillis(1500), scenario(project).getRendezvousPolicies().get(0).getTimeout());
		assertViolations(validate(project), invalidValue("project.scenarios[0].rendezvous_policies[0].timeout", "'1s500ms' must be a whole number of seconds."));
	}

	@Test
	public void secondFieldsRejectMisorderedUnitsAtRead() {
		assertInvalidDurationRead(() -> read(DurationFields.RENDEZVOUS_TIMEOUT.yaml("1s1h")), "1s1h", "timeout");
	}

	@Test
	public void millisecondFieldsAcceptOrderedUnitsAndRejectMisorderedUnitsAtRead() throws IOException {
		assertEquals(millis(3723004), valueOf(steps(read(DurationFields.THINK_TIME.yaml("1h2m3s4ms"))).get(0)));
		assertEquals(millis(250), valueOf(steps(read(DurationFields.THINK_TIME.yaml("250ms"))).get(0)));
		for (final String value : new String[] {"1s1h", "5ms1s"}) {
			assertInvalidDurationRead(() -> read(DurationFields.THINK_TIME.yaml(value)), value, "think_time");
		}
	}

	@Test
	public void waitUntilTimeoutAcceptsAndRejectsWhatStepThinkTimeDoes() {
		for (final String value : new String[] {"1500", "1s500ms", "2m", "${timeout}", "abc", "5x", "-1", "1s1h", "99999999999999999999"}) {
			final Object thinkTime = readOrFailure(DurationFields.THINK_TIME, value);
			final Object waitUntil = readOrFailure(DurationFields.WAIT_UNTIL_TIMEOUT, value);

			if (thinkTime instanceof JsonMappingException) {
				assertTrue(value, waitUntil instanceof JsonMappingException);
				assertTrue(value, ((JsonMappingException) waitUntil).getMessage().contains("'" + value + "'"));
				assertTrue(value, ((JsonMappingException) waitUntil).getMessage().contains("timeout': "));
			} else {
				assertEquals(value, thinkTime, waitUntil);
			}
		}
	}

	private static Object readOrFailure(final DurationFields field, final String value) {
		try {
			final Step step = steps(read(field.yaml(value))).get(0);
			return step instanceof WaitUntil ? ((WaitUntil) step).getTimeout() : valueOf(step);
		} catch (final JsonMappingException e) {
			return e;
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void variableReferencesRoundTripUnchangedOnMillisecondFields() throws IOException {
		final String yaml = DurationFields.actions("    - delay: ${d}\n    - think_time: ${t}\n"
				+ "    - transaction:\n        name: T\n        pacing: ${p}\n        steps:\n        - delay: 1s\n"
				+ "    - web_page:\n        think_time: ${w}\n        steps:\n        - request:\n            url: http://host:80/\n"
				+ DurationFields.waitUntil("        timeout: ${u}\n"));

		final Project project = read(yaml);
		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
		final String written = write(project);
		final Project reopened = read(written);

		for (final Project read : Arrays.asList(project, reopened)) {
			final List<Step> steps = steps(read);
			assertEquals(variable("${d}"), valueOf(steps.get(0)));
			assertEquals(variable("${t}"), valueOf(steps.get(1)));
			assertEquals(variable("${p}"), ((DurationConstant) ((Container) steps.get(2)).getPacing().get()).getValue());
			assertEquals(variable("${w}"), ((DurationConstant) ((WebPage) steps.get(3)).getThinkTime().get()).getValue());
			assertEquals(variable("${u}"), ((WaitUntil) steps.get(4)).getTimeout());
		}
		for (final String reference : new String[] {"${d}", "${t}", "${p}", "${w}", "${u}"}) {
			assertTrue(written, written.contains(reference));
		}
	}

	@Test
	public void invalidDurationsFailTheReadAndAreNeverReplacedByZero() {
		assertInvalidDurationRead(() -> read(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      stop_after: abc\n")), "abc", "stop_after");
		assertInvalidDurationRead(() -> read(DurationFields.MONITORING_BEFORE.yaml("5x")), "5x", "before");
		assertInvalidDurationRead(() -> read(DurationFields.CONSTANT_RAMPUP.yaml("-5")), "-5", "rampup");
		assertInvalidDurationRead(() -> read(DurationFields.DELAY.yaml("99999999999999999999")), "99999999999999999999", "delay");
		assertInvalidDurationRead(() -> read(DurationFields.WEB_PAGE_THINK_TIME.yaml("abc")), "abc", "think_time");
	}

	@Test
	public void invalidDurationReadErrorNamesTheFieldPathInTheFile() {
		final JsonMappingException exception = assertThrows(JsonMappingException.class, () -> read(DurationFields.MONITORING_BEFORE.yaml("abc")));

		assertEquals(invalidValue("scenarios[0].monitoring.before", invalidDuration("abc")), exception.getMessage());
	}

	@Test
	public void durationViolationNamesTheFieldByItsKeyInTheFileAndQuotesTheValue() {
		final Project project = Project.builder().name("P").addScenarios(Scenario.builder().name("S")
				.monitoringParameters(MonitoringParameters.builder().beforeFirstVu(Duration.ofMillis(1500)).afterLastVus(Duration.ofSeconds(2)).build())
				.addPopulations(PopulationPolicy.builder().name("C").loadPolicy(ConstantLoadPolicy.builder().users(1).build()).build())
				.build()).build();

		assertViolations(validate(project), invalidValue("project.scenarios[0].monitoring.before", "'1s500ms' must be a whole number of seconds."));
	}

	@Test
	public void startAfterIsAPositiveDelayInSecondsOrAPopulationName() throws IOException {
		assertEquals(StartAfter.builder().type(StartAfter.Type.TIME).value(Duration.ofSeconds(30)).build(), startAfter("30"));
		assertEquals(StartAfter.builder().type(StartAfter.Type.TIME).value(Duration.ofSeconds(60)).build(), startAfter("1m"));
		assertEquals(StartAfter.builder().type(StartAfter.Type.POPULATION).value("my_population").build(), startAfter("my_population"));
		assertEquals(StartAfter.builder().type(StartAfter.Type.POPULATION).value("1s1h").build(), startAfter("1s1h"));
		assertInvalidDurationRead(() -> startAfter("-5"), "-5", "start_after");
	}

	private static StartAfter startAfter(final String value) throws IOException {
		return loadPolicy(read(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: " + value + "\n")), 0).getStartAfter();
	}

	@Test
	public void secondBasedDurationWithAMillisecondsPartIsReportedByValidation() {
		final Project project = Project.builder().name("P").addScenarios(Scenario.builder().name("S")
				.addPopulations(PopulationPolicy.builder().name("C").loadPolicy(ConstantLoadPolicy.builder().users(1).rampup(Duration.ofMillis(1500)).build()).build())
				.addRendezvousPolicies(RendezvousPolicy.builder().name("r").timeout(Duration.ofSeconds(2)).build())
				.build()).build();

		assertViolations(validate(project), invalidValue("project.scenarios[0].populations[0].constant_load.rampup", "'1s500ms' must be a whole number of seconds."));
	}

	@Test
	public void savingAndReopeningKeepsDurationsWrittenInCompactForm() throws IOException {
		final String yaml = DurationFields.scenario("  monitoring:\n    after: 3600\n"
				+ "  populations:\n  - name: C\n    constant_load:\n      users: 1\n      rampup: 90\n"
				+ "  rendezvous_policies:\n  - name: r\n    timeout: 61\n")
				+ "user_paths:\n- name: U\n  actions:\n    steps:\n    - delay: 90000\n"
				+ "    - web_page:\n        think_time: 1500\n        steps:\n        - request:\n            url: http://host:80/\n";

		final Project project = read(yaml);
		final String written = write(project);

		for (final String compact : new String[] {"rampup: 1m30s", "timeout: 1m1s", "after: 1h", "delay: 1m30s", "think_time: 1s500ms"}) {
			assertTrue(written, written.contains(compact));
		}
		assertEquals(project, read(written));
	}

	@Test
	public void defaultTimeoutsAreAppliedWhenAbsentAndOmittedWhenUnchanged() throws IOException {
		final String yaml = DurationFields.scenario("  rendezvous_policies:\n  - name: r\n")
				+ "user_paths:\n- name: U\n  actions:\n    steps:\n    - wait_until:\n        conditions:\n        - \"'${a}' == '1'\"\n";

		final Project project = read(yaml);

		assertEquals(Duration.ofSeconds(300), scenario(project).getRendezvousPolicies().get(0).getTimeout());
		assertEquals(millis(60000), ((WaitUntil) steps(project).get(0)).getTimeout());
		assertFalse(write(project), write(project).contains("timeout"));
	}

	@Test
	public void gettersKeepTheirNamesAndReturnJavaTimeBasedTypes() throws IOException, URISyntaxException {
		final String yaml = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      duration: 10m\n      rampup: 2m\n")
				+ "variables:\n- current_date:\n    name: D\n    offset: 3h\n"
				+ "user_paths:\n- name: U\n  actions:\n    steps:\n    - delay: 1s\n    - delay: ${d}\n";

		final Project project = read(yaml);

		assertEquals(millis(1000), valueOf(steps(project).get(0)));
		assertEquals(variable("${d}"), valueOf(steps(project).get(1)));
		final ConstantLoadPolicy constant = (ConstantLoadPolicy) loadPolicy(project, 0);
		assertEquals(Duration.ofSeconds(120), constant.getRampup());
		assertEquals(LoadDuration.builder().type(LoadDuration.Type.TIME).value(600).build(), constant.getDuration());
		assertEquals(java.util.Optional.of("3h"), ((CurrentDateVariable) project.getVariables().get(0)).getOffset());
		assertEquals(Arrays.asList(DurationLiteral.class, DurationVariable.class), implementationsOfDurationOrVariable());
	}

	private static List<Class<?>> implementationsOfDurationOrVariable() throws URISyntaxException, IOException {
		final File packageDirectory = new File(new File(DurationOrVariable.class.getProtectionDomain().getCodeSource().getLocation().toURI()),
				DurationOrVariable.class.getPackageName().replace('.', '/'));
		final List<Class<?>> implementations = new ArrayList<>();
		for (final String fileName : packageDirectory.list((directory, name) -> name.endsWith(".class") && !name.contains("$"))) {
			final Class<?> candidate = load(DurationOrVariable.class.getPackageName() + '.' + fileName.replace(".class", ""));
			if (candidate.isInterface() && candidate != DurationOrVariable.class && DurationOrVariable.class.isAssignableFrom(candidate)) {
				implementations.add(candidate);
			}
		}
		implementations.sort((a, b) -> a.getSimpleName().compareTo(b.getSimpleName()));
		return implementations;
	}

	private static Class<?> load(final String name) throws IOException {
		try {
			return Class.forName(name);
		} catch (final ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	@Test
	public void customLoadPolicyRampupIsASecondBasedDuration() throws IOException {
		final Project project = read(DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      rampup: 1m\n      steps:\n      - when: 1m40s\n        users: 300\n"));

		assertEquals(Duration.ofSeconds(60), ((CustomLoadPolicy) loadPolicy(project, 0)).getRampup());
		assertNotEquals(Duration.ofSeconds(1), ((CustomLoadPolicy) loadPolicy(project, 0)).getRampup());
		assertThrows(JsonMappingException.class, () -> read(DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      rampup: 1m1h\n      steps:\n      - when: 1m40s\n        users: 300\n")));
	}
}
