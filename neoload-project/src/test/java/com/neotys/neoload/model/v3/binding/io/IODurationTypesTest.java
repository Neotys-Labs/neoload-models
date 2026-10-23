package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RendezvousPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WaitUntil;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable;
import com.neotys.neoload.model.v3.project.variable.DateVariable;
import com.neotys.neoload.model.v3.project.variable.IncrementTimeUnit;
import com.neotys.neoload.model.v3.project.variable.Offset;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

/**
 * Reads, validates and writes durations and offsets written inline as YAML, to check what each field accepts,
 * rejects and gives back.
 */
public class IODurationTypesTest {
	private static final String HEAD = "name: MyProject\n";
	private static final String USER_PATH = HEAD + "user_paths:\n- name: MyUserPath\n  actions:\n    steps:\n";
	private static final String SCENARIO = HEAD + "scenarios:\n- name: MyScenario\n";
	private static final String CONSTANT_LOAD = SCENARIO + "  populations:\n  - name: MyPopulation\n    constant_load:\n      users: 1\n";

	private static ProjectDescriptor read(final String yaml) throws IOException {
		return new IO().read(yaml);
	}

	private static Validation validate(final ProjectDescriptor descriptor) {
		return new Validator().validate(descriptor, NeoLoad.class);
	}

	private static String write(final ProjectDescriptor descriptor) throws IOException {
		return new IO().write(descriptor, IO.Format.YAML);
	}

	private static String message(final Validation validation) {
		return validation.getMessage().orElse("");
	}

	private static Scenario scenario(final ProjectDescriptor descriptor) {
		return descriptor.getProject().getScenarios().get(0);
	}

	private static LoadPolicy policy(final ProjectDescriptor descriptor, final int index) {
		return scenario(descriptor).getPopulations().get(index).getLoadPolicy();
	}

	private static Container actions(final ProjectDescriptor descriptor) {
		return descriptor.getProject().getUserPaths().get(0).getActions();
	}

	private static MillisecondsDuration milliseconds(final Duration duration) {
		return ((DurationConstant) duration).getValue();
	}

	private static MillisecondsDuration delay(final Step step) {
		return milliseconds(((Delay) step).getDuration());
	}

	private static int count(final String text, final String part) {
		return text.split(java.util.regex.Pattern.quote(part), -1).length - 1;
	}

	private static String secondBasedProject(final String value) {
		return SCENARIO + "  monitoring:\n    before: " + value + "\n    after: " + value + "\n"
				+ "  populations:\n"
				+ "  - name: Constant\n    constant_load:\n      users: 1\n      duration: " + value + "\n      start_after: " + value
				+ "\n      rampup: " + value + "\n      stop_after: " + value + "\n"
				+ "  - name: Rampup\n    rampup_load:\n      min_users: 1\n      increment_users: 1\n      increment_every: 5s\n      increment_rampup: " + value + "\n"
				+ "  - name: Peaks\n    peaks_load:\n      minimum:\n        users: 1\n        duration: 1m\n      maximum:\n        users: 2\n        duration: 1m\n"
				+ "      start: minimum\n      step_rampup: " + value + "\n"
				+ "  rendezvous_policies:\n  - name: rdv\n    timeout: " + value + "\n";
	}

	private static String millisecondBasedProject(final String value) {
		return USER_PATH
				+ "    - delay: " + value + "\n"
				+ "    - think_time: " + value + "\n"
				+ "    - web_page:\n        think_time: " + value + "\n        steps:\n        - request:\n            url: http://host:80/\n"
				+ "    - wait_until:\n        conditions:\n        - \"'${a}' == 'b'\"\n        timeout: " + value + "\n";
	}

	@Test
	public void plainNumbersOnSecondBasedFieldsMeanSeconds() throws IOException {
		final ProjectDescriptor descriptor = read(secondBasedProject("90"));

		final SecondsDuration ninety = SecondsDuration.of(90);
		final LoadPolicy constant = policy(descriptor, 0);
		assertEquals(ninety, constant.getDuration().getValue());
		assertEquals(LoadDuration.Type.TIME, constant.getDuration().getType());
		assertEquals(ninety, constant.getStartAfter().getValue());
		assertEquals(StartAfter.Type.TIME, constant.getStartAfter().getType());
		assertEquals(ninety, constant.getRampup());
		assertEquals(ninety, constant.getStopAfter().getValue());
		assertEquals(StopAfter.Type.TIME, constant.getStopAfter().getType());
		assertEquals(ninety, ((RampupLoadPolicy) policy(descriptor, 1)).getRampup());
		assertEquals(ninety, ((PeaksLoadPolicy) policy(descriptor, 2)).getRampup());
		assertEquals(ninety, scenario(descriptor).getMonitoringParameters().get().getBeforeFirstVu());
		assertEquals(ninety, scenario(descriptor).getMonitoringParameters().get().getAfterLastVus());
		final RendezvousPolicy rendezvous = scenario(descriptor).getRendezvousPolicies().get(0);
		assertEquals(ninety, rendezvous.getTimeout());
		assertTrue(message(validate(descriptor)), validate(descriptor).isValid());
	}

	@Test
	public void plainNumbersOnMillisecondBasedFieldsMeanMilliseconds() throws IOException {
		final ProjectDescriptor descriptor = read(millisecondBasedProject("1500"));

		final MillisecondsDuration expected = MillisecondsDuration.of(1500);
		final List<Step> steps = actions(descriptor).getSteps();
		assertEquals(expected, delay(steps.get(0)));
		assertEquals(expected, delay(steps.get(1)));
		assertEquals(expected, milliseconds(((WebPage) steps.get(2)).getThinkTime().get()));
		assertEquals(expected, ((WaitUntil) steps.get(3)).getTimeout());
		assertTrue(message(validate(descriptor)), validate(descriptor).isValid());

		final ProjectDescriptor withPacing = read(USER_PATH.replace("    steps:\n", "    pacing: 1500\n    steps:\n") + "    - delay: 1\n");
		assertEquals(expected, milliseconds(actions(withPacing).getPacing().get()));
		assertTrue(message(validate(withPacing)), validate(withPacing).isValid());
	}

	@Test
	public void secondBasedFieldsAcceptOrderedUnitsWithAnyNumberOfDigits() throws IOException {
		final String[] texts = {"90s", "1h30m", "120m", "100h"};
		final long[] seconds = {90, 5400, 7200, 360000};
		for (int i = 0; i < texts.length; i++) {
			final ProjectDescriptor descriptor = read(CONSTANT_LOAD + "      rampup: " + texts[i] + "\n");

			assertEquals(texts[i], SecondsDuration.of(seconds[i]), policy(descriptor, 0).getRampup());
			assertTrue(texts[i] + message(validate(descriptor)), validate(descriptor).isValid());
		}
	}

	@Test
	public void startAfterIsAPositiveDelayOrAPopulationName() throws IOException {
		final ProjectDescriptor delay = read(CONSTANT_LOAD + "      start_after: 1m\n");
		assertEquals(StartAfter.Type.TIME, policy(delay, 0).getStartAfter().getType());
		assertEquals(SecondsDuration.of(60), policy(delay, 0).getStartAfter().getValue());
		assertTrue(message(validate(delay)), validate(delay).isValid());

		final ProjectDescriptor population = read(CONSTANT_LOAD + "      start_after: MyOtherPopulation\n");
		assertEquals(StartAfter.Type.POPULATION, policy(population, 0).getStartAfter().getType());
		assertEquals("MyOtherPopulation", policy(population, 0).getStartAfter().getValue());
		assertTrue(message(validate(population)), validate(population).isValid());

		for (final String text : new String[] {"0", "-5", "-5s"}) {
			final ProjectDescriptor invalid = read(CONSTANT_LOAD + "      start_after: " + text + "\n");

			assertEquals(text, StartAfter.Type.TIME, policy(invalid, 0).getStartAfter().getType());
			final String message = message(validate(invalid));
			assertTrue(message, message.contains("constant_load.start_after"));
			assertEquals(text, 1, count(message, "Violation 1"));
			assertTrue(text, write(invalid).contains("start_after: " + text));
		}
	}

	@Test
	public void secondBasedFieldsRejectMillisecondsAndOutOfOrderUnits() throws IOException {
		for (final String text : new String[] {"500ms", "1s1h"}) {
			final ProjectDescriptor descriptor = read(SCENARIO + "  rendezvous_policies:\n  - name: rdv\n    timeout: " + text + "\n");

			final RendezvousPolicy rendezvous = scenario(descriptor).getRendezvousPolicies().get(0);
			assertTrue(text, rendezvous.getTimeout().isInvalid());
			assertEquals(text, rendezvous.getTimeout().toText());
			final String message = message(validate(descriptor));
			assertTrue(message, message.contains("rendezvous_policies[0].timeout"));
			assertTrue(message, message.contains("'" + text + "' is not a valid duration"));
		}
	}

	@Test
	public void millisecondBasedFieldsAcceptOrderedUnitsAndRejectOutOfOrderUnits() throws IOException {
		final ProjectDescriptor valid = read(USER_PATH + "    - think_time: 1h2m3s4ms\n    - think_time: 250ms\n");
		assertEquals(MillisecondsDuration.of(3723004), delay(actions(valid).getSteps().get(0)));
		assertEquals(MillisecondsDuration.of(250), delay(actions(valid).getSteps().get(1)));
		assertTrue(message(validate(valid)), validate(valid).isValid());

		for (final String text : new String[] {"1s1h", "5ms1s"}) {
			final ProjectDescriptor invalid = read(USER_PATH + "    - think_time: " + text + "\n");

			assertTrue(text, delay(actions(invalid).getSteps().get(0)).isInvalid());
			final String message = message(validate(invalid));
			assertTrue(message, message.contains("steps[0].think_time"));
			assertTrue(message, message.contains("'" + text + "' is not a valid duration"));
		}
	}

	@Test
	public void waitUntilTimeoutAcceptsAndRejectsExactlyWhatThinkTimeDoes() throws IOException {
		for (final String text : new String[] {"1500", "1s500ms", "2m", "${timeout}", "abc", "5x", "-1", "1s1h", "99999999999999999999"}) {
			final ProjectDescriptor waitUntil = read(USER_PATH + "    - wait_until:\n        conditions:\n        - \"'${a}' == 'b'\"\n        timeout: " + text + "\n");
			final ProjectDescriptor thinkTime = read(USER_PATH + "    - think_time: " + text + "\n");

			assertEquals(text, delay(actions(thinkTime).getSteps().get(0)), ((WaitUntil) actions(waitUntil).getSteps().get(0)).getTimeout());
			assertEquals(text, validate(thinkTime).isValid(), validate(waitUntil).isValid());
		}
	}

	@Test
	public void variableReferencesStayAcceptedOnMillisecondFieldsAndRoundTripUnchanged() throws IOException {
		final String yaml = USER_PATH
				+ "    - delay: ${d}\n"
				+ "    - think_time: ${t}\n"
				+ "    - web_page:\n        think_time: ${w}\n        steps:\n        - request:\n            url: http://host:80/\n"
				+ "    - wait_until:\n        conditions:\n        - \"'${a}' == 'b'\"\n        timeout: ${u}\n";

		final ProjectDescriptor descriptor = read(yaml);
		assertTrue(message(validate(descriptor)), validate(descriptor).isValid());
		final List<Step> steps = actions(descriptor).getSteps();
		assertEquals(MillisecondsDuration.ofVariable("d"), delay(steps.get(0)));
		assertEquals(MillisecondsDuration.ofVariable("t"), delay(steps.get(1)));
		assertEquals(MillisecondsDuration.ofVariable("w"), milliseconds(((WebPage) steps.get(2)).getThinkTime().get()));
		assertEquals(MillisecondsDuration.ofVariable("u"), ((WaitUntil) steps.get(3)).getTimeout());

		final String written = write(descriptor);
		for (final String variable : new String[] {"${d}", "${t}", "${w}", "${u}"}) {
			assertTrue(written, written.contains(variable));
		}
		final ProjectDescriptor reread = read(written);
		assertTrue(message(validate(reread)), validate(reread).isValid());
		assertEquals(descriptor.toString(), reread.toString());
	}

	@Test
	public void invalidDurationsAreRejectedAtLoadAndNeverReplacedByZero() throws IOException {
		final String yaml = HEAD
				+ "user_paths:\n- name: MyUserPath\n  actions:\n    steps:\n"
				+ "    - delay: 99999999999999999999\n"
				+ "    - web_page:\n        think_time: abc\n        steps:\n        - request:\n            url: http://host:80/\n"
				+ "scenarios:\n- name: MyScenario\n  monitoring:\n    before: 5x\n"
				+ "  populations:\n  - name: MyPopulation\n    constant_load:\n      users: 1\n      rampup: -5\n      stop_after: abc\n";

		final ProjectDescriptor descriptor = read(yaml);

		final Validation validation = validate(descriptor);
		assertFalse(validation.isValid());
		final String message = message(validation);
		assertTrue(message, message.startsWith("Data Model is invalid. Violation Number: 5."));
		for (final String part : new String[] {"steps[0].delay", "'99999999999999999999' is not a valid duration", "steps[1].think_time", "'abc' is not a valid duration",
				"monitoring.before", "'5x' is not a valid duration", "rampup", "'-5' is not a valid duration", "stop_after"}) {
			assertTrue(message + " / " + part, message.contains(part));
		}
		assertEquals(2, count(message, "'abc' is not a valid duration"));
		assertEquals("99999999999999999999", delay(actions(descriptor).getSteps().get(0)).toText());
		assertTrue(delay(actions(descriptor).getSteps().get(0)).isInvalid());
		assertEquals(SecondsDuration.parse("5x"), scenario(descriptor).getMonitoringParameters().get().getBeforeFirstVu());
		assertEquals(SecondsDuration.parse("-5"), policy(descriptor, 0).getRampup());
		assertEquals(SecondsDuration.parse("abc"), policy(descriptor, 0).getStopAfter().getValue());
	}

	@Test
	public void savingAProjectHoldingInvalidDurationTextDoesNotCrash() throws IOException {
		final String yaml = USER_PATH + "    - delay: abc\n    - wait_until:\n        conditions:\n        - \"'${a}' == 'b'\"\n        timeout: 5x\n"
				+ "scenarios:\n- name: MyScenario\n  populations:\n  - name: MyPopulation\n    constant_load:\n      users: 1\n      rampup: 1s1h\n";

		final String written = write(read(yaml));

		assertTrue(written, written.contains("delay: abc"));
		assertTrue(written, written.contains("timeout: 5x"));
		assertTrue(written, written.contains("rampup: 1s1h"));
	}

	@Test
	public void savingAndReopeningKeepsDurationsAndWritesSecondBasedFieldsCompactly() throws IOException {
		final String yaml = USER_PATH + "    - delay: 90000\n"
				+ "scenarios:\n- name: MyScenario\n  populations:\n  - name: MyPopulation\n    constant_load:\n      users: 1\n      duration: 3600\n      rampup: 90\n"
				+ "  rendezvous_policies:\n  - name: rdv\n    timeout: 61\n"
				+ "variables:\n- current_date:\n    name: MyDate\n    offset: -5d\n";

		final ProjectDescriptor first = read(yaml);
		final String written = write(first);

		assertTrue(written, written.contains("rampup: 1m30s"));
		assertTrue(written, written.contains("duration: 1h"));
		assertTrue(written, written.contains("timeout: 1m1s"));
		assertTrue(written, written.contains("delay: 1m30s"));
		assertTrue(written, written.contains("offset: -5d"));
		final ProjectDescriptor reopened = read(written);
		assertEquals(first.getProject(), reopened.getProject());
		assertEquals(SecondsDuration.of(90), policy(reopened, 0).getRampup());
		assertEquals(SecondsDuration.of(3600), policy(reopened, 0).getDuration().getValue());
		assertEquals(SecondsDuration.of(61), scenario(reopened).getRendezvousPolicies().get(0).getTimeout());
		assertEquals(MillisecondsDuration.of(90000), delay(actions(reopened).getSteps().get(0)));
	}

	@Test
	public void dateOffsetsKeepAcceptingNegativeValuesAndEveryUnit() throws IOException {
		final String[] texts = {"-5d", "10ms", "3s", "-2m", "4h", "1mo", "-1y"};
		final int[] amounts = {-5, 10, 3, -2, 4, 1, -1};
		final IncrementTimeUnit[] units = {IncrementTimeUnit.DAY, IncrementTimeUnit.MILLISECOND, IncrementTimeUnit.SECOND, IncrementTimeUnit.MINUTE,
				IncrementTimeUnit.HOUR, IncrementTimeUnit.MONTH, IncrementTimeUnit.YEAR};
		for (int i = 0; i < texts.length; i++) {
			final String yaml = HEAD + "variables:\n- current_date:\n    name: MyCurrentDate\n    offset: " + texts[i]
					+ "\n- date:\n    name: MyDate\n    start_date: 01/01/2024 00:00:00\n    change_step: " + texts[i] + "\n";

			final ProjectDescriptor descriptor = read(yaml);
			final Offset expected = Offset.of(amounts[i], units[i]);
			final Project project = descriptor.getProject();
			assertEquals(texts[i], Optional.of(expected), ((CurrentDateVariable) project.getVariables().get(0)).getOffset());
			assertEquals(texts[i], Optional.of(expected), ((DateVariable) project.getVariables().get(1)).getChangeStep());
			assertTrue(texts[i] + message(validate(descriptor)), validate(descriptor).isValid());

			final String written = write(descriptor);
			assertEquals(texts[i], 2, count(written, ": " + texts[i] + "\n"));
			assertEquals(texts[i], descriptor.getProject(), read(written).getProject());
		}
	}

	@Test
	public void invalidDateOffsetsAreReportedAndKept() throws IOException {
		final String yaml = HEAD + "variables:\n- current_date:\n    name: MyCurrentDate\n    offset: 5x\n"
				+ "- date:\n    name: MyDate\n    start_date: 01/01/2024 00:00:00\n    change_step: d5\n";

		final ProjectDescriptor descriptor = read(yaml);

		final Validation validation = validate(descriptor);
		assertFalse(validation.isValid());
		final String message = message(validation);
		assertTrue(message, message.contains("variables[0].offset"));
		assertTrue(message, message.contains("'5x' is not a valid offset"));
		assertTrue(message, message.contains("variables[1].change_step"));
		assertTrue(message, message.contains("'d5' is not a valid offset"));
		final String written = write(descriptor);
		assertTrue(written, written.contains("offset: 5x"));
		assertTrue(written, written.contains("change_step: d5"));
	}

	@Test
	public void gettersKeepTheirNamesAndReturnTheTypedModel() throws IOException {
		final String yaml = USER_PATH + "    - delay: 1s\n    - delay: ${d}\n"
				+ "scenarios:\n- name: MyScenario\n  populations:\n  - name: MyPopulation\n    constant_load:\n      users: 1\n      rampup: 2m\n"
				+ "variables:\n- current_date:\n    name: MyCurrentDate\n    offset: 3h\n";

		final ProjectDescriptor descriptor = read(yaml);

		assertEquals(MillisecondsDuration.of(1000), delay(actions(descriptor).getSteps().get(0)));
		assertEquals(MillisecondsDuration.ofVariable("d"), delay(actions(descriptor).getSteps().get(1)));
		assertEquals(SecondsDuration.of(120), policy(descriptor, 0).getRampup());
		assertEquals(Optional.of(Offset.of(3, IncrementTimeUnit.HOUR)), ((CurrentDateVariable) descriptor.getProject().getVariables().get(0)).getOffset());
		for (final Class<?> type : new Class<?>[] {DurationConstant.class, DurationRandom.class, WaitUntil.class, CurrentDateVariable.class, DateVariable.class}) {
			for (final Method method : type.getMethods()) {
				assertFalse(method.getName(), method.getName().contains("InMsOrVariable") || method.getName().startsWith("getParsed"));
			}
		}
	}
}
