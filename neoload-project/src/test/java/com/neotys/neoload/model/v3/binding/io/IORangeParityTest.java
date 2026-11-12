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
 * The literal range rules of the v3.1 as-code fields: schema 3.1 and the model, read with {@link IO} and checked
 * with {@link Validator}, judge the same literal the same way at every boundary.
 */
public class IORangeParityTest {
	private static final Validator VALIDATOR = new Validator();

	private static SchemaFiles SCHEMA;

	private static final String VALID = "valid";
	private static final String INVALID = "invalid";

	private static final String CONSTANT_START_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: %s\n");
	private static final String CONSTANT_STOP_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      stop_after: %s\n");
	private static final String SERVER_PORT = "name: P\nservers:\n- name: S\n  host: myserver.intranet.neotys.com\n  port: %s\n";
	private static final String LOOP_COUNT = DurationFields.actions("    - loop:\n        count: %s\n        steps:\n        - delay: 1s\n");
	private static final String COUNTER_INCREMENT = variable("counter", "start: 0\n    end: 10\n    increment: %s\n    scope: local\n");

	private static String variable(final String type, final String properties) {
		return "name: P\nvariables:\n- " + type + ":\n    name: V\n    " + properties;
	}

	@BeforeClass
	public static void loadSchema() throws URISyntaxException {
		SCHEMA = SchemaFiles.version("v3.1");
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
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "0", INVALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "1", VALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "2147483", VALID));
		rows.add(row("rendezvous_policies[].timeout", DurationFields.RENDEZVOUS_TIMEOUT.yaml("%s"), "2147484", INVALID));
		rows.add(row("loop.count", LOOP_COUNT, "0", INVALID));
		rows.add(row("loop.count", LOOP_COUNT, "1", VALID));
		rows.add(row("servers[].port", SERVER_PORT, "0", INVALID));
		rows.add(row("servers[].port", SERVER_PORT, "1", VALID));
		rows.add(row("servers[].port", SERVER_PORT, "65535", VALID));
		rows.add(row("servers[].port", SERVER_PORT, "65536", INVALID));
		rows.add(row("servers[].port", SERVER_PORT, "70000", INVALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "0", INVALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "-1", VALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "1", VALID));
		return rows;
	}

	private static List<String[]> negatives() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("servers[].port", SERVER_PORT, "-80", INVALID));
		rows.add(row("servers[].port", SERVER_PORT, "\"-80\"", INVALID));
		rows.add(row("loop.count", LOOP_COUNT, "\"-1\"", INVALID));
		rows.add(row("start_after", CONSTANT_START_AFTER, "\"-5\"", INVALID));
		rows.add(row("counter.increment", COUNTER_INCREMENT, "-1", VALID));
		return rows;
	}

	private static List<String[]> variables() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("servers[].port", SERVER_PORT, "\"${myVar}\"", VALID));
		rows.add(row("loop.count", LOOP_COUNT, "\"${myVar}\"", VALID));
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
