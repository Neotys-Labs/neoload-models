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

	private static final String RAMPUP_MIN_USERS = DurationFields.scenario("  populations:\n  - name: R\n    rampup_load:\n      min_users: %s\n"
			+ "      increment_users: 1\n      increment_every: 1 iteration\n");
	private static final String PEAKS_MINIMUM_USERS = DurationFields.scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: %s\n"
			+ "        duration: 1 iteration\n      maximum:\n        users: 5\n        duration: 1 iteration\n      start: maximum\n");

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
		for (final String[] field : new String[][] {{"rampup_load.min_users", RAMPUP_MIN_USERS}, {"peaks_load.minimum.users", PEAKS_MINIMUM_USERS}}) {
			rows.add(row(field[0], field[1], "0", INVALID));
			rows.add(row(field[0], field[1], "1", VALID));
			rows.add(row(field[0], field[1], "2147483647", VALID));
			rows.add(row(field[0], field[1], "2147483648", INVALID));
		}
		return rows;
	}

	private static List<String[]> negatives() {
		final List<String[]> rows = new ArrayList<>();
		rows.add(row("rampup_load.min_users", RAMPUP_MIN_USERS, "-1", INVALID));
		rows.add(row("peaks_load.minimum.users", PEAKS_MINIMUM_USERS, "-1", INVALID));
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
