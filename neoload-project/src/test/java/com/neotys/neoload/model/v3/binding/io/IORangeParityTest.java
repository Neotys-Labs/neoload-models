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
	private static final String RAMPUP_MAX_USERS = rampup("%s", "1");
	private static final String RAMPUP_INCREMENT_USERS = rampup("5", "%s");
	private static final String PEAKS_MAXIMUM_USERS = DurationFields.scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: 1\n        duration: 1 iteration\n"
			+ "      maximum:\n        users: %s\n        duration: 1 iteration\n      start: maximum\n      duration: 15 iterations\n");
	private static final String CUSTOM_USERS = DurationFields.scenario("  populations:\n  - name: C\n    custom_load:\n      steps:\n      - when: 0s\n        users: %s\n      - when: 10s\n        users: 1\n");
	private static final String RENDEZVOUS_WHEN = DurationFields.scenario("  rendezvous_policies:\n  - name: r\n    when: %s\n");
	private static final String FILE_START_FROM_LINE = variable("file", "path: data/cities.csv\n    is_first_line_column_names: true\n    start_from_line: %s\n");
	private static final String LIST_START_FROM_LINE = variable("list", "column_names:\n    - city\n    values:\n    - - Paris\n    start_from_line: %s\n");
	private static final String DISTRIBUTION = "name: P\npopulations:\n- name: Pop\n  user_paths:\n  - name: U\n    distribution: %s\n";
	private static final String COUNTER_START = variable("counter", "start: %s\n    end: 10\n    increment: 1\n");
	private static final String COUNTER_END = variable("counter", "start: 0\n    end: %s\n    increment: 1\n");
	private static final String RANDOM_NUMBER_MIN = variable("random_number", "min: %s\n    max: 10\n");
	private static final String RANDOM_NUMBER_MAX = variable("random_number", "min: -10\n    max: %s\n");

	private static final String RAMPUP_MIN_USERS = DurationFields.scenario("  populations:\n  - name: R\n    rampup_load:\n      min_users: %s\n      max_users: 2147483647\n      increment_users: 1\n      increment_every: 1 iteration\n      duration: 15 iterations\n");
	private static final String RANDOM_STRING_MIN_LENGTH = variable("random_string", "min_length: %s\n    max_length: 2147483647\n");
	private static final String RANDOM_STRING_MAX_LENGTH = variable("random_string", "min_length: 1\n    max_length: %s\n");
	private static final String SHARED_QUEUE_SIZE = variable("shared_queue", "queue_size: %s\n    consumer_timeout: 5000\n");
	private static final String EXTRACTOR_MATCH_NUMBER = DurationFields.actions("    - request:\n        url: http://host:80/\n        extractors:\n        - name: E\n          regexp: a(.*)\n          match_number: %s\n");
	private static final String TIME_TO_LIVE = "name: P\nweb_services_security:\n  request_profiles:\n  - name: R\n    headers:\n    - tokens:\n      - timestamp:\n          time_to_live: %s\n";

	private static String rampup(final String maxUsers, final String incrementUsers) {
		return DurationFields.scenario("  populations:\n  - name: R\n    rampup_load:\n      min_users: 1\n      max_users: " + maxUsers + "\n      increment_users: " + incrementUsers
				+ "\n      increment_every: 1 iteration\n      duration: 15 iterations\n");
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
		rows.add(row("constant_load.users", CONSTANT_USERS, "2147483647", VALID));
		rows.add(row("constant_load.users", CONSTANT_USERS, "2147483648", INVALID));
		rows.add(row("rampup_load.max_users", RAMPUP_MAX_USERS, "2147483647", VALID));
		rows.add(row("rampup_load.max_users", RAMPUP_MAX_USERS, "2147483648", INVALID));
		rows.add(row("rampup_load.increment_users", RAMPUP_INCREMENT_USERS, "2147483647", VALID));
		rows.add(row("rampup_load.increment_users", RAMPUP_INCREMENT_USERS, "2147483648", INVALID));
		rows.add(row("peaks_load.maximum.users", PEAKS_MAXIMUM_USERS, "2147483647", VALID));
		rows.add(row("peaks_load.maximum.users", PEAKS_MAXIMUM_USERS, "2147483648", INVALID));
		rows.add(row("custom_load.steps[].users", CUSTOM_USERS, "2147483647", VALID));
		rows.add(row("custom_load.steps[].users", CUSTOM_USERS, "2147483648", INVALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "2147483647", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "2147483648", INVALID));
		rows.add(row("file start_from_line", FILE_START_FROM_LINE, "2147483647", VALID));
		rows.add(row("file start_from_line", FILE_START_FROM_LINE, "2147483648", INVALID));
		rows.add(row("list start_from_line", LIST_START_FROM_LINE, "2147483647", VALID));
		rows.add(row("list start_from_line", LIST_START_FROM_LINE, "2147483648", INVALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "manual", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "\"50%\"", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "\"100%\"", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "\"25\"", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "\"2147483647\"", VALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "\"${myVar}\"", INVALID));
		rows.add(row("rendezvous_policies[].when", RENDEZVOUS_WHEN, "abc", INVALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "100%", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "100", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "\"100\"", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "99.9%", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "0%", VALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "101%", INVALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "100.1%", INVALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "100.1", INVALID));
		rows.add(row("user_paths[].distribution", DISTRIBUTION, "\"100.1\"", INVALID));
		rows.add(row("counter.start", COUNTER_START, "2147483647", VALID));
		rows.add(row("counter.start", COUNTER_START, "2147483648", INVALID));
		rows.add(row("counter.start", COUNTER_START, "1.5", INVALID));
		rows.add(row("counter.end", COUNTER_END, "-2147483648", VALID));
		rows.add(row("counter.end", COUNTER_END, "-2147483649", INVALID));
		rows.add(row("counter.end", COUNTER_END, "10.2", INVALID));
		rows.add(row("random_number.min", RANDOM_NUMBER_MIN, "-2147483648", VALID));
		rows.add(row("random_number.min", RANDOM_NUMBER_MIN, "-2147483649", INVALID));
		rows.add(row("random_number.min", RANDOM_NUMBER_MIN, "0.5", INVALID));
		rows.add(row("random_number.max", RANDOM_NUMBER_MAX, "2147483647", VALID));
		rows.add(row("random_number.max", RANDOM_NUMBER_MAX, "2147483648", INVALID));
		rows.add(row("random_number.max", RANDOM_NUMBER_MAX, "9.9", INVALID));
		rows.add(row("rampup_load.min_users", RAMPUP_MIN_USERS, "2147483647", VALID));
		rows.add(row("rampup_load.min_users", RAMPUP_MIN_USERS, "2147483648", INVALID));
		rows.add(row("random_string.min_length", RANDOM_STRING_MIN_LENGTH, "2147483647", VALID));
		rows.add(row("random_string.min_length", RANDOM_STRING_MIN_LENGTH, "5.5", INVALID));
		rows.add(row("random_string.min_length", RANDOM_STRING_MIN_LENGTH, "0", INVALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX_LENGTH, "2147483647", VALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX_LENGTH, "2147483648", INVALID));
		rows.add(row("random_string.max_length", RANDOM_STRING_MAX_LENGTH, "10.5", INVALID));
		rows.add(row("shared_queue.queue_size", SHARED_QUEUE_SIZE, "2147483647", VALID));
		rows.add(row("shared_queue.queue_size", SHARED_QUEUE_SIZE, "2147483648", INVALID));
		rows.add(row("shared_queue.queue_size", SHARED_QUEUE_SIZE, "0", INVALID));
		rows.add(row("extractor.match_number", EXTRACTOR_MATCH_NUMBER, "2147483647", VALID));
		rows.add(row("extractor.match_number", EXTRACTOR_MATCH_NUMBER, "2147483648", INVALID));
		rows.add(row("extractor.match_number", EXTRACTOR_MATCH_NUMBER, "-1", VALID));
		rows.add(row("extractor.match_number", EXTRACTOR_MATCH_NUMBER, "-2", INVALID));
		rows.add(row("extractor.match_number", EXTRACTOR_MATCH_NUMBER, "1.5", INVALID));
		rows.add(row("timestamp.time_to_live", TIME_TO_LIVE, "2147483647", VALID));
		rows.add(row("timestamp.time_to_live", TIME_TO_LIVE, "2147483648", INVALID));
		return rows;
	}

	private static List<String[]> negatives() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("counter.start", COUNTER_START, "-10", VALID));
		rows.add(row("counter.end", COUNTER_END, "-5", VALID));
		rows.add(row("random_number.min", RANDOM_NUMBER_MIN, "-2147483648", VALID));
		rows.add(row("random_number.max", RANDOM_NUMBER_MAX, "-1", VALID));
		return rows;
	}

	private static List<String[]> variables() {
		final List<String[]> rows = new ArrayList<>();
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
