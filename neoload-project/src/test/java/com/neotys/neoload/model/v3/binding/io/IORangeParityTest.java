package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The literal range rules of the as-code fields: schema 3.2 and the model, read with {@link IO} and checked
 * with {@link Validator}, judge the same literal the same way at every boundary.
 */
public class IORangeParityTest {
	private static final Validator VALIDATOR = new Validator();

	private static SchemaFiles SCHEMA;

	private static final String VALID = "valid";
	private static final String INVALID = "invalid";

	private static final String CONSTANT_USERS = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: %s\n");
	private static final String CONSTANT_DURATION = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      duration: %s\n");
	private static final String RAMPUP_MAX_USERS = rampup("%s", "1", "1 iteration");
	private static final String RAMPUP_INCREMENT_USERS = rampup("5", "%s", "1 iteration");
	private static final String RAMPUP_INCREMENT_EVERY = rampup("5", "1", "%s");
	private static final String PEAKS_MAXIMUM_USERS = peaks("1 iteration", "%s", "1 iteration");
	private static final String PEAKS_MINIMUM_DURATION = peaks("%s", "5", "1 iteration");
	private static final String PEAKS_MAXIMUM_DURATION = peaks("1 iteration", "5", "%s");
	private static final String CUSTOM_WHEN = DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      steps:\n      - when: %s\n        users: 1\n      - when: 10s\n        users: 2\n");
	private static final String CUSTOM_USERS = DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      steps:\n      - when: 0s\n        users: %s\n      - when: 10s\n        users: 1\n");
	private static final String LOOP_COUNT = DurationFields.actions("    - loop:\n        count: %s\n        steps:\n        - delay: 1s\n");
	private static final String DELAY_RANDOM_MIN = DurationFields.actions("    - delay:\n        min: %s\n        max: 2s\n");
	private static final String THINK_TIME_RANDOM_MIN = DurationFields.actions("    - think_time:\n        min: %s\n        max: 2s\n");
	private static final String THINK_TIME_RANDOM_MAX = DurationFields.actions("    - think_time:\n        max: %s\n");
	private static final String DURATION_ASSERTION = DurationFields.actions("    - request:\n        url: http://host:80/\n        duration_assertion:\n          less_than: %s\n");
	private static final String SIZE_EQUALS = sizeAssertion("equals");
	private static final String SIZE_GREATER_THAN = sizeAssertion("greater_than");
	private static final String SIZE_LESS_THAN = sizeAssertion("less_than");
	private static final String COUNTER_INCREMENT = variable("counter", "start: 0\n    end: 10\n    increment: %s\n    scope: local\n");
	private static final String FILE_START_FROM_LINE = variable("file", "path: data/cities.csv\n    is_first_line_column_names: true\n    start_from_line: %s\n");
	private static final String LIST_START_FROM_LINE = variable("list", "column_names:\n    - city\n    values:\n    - - Paris\n    start_from_line: %s\n");
	private static final String CONSUMER_TIMEOUT = variable("shared_queue", "queue_size: 10\n    consumer_timeout: %s\n");

	private static String rampup(final String maxUsers, final String incrementUsers, final String incrementEvery) {
		return DurationFields.scenario("  populations:\n  - name: R\n    rampup_load:\n      min_users: 1\n      max_users: " + maxUsers + "\n      increment_users: " + incrementUsers
				+ "\n      increment_every: " + incrementEvery + "\n      duration: 15 iterations\n");
	}

	private static String peaks(final String minDuration, final String maxUsers, final String maxDuration) {
		return DurationFields.scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: 1\n        duration: " + minDuration
				+ "\n      maximum:\n        users: " + maxUsers + "\n        duration: " + maxDuration + "\n      start: maximum\n      duration: 15 iterations\n");
	}

	private static String sizeAssertion(final String key) {
		return DurationFields.actions("    - request:\n        url: http://host:80/\n        size_assertion:\n          " + key + ": %s\n");
	}

	private static String variable(final String type, final String properties) {
		return "name: P\nvariables:\n- " + type + ":\n    name: V\n    " + properties;
	}

	@BeforeClass
	public static void loadSchema() throws URISyntaxException {
		SCHEMA = SchemaFiles.version("v3.2");
	}

	private static boolean modelAccepts(final String yaml) {
		try {
			final ProjectDescriptor descriptor = new IO().read(yaml);
			return VALIDATOR.validate(descriptor, NeoLoad.class).isValid();
		} catch (final IOException e) {
			return false;
		}
	}

	private static String[] row(final String field, final String template, final String value, final String outcome) {
		return new String[] {field, template, value, outcome};
	}

	private static List<String[]> boundaries() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("loop.count", LOOP_COUNT, "0", INVALID));
		rows.add(row("loop.count", LOOP_COUNT, "1", VALID));
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "0", INVALID));
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "1", VALID));
		rows.add(row("think_time", DurationFields.THINK_TIME.yaml("%s"), "0", INVALID));
		rows.add(row("think_time", DurationFields.THINK_TIME.yaml("%s"), "1", VALID));
		rows.add(row("think_time random max", THINK_TIME_RANDOM_MAX, "0", INVALID));
		rows.add(row("think_time random max", THINK_TIME_RANDOM_MAX, "1", VALID));
		rows.add(row("web page think_time", DurationFields.WEB_PAGE_THINK_TIME.yaml("%s"), "0", INVALID));
		rows.add(row("web page think_time", DurationFields.WEB_PAGE_THINK_TIME.yaml("%s"), "1", VALID));
		rows.add(row("pacing", DurationFields.PACING.yaml("%s"), "0", INVALID));
		rows.add(row("pacing", DurationFields.PACING.yaml("%s"), "1", VALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "0", INVALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "1", VALID));
		rows.add(row("duration_assertion.less_than", DURATION_ASSERTION, "0", INVALID));
		rows.add(row("duration_assertion.less_than", DURATION_ASSERTION, "1", VALID));
		rows.add(row("size_assertion.equals", SIZE_EQUALS, "0", INVALID));
		rows.add(row("size_assertion.equals", SIZE_EQUALS, "1", VALID));
		rows.add(row("size_assertion.greater_than", SIZE_GREATER_THAN, "0", INVALID));
		rows.add(row("size_assertion.greater_than", SIZE_GREATER_THAN, "1", VALID));
		rows.add(row("size_assertion.less_than", SIZE_LESS_THAN, "0", INVALID));
		rows.add(row("size_assertion.less_than", SIZE_LESS_THAN, "1", VALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "0", INVALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "1", VALID));
		rows.add(row("rampup_load.max_users", RAMPUP_MAX_USERS, "0", INVALID));
		rows.add(row("rampup_load.max_users", RAMPUP_MAX_USERS, "1", VALID));
		rows.add(row("rampup_load.increment_users", RAMPUP_INCREMENT_USERS, "0", INVALID));
		rows.add(row("rampup_load.increment_users", RAMPUP_INCREMENT_USERS, "1", VALID));
		rows.add(row("peaks_load.maximum.users", PEAKS_MAXIMUM_USERS, "0", INVALID));
		rows.add(row("peaks_load.maximum.users", PEAKS_MAXIMUM_USERS, "1", VALID));
		rows.add(row("file start_from_line", FILE_START_FROM_LINE, "0", INVALID));
		rows.add(row("file start_from_line", FILE_START_FROM_LINE, "1", VALID));
		rows.add(row("list start_from_line", LIST_START_FROM_LINE, "0", INVALID));
		rows.add(row("list start_from_line", LIST_START_FROM_LINE, "1", VALID));
		rows.add(row("population duration", CONSTANT_DURATION, "0s", INVALID));
		rows.add(row("population duration", CONSTANT_DURATION, "1s", VALID));
		rows.add(row("population duration", CONSTANT_DURATION, "0 iterations", INVALID));
		rows.add(row("population duration", CONSTANT_DURATION, "1 iteration", VALID));
		rows.add(row("increment_every", RAMPUP_INCREMENT_EVERY, "0s", INVALID));
		rows.add(row("increment_every", RAMPUP_INCREMENT_EVERY, "1s", VALID));
		rows.add(row("peaks_load.maximum.duration", PEAKS_MAXIMUM_DURATION, "0s", INVALID));
		rows.add(row("peaks_load.maximum.duration", PEAKS_MAXIMUM_DURATION, "1s", VALID));
		rows.add(row("peaks_load.minimum.duration", PEAKS_MINIMUM_DURATION, "0 iterations", INVALID));
		rows.add(row("delay random min", DELAY_RANDOM_MIN, "0", VALID));
		rows.add(row("think_time random min", THINK_TIME_RANDOM_MIN, "0", VALID));
		rows.add(row("custom_load.steps[].when", CUSTOM_WHEN, "0", VALID));
		rows.add(row("custom_load.steps[].users", CUSTOM_USERS, "0", VALID));
		rows.add(row("think_time.override", DurationFields.USER_PATH_THINK_TIME_OVERRIDE.yaml("%s"), "0", VALID));
		rows.add(row("shared_queue.consumer_timeout", CONSUMER_TIMEOUT, "0", VALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "0", INVALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "1", VALID));
		return rows;
	}

	private static List<String[]> negatives() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "-1", INVALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "-1", INVALID));
		rows.add(row("duration_assertion.less_than", DURATION_ASSERTION, "-1", INVALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "-1", INVALID));
		rows.add(row("delay random min", DELAY_RANDOM_MIN, "-1", INVALID));
		rows.add(row("think_time random min", THINK_TIME_RANDOM_MIN, "-1", INVALID));
		rows.add(row("custom_load.steps[].users", CUSTOM_USERS, "-1", INVALID));
		rows.add(row("think_time.override", DurationFields.USER_PATH_THINK_TIME_OVERRIDE.yaml("%s"), "-1", INVALID));
		rows.add(row("shared_queue.consumer_timeout", CONSUMER_TIMEOUT, "-1", INVALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "-1", VALID));
		return rows;
	}

	private static List<String[]> variables() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("loop.count", LOOP_COUNT, "\"${myVar}\"", VALID));
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("think_time", DurationFields.THINK_TIME.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("think_time random min", THINK_TIME_RANDOM_MIN, "\"${myVar}\"", VALID));
		rows.add(row("think_time random max", THINK_TIME_RANDOM_MAX, "\"${myVar}\"", VALID));
		rows.add(row("web page think_time", DurationFields.WEB_PAGE_THINK_TIME.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("pacing", DurationFields.PACING.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("think_time.override", DurationFields.USER_PATH_THINK_TIME_OVERRIDE.yaml("%s"), "\"${myVar}\"", VALID));
		return rows;
	}

	private static void assertParity(final List<String[]> rows) throws IOException {
		final List<String> failures = new ArrayList<>();
		for (final String[] row : rows) {
			final String yaml = row[1].replace("%s", row[2]);
			final boolean expected = VALID.equals(row[3]);
			final boolean schema = SCHEMA.accepts(yaml);
			final boolean model = modelAccepts(yaml);
			if (schema != expected || model != expected) {
				failures.add(row[0] + " = " + row[2] + ": expected " + row[3] + ", schema " + (schema ? VALID : INVALID) + ", model " + (model ? VALID : INVALID));
			}
		}
		assertEquals(String.join("\n", failures), 0, failures.size());
	}

	@Test
	public void boundaryLiteralIsJudgedTheSameBySchemaAndModel() throws IOException {
		assertParity(boundaries());
	}

	@Test
	public void negativeLiteralIsRejectedExceptWhereItHasAMeaning() throws IOException {
		assertParity(negatives());
	}

	@Test
	public void variableReferenceStaysAcceptedOnEveryCheckedField() throws IOException {
		assertParity(variables());
	}
}
