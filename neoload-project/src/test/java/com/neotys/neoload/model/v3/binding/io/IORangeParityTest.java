package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The literal range rules of the v3.1 as-code fields: schema 3.1 and the model, read with {@link IO} and checked
 * with {@link Validator}, judge the same literal the same way at every boundary.
 */
public class IORangeParityTest {
	private static final Validator VALIDATOR = new Validator();
	private static final YAMLMapper YAML_MAPPER = new YAMLMapper();

	private static JsonSchema SCHEMA;

	private static final String VALID = "valid";
	private static final String INVALID = "invalid";

	private static final String CONSTANT_USERS = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: %s\n");
	private static final String CONSTANT_DURATION = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      duration: %s\n");
	private static final String CONSTANT_START_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: %s\n");
	private static final String CONSTANT_STOP_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      stop_after: %s\n");
	private static final String RAMPUP_MIN_USERS = rampup("%s", "1", "1 iteration");
	private static final String RAMPUP_INCREMENT_USERS = rampup("1", "%s", "1 iteration");
	private static final String RAMPUP_INCREMENT_EVERY = rampup("1", "1", "%s");
	private static final String PEAKS_MINIMUM_USERS = peaks("%s", "1 iteration", "1", "1 iteration");
	private static final String PEAKS_MAXIMUM_USERS = peaks("1", "1 iteration", "%s", "1 iteration");
	private static final String PEAKS_MAXIMUM_DURATION = peaks("1", "1 iteration", "5", "%s");
	private static final String CUSTOM_WHEN = DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      steps:\n      - when: %s\n        users: 1\n      - when: 10s\n        users: 2\n");
	private static final String CUSTOM_USERS = DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      steps:\n      - when: 0s\n        users: %s\n      - when: 10s\n        users: 1\n");
	private static final String RENDEZVOUS_WHEN = DurationFields.scenario("  rendezvous_policies:\n  - name: r\n    when: %s\n");
	private static final String SERVER_PORT = "name: P\nservers:\n- name: S\n  host: myserver.intranet.neotys.com\n  port: %s\n";
	private static final String LOOP_COUNT = DurationFields.actions("    - loop:\n        count: %s\n        steps:\n        - delay: 1s\n");
	private static final String DURATION_ASSERTION = DurationFields.actions("    - request:\n        url: http://host:80/\n        duration_assertion:\n          less_than: %s\n");
	private static final String SIZE_EQUALS = sizeAssertion("equals");
	private static final String SIZE_GREATER_THAN = sizeAssertion("greater_than");
	private static final String SIZE_LESS_THAN = sizeAssertion("less_than");
	private static final String COUNTER_START = variable("counter", "start: %s\n    end: 10\n    increment: 1\n    scope: local\n");
	private static final String COUNTER_END = variable("counter", "start: 0\n    end: %s\n    increment: 1\n    scope: local\n");
	private static final String COUNTER_INCREMENT = variable("counter", "start: 0\n    end: 10\n    increment: %s\n    scope: local\n");
	private static final String RANDOM_NUMBER_MIN = variable("random_number", "min: %s\n    max: 10\n    predictable: false\n");
	private static final String RANDOM_NUMBER_MAX = variable("random_number", "min: -10\n    max: %s\n    predictable: false\n");
	private static final String RANDOM_STRING_MIN = variable("random_string", "min_length: %s\n    max_length: 10\n    predictable: false\n");
	private static final String RANDOM_STRING_MAX = variable("random_string", "min_length: 1\n    max_length: %s\n    predictable: false\n");
	private static final String FILE_START_FROM_LINE = variable("file", "path: data/cities.csv\n    is_first_line_column_names: true\n    start_from_line: %s\n");
	private static final String LIST_START_FROM_LINE = variable("list", "column_names:\n    - city\n    values:\n    - - Paris\n    start_from_line: %s\n");
	private static final String QUEUE_SIZE = variable("shared_queue", "queue_size: %s\n    consumer_timeout: 5000\n");
	private static final String CONSUMER_TIMEOUT = variable("shared_queue", "queue_size: 10\n    consumer_timeout: %s\n");
	private static final String CURRENT_DATE_OFFSET = variable("current_date", "offset: %s\n");
	private static final String DATE_CHANGE_STEP = variable("date", "pattern: yyyy-MM-dd'T'HH:mm:ss\n    start_date: 2024-01-01T00:00:00\n    change_step: %s\n");
	private static final String SLA_THRESHOLD = "name: P\nsla_profiles:\n- name: S\n  thresholds:\n  - avg-request-resp-time warn >= %s per test\n";
	private static final String SLA_PERCENTILE = "name: P\nsla_profiles:\n- name: S\n  thresholds:\n  - perc-transaction-resp-time (p%s) warn >= 1 per test\n";
	private static final String DISTRIBUTION = "name: P\npopulations:\n- name: Pop\n  user_paths:\n  - name: U\n    distribution: %s\n";
	private static final String QTEST_PROJECT_ID = "name: P\nproject_settings:\n  qtest.project_id: %s\n";
	private static final String QTEST_CYCLE_ID = "name: P\nproject_settings:\n  qtest.cycle_id: %s\n";

	private static String rampup(final String minUsers, final String incrementUsers, final String incrementEvery) {
		return DurationFields.scenario("  populations:\n  - name: R\n    rampup_load:\n      min_users: " + minUsers + "\n      max_users: 5\n      increment_users: " + incrementUsers
				+ "\n      increment_every: " + incrementEvery + "\n      duration: 15 iterations\n");
	}

	private static String peaks(final String minUsers, final String minDuration, final String maxUsers, final String maxDuration) {
		return DurationFields.scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: " + minUsers + "\n        duration: " + minDuration
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
		final Path moduleDir = Paths.get(IORangeParityTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent();
		final Path schemaPath = moduleDir.resolve("../schemas/v3.1/as-code.schema.json").normalize();
		SCHEMA = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemaPath.toUri());
	}

	private static boolean schemaAccepts(final String yaml) throws IOException {
		final JsonNode node = YAML_MAPPER.readTree(yaml);
		return SCHEMA.validate(node).isEmpty();
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
		rows.add(row("constant_load.users", CONSTANT_USERS, "0", INVALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "1", VALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "2147483647", VALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "2147483648", INVALID));
		rows.add(row("rampup_load.increment_users", RAMPUP_INCREMENT_USERS, "0", INVALID));
		rows.add(row("rampup_load.increment_users", RAMPUP_INCREMENT_USERS, "1", VALID));
		rows.add(row("peaks_load.maximum.users", PEAKS_MAXIMUM_USERS, "0", INVALID));
		rows.add(row("peaks_load.maximum.users", PEAKS_MAXIMUM_USERS, "1", VALID));
		rows.add(row("rampup_load.min_users", RAMPUP_MIN_USERS, "0", INVALID));
		rows.add(row("rampup_load.min_users", RAMPUP_MIN_USERS, "1", VALID));
		rows.add(row("peaks_load.minimum.users", PEAKS_MINIMUM_USERS, "0", INVALID));
		rows.add(row("peaks_load.minimum.users", PEAKS_MINIMUM_USERS, "1", VALID));
		rows.add(row("rampup", DurationFields.CONSTANT_RAMPUP.yaml("%s"), "0", INVALID));
		rows.add(row("rampup", DurationFields.CONSTANT_RAMPUP.yaml("%s"), "1", VALID));
		rows.add(row("rampup", DurationFields.CONSTANT_RAMPUP.yaml("%s"), "2147483", VALID));
		rows.add(row("rampup", DurationFields.CONSTANT_RAMPUP.yaml("%s"), "2147484", INVALID));
		rows.add(row("increment_rampup", DurationFields.RAMPUP_INCREMENT_RAMPUP.yaml("%s"), "0", INVALID));
		rows.add(row("increment_rampup", DurationFields.RAMPUP_INCREMENT_RAMPUP.yaml("%s"), "2147483", VALID));
		rows.add(row("increment_rampup", DurationFields.RAMPUP_INCREMENT_RAMPUP.yaml("%s"), "2147484", INVALID));
		rows.add(row("step_rampup", DurationFields.PEAKS_STEP_RAMPUP.yaml("%s"), "0", INVALID));
		rows.add(row("step_rampup", DurationFields.PEAKS_STEP_RAMPUP.yaml("%s"), "2147483", VALID));
		rows.add(row("step_rampup", DurationFields.PEAKS_STEP_RAMPUP.yaml("%s"), "2147484", INVALID));
		rows.add(row("start_after", CONSTANT_START_AFTER, "0", INVALID));
		rows.add(row("start_after", CONSTANT_START_AFTER, "1", VALID));
		rows.add(row("start_after", CONSTANT_START_AFTER, "2147483", VALID));
		rows.add(row("start_after", CONSTANT_START_AFTER, "2147484", INVALID));
		rows.add(row("stop_after", CONSTANT_STOP_AFTER, "0", INVALID));
		rows.add(row("stop_after", CONSTANT_STOP_AFTER, "1", VALID));
		rows.add(row("stop_after", CONSTANT_STOP_AFTER, "2147483", VALID));
		rows.add(row("stop_after", CONSTANT_STOP_AFTER, "2147484", INVALID));
		rows.add(row("monitoring.before", DurationFields.MONITORING_BEFORE.yaml("%s"), "0", INVALID));
		rows.add(row("monitoring.before", DurationFields.MONITORING_BEFORE.yaml("%s"), "2147483", VALID));
		rows.add(row("monitoring.before", DurationFields.MONITORING_BEFORE.yaml("%s"), "2147484", INVALID));
		rows.add(row("monitoring.after", DurationFields.MONITORING_AFTER.yaml("%s"), "0", INVALID));
		rows.add(row("monitoring.after", DurationFields.MONITORING_AFTER.yaml("%s"), "2147483", VALID));
		rows.add(row("monitoring.after", DurationFields.MONITORING_AFTER.yaml("%s"), "2147484", INVALID));
		rows.add(row("increment_every", RAMPUP_INCREMENT_EVERY, "0s", INVALID));
		rows.add(row("increment_every", RAMPUP_INCREMENT_EVERY, "0 iterations", INVALID));
		rows.add(row("increment_every", RAMPUP_INCREMENT_EVERY, "1s", VALID));
		rows.add(row("increment_every", RAMPUP_INCREMENT_EVERY, "1 iteration", VALID));
		rows.add(row("population duration", CONSTANT_DURATION, "0s", INVALID));
		rows.add(row("population duration", CONSTANT_DURATION, "0 iterations", INVALID));
		rows.add(row("population duration", CONSTANT_DURATION, "1s", VALID));
		rows.add(row("population duration", CONSTANT_DURATION, "1 iteration", VALID));
		rows.add(row("peaks_load.maximum.duration", PEAKS_MAXIMUM_DURATION, "0s", INVALID));
		rows.add(row("peaks_load.maximum.duration", PEAKS_MAXIMUM_DURATION, "0 iterations", INVALID));
		rows.add(row("peaks_load.maximum.duration", PEAKS_MAXIMUM_DURATION, "1s", VALID));
		rows.add(row("custom_load.steps[].when", CUSTOM_WHEN, "0", VALID));
		rows.add(row("custom_load.steps[].when", CUSTOM_WHEN, "0s", VALID));
		rows.add(row("custom_load.steps[].users", CUSTOM_USERS, "0", VALID));
		rows.add(row("custom_load.steps[].users", CUSTOM_USERS, "-1", INVALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "0", INVALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "1", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "1%", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "100%", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "101%", INVALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "0", INVALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "1", VALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "2147483", VALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "2147484", INVALID));
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "0", INVALID));
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "1", VALID));
		rows.add(row("think_time", DurationFields.THINK_TIME.yaml("%s"), "0", INVALID));
		rows.add(row("think_time", DurationFields.THINK_TIME.yaml("%s"), "1", VALID));
		rows.add(row("web page think_time", DurationFields.WEB_PAGE_THINK_TIME.yaml("%s"), "0", INVALID));
		rows.add(row("web page think_time", DurationFields.WEB_PAGE_THINK_TIME.yaml("%s"), "1", VALID));
		rows.add(row("pacing", DurationFields.PACING.yaml("%s"), "0", INVALID));
		rows.add(row("pacing", DurationFields.PACING.yaml("%s"), "1", VALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "0", INVALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "1", VALID));
		rows.add(row("loop.count", LOOP_COUNT, "0", INVALID));
		rows.add(row("loop.count", LOOP_COUNT, "1", VALID));
		rows.add(row("servers[].port", SERVER_PORT, "0", INVALID));
		rows.add(row("servers[].port", SERVER_PORT, "1", VALID));
		rows.add(row("servers[].port", SERVER_PORT, "65535", VALID));
		rows.add(row("servers[].port", SERVER_PORT, "65536", INVALID));
		rows.add(row("servers[].port", SERVER_PORT, "70000", INVALID));
		rows.add(row("duration_assertion.less_than", DURATION_ASSERTION, "0", INVALID));
		rows.add(row("duration_assertion.less_than", DURATION_ASSERTION, "1", VALID));
		rows.add(row("size_assertion.equals", SIZE_EQUALS, "0", INVALID));
		rows.add(row("size_assertion.equals", SIZE_EQUALS, "1", VALID));
		rows.add(row("size_assertion.greater_than", SIZE_GREATER_THAN, "0", INVALID));
		rows.add(row("size_assertion.greater_than", SIZE_GREATER_THAN, "1", VALID));
		rows.add(row("size_assertion.less_than", SIZE_LESS_THAN, "0", INVALID));
		rows.add(row("size_assertion.less_than", SIZE_LESS_THAN, "1", VALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "0", INVALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "-1", VALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "1", VALID));
		rows.add(row("counter.start", COUNTER_START, "-10", VALID));
		rows.add(row("counter.start", COUNTER_START, "1.5", INVALID));
		rows.add(row("counter.end", COUNTER_END, "-5", VALID));
		rows.add(row("random_number.min", RANDOM_NUMBER_MIN, "-10", VALID));
		rows.add(row("random_number.max", RANDOM_NUMBER_MAX, "-1", VALID));
		rows.add(row("random_string.min_length", RANDOM_STRING_MIN, "0", INVALID));
		rows.add(row("random_string.min_length", RANDOM_STRING_MIN, "1", VALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX, "1000", VALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX, "1001", INVALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX, "2000", INVALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX, "-1", INVALID));
		rows.add(row("file start_from_line", FILE_START_FROM_LINE, "0", INVALID));
		rows.add(row("file start_from_line", FILE_START_FROM_LINE, "1", VALID));
		rows.add(row("list start_from_line", LIST_START_FROM_LINE, "0", INVALID));
		rows.add(row("list start_from_line", LIST_START_FROM_LINE, "1", VALID));
		rows.add(row("shared_queue.queue_size", QUEUE_SIZE, "0", INVALID));
		rows.add(row("shared_queue.queue_size", QUEUE_SIZE, "1", VALID));
		rows.add(row("shared_queue.consumer_timeout", CONSUMER_TIMEOUT, "0", VALID));
		rows.add(row("shared_queue.consumer_timeout", CONSUMER_TIMEOUT, "-1", INVALID));
		rows.add(row("current_date.offset", CURRENT_DATE_OFFSET, "0d", INVALID));
		rows.add(row("current_date.offset", CURRENT_DATE_OFFSET, "-5d", VALID));
		rows.add(row("current_date.offset", CURRENT_DATE_OFFSET, "5d", VALID));
		rows.add(row("date.change_step", DATE_CHANGE_STEP, "0s", INVALID));
		rows.add(row("date.change_step", DATE_CHANGE_STEP, "-1h", VALID));
		rows.add(row("date.change_step", DATE_CHANGE_STEP, "-5d", VALID));
		rows.add(row("SLA threshold value", SLA_THRESHOLD, "0", VALID));
		rows.add(row("SLA threshold value", SLA_THRESHOLD, "-1", INVALID));
		rows.add(row("SLA percentile", SLA_PERCENTILE, "0", INVALID));
		rows.add(row("SLA percentile", SLA_PERCENTILE, "1", VALID));
		rows.add(row("SLA percentile", SLA_PERCENTILE, "99", VALID));
		rows.add(row("SLA percentile", SLA_PERCENTILE, "100", INVALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "0", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "100%", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "101%", INVALID));
		rows.add(row("qtest.project_id", QTEST_PROJECT_ID, "0", INVALID));
		rows.add(row("qtest.project_id", QTEST_PROJECT_ID, "1", VALID));
		rows.add(row("qtest.cycle_id", QTEST_CYCLE_ID, "0", INVALID));
		rows.add(row("qtest.cycle_id", QTEST_CYCLE_ID, "1", VALID));
		return rows;
	}

	private static List<String[]> negatives() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("servers[].port", SERVER_PORT, "-80", INVALID));
		rows.add(row("servers[].port", SERVER_PORT, "\"-80\"", INVALID));
		rows.add(row("loop.count", LOOP_COUNT, "\"-1\"", INVALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "\"-5\"", INVALID));
		rows.add(row("start_after", CONSTANT_START_AFTER, "\"-5\"", INVALID));
		rows.add(row("duration_assertion.less_than", DURATION_ASSERTION, "-1", INVALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX, "-1", INVALID));
		rows.add(row("counter.end", COUNTER_END, "-5", VALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "-1", VALID));
		rows.add(row("random_number.max", RANDOM_NUMBER_MAX, "-1", VALID));
		rows.add(row("current_date.offset", CURRENT_DATE_OFFSET, "-5d", VALID));
		rows.add(row("date.change_step", DATE_CHANGE_STEP, "-5d", VALID));
		return rows;
	}

	private static List<String[]> variables() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("servers[].port", SERVER_PORT, "\"${myVar}\"", VALID));
		rows.add(row("loop.count", LOOP_COUNT, "\"${myVar}\"", VALID));
		rows.add(row("delay", DurationFields.DELAY.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("think_time", DurationFields.THINK_TIME.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("pacing", DurationFields.PACING.yaml("%s"), "\"${myVar}\"", VALID));
		rows.add(row("wait_until.timeout", DurationFields.WAIT_UNTIL_TIMEOUT.yaml("%s"), "\"${myVar}\"", VALID));
		return rows;
	}

	private static void assertParity(final List<String[]> rows) throws IOException {
		final List<String> failures = new ArrayList<>();
		for (final String[] row : rows) {
			final String yaml = row[1].replace("%s", row[2]);
			final boolean expected = VALID.equals(row[3]);
			final boolean schema = schemaAccepts(yaml);
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
