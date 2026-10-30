package com.neotys.neoload.model.v3.compatibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.project.variable.IncrementTimeUnit;
import com.neotys.neoload.model.v3.validation.constraintvalidators.ElementNameValidator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Drift guard: each pattern of {@code schemas/v3.1/as-code.schema.json} shared with a Java regex must equal the
 * string composed from the Java constants. To cover a new shared pattern, add one entry to {@link #patterns()}.
 */
@RunWith(Parameterized.class)
public class SchemaJavaPatternConsistencyTest {
	private static final String SCHEMA_RESOURCE = "schemas/v3.1/as-code.schema.json";

	private static JsonNode schema;

	private final String pointer;
	private final String expectedPattern;

	public SchemaJavaPatternConsistencyTest(final String pointer, final String expectedPattern) {
		this.pointer = pointer;
		this.expectedPattern = expectedPattern;
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> patterns() {
		return Arrays.asList(new Object[][] {
				{ "/definitions/common/duration_value/anyOf/0/pattern",
						"^\\s*(" + DurationParser.PLAIN_NUMBER_GRAMMAR + "|" + DurationParser.MILLISECOND_GRAMMAR + "|"
								+ Duration.VARIABLE_PATTERN + ")\\s*$" },
				{ "/definitions/common/duration_seconds/anyOf/0/pattern",
						"^\\s*(" + DurationParser.PLAIN_NUMBER_GRAMMAR + "|" + DurationParser.SECOND_GRAMMAR + ")\\s*$" },
				{ "/definitions/variables/current_date/allOf/1/properties/offset/pattern",
						IncrementTimeUnit.PATTERN.pattern() },
				{ "/definitions/variables/date/allOf/1/properties/change_step/pattern",
						IncrementTimeUnit.PATTERN.pattern() },
				{ "/definitions/common/name/pattern", "^[^" + ElementNameValidator.FORBIDDEN_CHARS + "]*$" },
				{ "/definitions/user_paths/user_path_think_time/properties/factor/anyOf/0/pattern",
						"^" + UserPathThinkTime.FACTOR_PATTERN + "$" },
				{ "/definitions/user_paths/user_path_think_time/properties/random/anyOf/0/pattern",
						"^" + UserPathThinkTime.RANDOM_PATTERN + "$" } });
	}

	@BeforeClass
	public static void loadSchema() throws IOException {
		schema = new ObjectMapper().readTree(Files.readAllBytes(locateSchema()));
	}

	@Test
	public void schema_pattern_equals_java_pattern() {
		final JsonNode node = schema.at(pointer);
		assertFalse("no pattern at JSON pointer " + pointer + " in " + SCHEMA_RESOURCE, node.isMissingNode());
		assertEquals("schema pattern at " + pointer + " differs from the Java pattern.\n  schema:   " + node.asText()
				+ "\n  expected: " + expectedPattern + "\n", expectedPattern, node.asText());
	}

	/**
	 * Maven runs from the module directory ({@code neoload-project/}), so the schema lives under {@code ../schemas/};
	 * IDEs may run from the repo root.
	 */
	private static Path locateSchema() {
		for (final String candidate : new String[] { "../" + SCHEMA_RESOURCE, SCHEMA_RESOURCE }) {
			final Path path = Paths.get(candidate).toAbsolutePath().normalize();
			if (Files.isRegularFile(path)) {
				return path;
			}
		}
		throw new IllegalStateException(
				"Could not locate " + SCHEMA_RESOURCE + " at the repo root from cwd=" + Paths.get("").toAbsolutePath());
	}
}
