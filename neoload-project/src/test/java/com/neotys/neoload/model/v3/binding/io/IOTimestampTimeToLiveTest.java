package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.security.WebServicesSecurity;
import com.neotys.neoload.model.v3.project.security.WsRequestProfile;
import com.neotys.neoload.model.v3.project.security.WsSecurityHeader;
import com.neotys.neoload.model.v3.project.security.WsTimestampToken;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@code time_to_live} of a WS-Security timestamp token: a duration of whole seconds, read and written in compact
 * form, with the same accepted values in the Java loader and in the 3.2 JSON schema.
 */
public class IOTimestampTimeToLiveTest {

	private static final List<String> VALID = List.of("0", "0s", "0m", "1", "300", "5m", "300s", "90", "1m30s", "1h30m", "120m", "2147483647");
	private static final List<String> INVALID = List.of("abc", "5x", "500ms", "1s1h", "${v}", "-5", "5.5", "1s500ms");

	private static JsonSchema schema;

	@BeforeClass
	public static void loadSchema() throws URISyntaxException {
		final URL testClassesUrl = IOTimestampTimeToLiveTest.class.getProtectionDomain().getCodeSource().getLocation();
		final Path schemasDir = Paths.get(testClassesUrl.toURI()).getParent().getParent().resolve("../schemas").normalize();
		schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemasDir.resolve("v3.2/as-code.schema.json").toUri());
	}

	private static String yaml(final String timeToLiveLine) {
		return "name: MyProject\n"
				+ "web_services_security:\n"
				+ "  request_profiles:\n"
				+ "  - name: MyRequestProfile\n"
				+ "    headers:\n"
				+ "    - tokens:\n"
				+ "      - timestamp:\n"
				+ "          id: TS-1\n"
				+ timeToLiveLine
				+ "          milliseconds: false\n";
	}

	private static String yamlWith(final String value) {
		return yaml("          time_to_live: " + value + "\n");
	}

	private static WsTimestampToken readToken(final String yaml) throws IOException {
		final Project project = new IO().read(yaml).getProject();
		return (WsTimestampToken) project.getWebServicesSecurity().get().getRequestProfiles().get(0).getHeaders().get(0).getTokens().get(0);
	}

	private static String roundTrip(final String yaml) throws IOException {
		return new IO().write(new IO().read(yaml), Format.YAML);
	}

	private static boolean loaderAccepts(final String value) {
		try {
			readToken(yamlWith(value));
			return true;
		} catch (final IOException e) {
			return false;
		}
	}

	private static boolean schemaAccepts(final String yaml) throws IOException {
		final JsonNode document = new YAMLMapper().readTree(yaml);
		return schema.validate(document).isEmpty();
	}

	@Test
	public void omittedAndDefaultValuesReadAs300SecondsAndAreNotWritten() throws IOException {
		for (final String line : List.of("", "          time_to_live: 300\n", "          time_to_live: 5m\n", "          time_to_live: 300s\n")) {
			final String yaml = yaml(line);

			assertEquals(line, Duration.ofSeconds(300), readToken(yaml).getTimeToLive());
			assertFalse(line, roundTrip(yaml).contains(WsTimestampToken.TIME_TO_LIVE));
		}
	}

	@Test
	public void zeroNeverExpiresAndIsWritten() throws IOException {
		for (final String value : List.of("0", "0s", "0m")) {
			final String yaml = yamlWith(value);

			assertEquals(value, Duration.ZERO, readToken(yaml).getTimeToLive());
			assertTrue(value, roundTrip(yaml).contains("time_to_live: 0\n"));
		}
	}

	@Test
	public void nonDefaultValuesAreWrittenInCompactForm() throws IOException {
		assertWrittenAs("90", 90, "1m30s");
		assertWrittenAs("1m30s", 90, "1m30s");
		assertWrittenAs("1h30m", 5400, "1h30m");
		assertWrittenAs("120m", 7200, "2h");
	}

	private static void assertWrittenAs(final String value, final long seconds, final String written) throws IOException {
		final String yaml = yamlWith(value);

		assertEquals(value, Duration.ofSeconds(seconds), readToken(yaml).getTimeToLive());
		assertTrue(value, roundTrip(yaml).contains("time_to_live: " + written));
	}

	@Test
	public void invalidValuesAreRejectedAtLoadNamingTheFieldAndTheValue() {
		for (final String value : List.of("abc", "5x", "500ms", "1s1h", "${v}", "-5", "2147483648")) {
			final String message = assertThrows(value, JsonMappingException.class, () -> readToken(yamlWith(value))).getMessage();

			assertTrue(message, message.contains("time_to_live"));
			assertTrue(message, message.contains("'" + value + "'"));
		}
	}

	@Test
	public void builderRejectsAMillisecondsPartAndANegativeValue() {
		assertViolations(validate(Duration.ofMillis(1500)), invalidValue("project.web_services_security.request_profiles[0].headers[0].tokens[0].time_to_live", "'1s500ms' must be a whole number of seconds."));
		assertViolations(validate(Duration.ofSeconds(-5)), invalidValue("project.web_services_security.request_profiles[0].headers[0].tokens[0].time_to_live", "'-5s' must be greater than or equal to 0."));
	}

	private static Validation validate(final Duration timeToLive) {
		final Project project = Project.builder().name("MyProject").webServicesSecurity(WebServicesSecurity.builder()
				.addRequestProfiles(WsRequestProfile.builder().name("MyRequestProfile")
						.addHeaders(WsSecurityHeader.builder().addTokens(WsTimestampToken.builder().timeToLive(timeToLive).build()).build())
						.build())
				.build()).build();
		return new Validator().validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	@Test
	public void schemaAndLoaderAcceptAndRejectTheSameTimeToLiveValues() throws IOException {
		for (final String value : VALID) {
			assertTrue("loader " + value, loaderAccepts(value));
			assertTrue("schema " + value, schemaAccepts(yamlWith(value)));
		}
		for (final String value : INVALID) {
			assertFalse("loader " + value, loaderAccepts(value));
			assertFalse("schema " + value, schemaAccepts(yamlWith(value)));
		}
	}

	@Test
	public void schemaRejectsZeroOnRampupsAndRendezvousTimeout() throws IOException {
		for (final DurationFields field : List.of(DurationFields.CONSTANT_RAMPUP, DurationFields.RAMPUP_INCREMENT_RAMPUP, DurationFields.PEAKS_STEP_RAMPUP, DurationFields.RENDEZVOUS_TIMEOUT)) {
			for (final String value : List.of("0", "0s", "0m", "0h0m")) {
				assertFalse(field.key + " " + value, schemaAccepts(field.yaml(value)));
			}
			for (final String value : List.of("1", "30s", "1h30m")) {
				assertTrue(field.key + " " + value, schemaAccepts(field.yaml(value)));
			}
		}
	}

	@Test
	public void defaultsAreNeverWritten() throws IOException {
		final String yaml = "name: MyProject\n"
				+ "web_services_security:\n"
				+ "  request_profiles:\n"
				+ "  - name: MyRequestProfile\n"
				+ "    headers:\n"
				+ "    - must_understand: false\n"
				+ "      tokens:\n"
				+ "      - timestamp:\n"
				+ "          time_to_live: 300\n"
				+ "          milliseconds: true\n";

		final String written = roundTrip(yaml);

		assertFalse(written, written.contains(WsTimestampToken.TIME_TO_LIVE));
		assertFalse(written, written.contains(WsTimestampToken.MILLISECONDS));
		assertFalse(written, written.contains("must_understand"));
	}
}
