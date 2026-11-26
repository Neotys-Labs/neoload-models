package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Schema 3.2 and the loader ({@link IO#read(File)} then {@link Validator}) accept or reject the same files. Each
 * fixture is checked in YAML and in its JSON twin.
 */
public class IOSchema3Dot2LoaderParityTest {

	private static final List<String> EXTENSIONS = List.of("yaml", "json");
	private static final List<String> STEPS_REQUIRED_FIXTURES = List.of(
			"test-empty-steps-loop-3-2", "test-empty-steps-while-3-2", "test-empty-steps-fork-3-2",
			"test-empty-steps-web-page-3-2", "test-empty-steps-case-3-2");
	private static final List<String> ACCEPTED_FIXTURES = List.of(
			"test-empty-steps-3-2", "test-missing-steps-3-2", "test-name-values-null-value-3-2", "test-request-name-3-2");

	private static JsonSchema schema;

	@BeforeClass
	public static void loadSchema() throws URISyntaxException {
		final URL testClassesUrl = IOSchema3Dot2LoaderParityTest.class.getProtectionDomain().getCodeSource().getLocation();
		final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
		schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
				.getSchema(moduleDir.resolve("../schemas/v3.2/as-code.schema.json").normalize().toUri());
	}

	@Test
	public void emptyStepsAreAcceptedInContainersTransactionsIfTryCatchAndSwitchDefault() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-empty-steps-3-2", extension);
			assertEquals(file.getName(), "", schemaErrors(file));
			assertEquals(file.getName(), Optional.empty(), loaderError(file));
		}
	}

	@Test
	public void missingStepsAreAcceptedInContainersTransactionsIfAndSwitchDefault() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-missing-steps-3-2", extension);
			assertEquals(file.getName(), "", schemaErrors(file));
			assertEquals(file.getName(), Optional.empty(), loaderError(file));
		}
	}

	@Test
	public void emptyStepsAreRejectedInLoopWhileForkWebPageAndSwitchCase() throws IOException, URISyntaxException {
		for (final String fixture : STEPS_REQUIRED_FIXTURES) {
			for (final String extension : EXTENSIONS) {
				final File file = getFile(fixture, extension);
				final String schemaErrors = schemaErrors(file);
				assertTrue(file.getName() + ": " + schemaErrors, schemaErrors.contains(".steps:"));
				final String loaderError = loaderError(file).orElse("");
				assertTrue(file.getName() + ": " + loaderError, loaderError.contains(".steps': missing value or value is empty."));
			}
		}
	}

	@Test
	public void nameValueEntryWithANullValueIsAccepted() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-name-values-null-value-3-2", extension);
			assertEquals(file.getName(), "", schemaErrors(file));
			assertEquals(file.getName(), Optional.empty(), loaderError(file));
		}
	}

	@Test
	public void requestNameIsAccepted() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-request-name-3-2", extension);
			assertEquals(file.getName(), "", schemaErrors(file));
			assertEquals(file.getName(), Optional.empty(), loaderError(file));
		}
	}

	@Test
	public void urlWithAVariableBaseIsRejected() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-request-url-variable-base-3-2", extension);
			final String schemaErrors = schemaErrors(file);
			assertTrue(file.getName() + ": " + schemaErrors, schemaErrors.contains(".request.url:"));
			final String loaderError = loaderError(file).orElse("");
			assertTrue(file.getName() + ": " + loaderError, loaderError.contains(".url': must match"));
		}
	}

	@Test
	public void schemaVersionWithoutValueIsReadLikeAMissingOne() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-schema-version-null", extension);
			assertEquals(file.getName(), "", schemaErrors(file));
			assertEquals(file.getName(), Optional.empty(), loaderError(file));
			assertEquals(file.getName(), Project.DEFAULT_SCHEMA_VERSION, new IO().read(file).getProject().getSchemaVersion());
		}
	}

	@Test
	public void emptySchemaVersionIsRejected() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-schema-version-empty", extension);
			final String schemaErrors = schemaErrors(file);
			assertTrue(file.getName() + ": " + schemaErrors, schemaErrors.contains("$.schemaVersion:"));
			final String loaderError = loaderError(file).orElse("");
			assertTrue(file.getName() + ": " + loaderError, loaderError.contains("The 'schemaVersion' value is empty"));
		}
	}

	@Test
	public void unknownSwapFileKeyIsRejected() throws IOException, URISyntaxException {
		for (final String extension : EXTENSIONS) {
			final File file = getFile("test-shared-queue-swap-file-unknown-key-3-2", extension);
			final String schemaErrors = schemaErrors(file);
			assertTrue(file.getName() + ": " + schemaErrors, schemaErrors.contains("unknown_key"));
			final String loaderError = loaderError(file).orElse("");
			assertTrue(file.getName() + ": " + loaderError, loaderError.contains("\"unknown_key\""));
		}
	}

	@Test
	public void filesWrittenBackByTheLoaderAreAcceptedByTheSchema() throws IOException, URISyntaxException {
		for (final String fixture : ACCEPTED_FIXTURES) {
			final IO io = new IO();
			final String written = io.write(io.read(getFile(fixture, "yaml")), IO.Format.YAML);
			final Set<ValidationMessage> errors = schema.validate(new YAMLMapper().readTree(written));
			assertTrue(fixture + " written as:\n" + written + "\n" + errors, errors.isEmpty());
		}
	}

	private static File getFile(final String fixture, final String extension) throws URISyntaxException {
		final URL url = IOSchema3Dot2LoaderParityTest.class.getClassLoader().getResource(fixture + "." + extension);
		assertNotNull("Missing classpath resource " + fixture + "." + extension, url);
		return new File(url.toURI());
	}

	private static String schemaErrors(final File file) throws IOException {
		final ObjectMapper mapper = file.getName().endsWith(".yaml") ? new YAMLMapper() : new ObjectMapper();
		final Set<ValidationMessage> errors = schema.validate(mapper.readTree(file));
		return errors.stream().map(ValidationMessage::getMessage).sorted().collect(Collectors.joining("\n"));
	}

	/**
	 * The error raised by the loader on the file, or empty when the file is read and valid.
	 */
	private static Optional<String> loaderError(final File file) {
		try {
			final Validation validation = new Validator().validate(new IO().read(file), NeoLoad.class);
			return validation.isValid() ? Optional.empty() : validation.getMessage();
		} catch (final IOException e) {
			return Optional.of(e.getMessage());
		}
	}
}
