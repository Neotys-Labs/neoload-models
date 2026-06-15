package com.neotys.neoload.model.v3.compatibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

/**
 * Drift guard: the embedded {@code supported-schemas.json} must match the
 * {@code schemas/vX.Y/} directory listing at the repo root. Maintained by
 * hand for now; this test fails fast at build time if the two get out of
 * sync (e.g. someone adds {@code schemas/v3.1/} but forgets to update the
 * embedded list).
 *
 * See <a href="https://tricentis.atlassian.net/wiki/spaces/NeoLoad/pages/3429171242">RFC-3</a>.
 */
public class SupportedSchemasConsistencyTest {

	@Test
	public void supported_schemas_json_matches_repo_root_schemas_dir() throws IOException {
		final Set<String> embedded = readEmbeddedSupported();
		final Set<String> onDisk = listVersionDirsAtRepoRoot();
		assertEquals(
				"supported-schemas.json must match the schemas/vX.Y/ directory listing at the repo root. "
						+ "Update one to mirror the other. Embedded=" + embedded + ", onDisk=" + onDisk,
				onDisk, embedded);
	}

	private static Set<String> readEmbeddedSupported() throws IOException {
		try (InputStream in = SchemaSupport.class.getResourceAsStream(SchemaSupport.RESOURCE_PATH)) {
			assertTrue("missing classpath resource " + SchemaSupport.RESOURCE_PATH, in != null);
			final SupportedSchemas data = new ObjectMapper().readValue(in, SupportedSchemas.class);
			return new TreeSet<>(data.getSupported());
		}
	}

	private static Set<String> listVersionDirsAtRepoRoot() throws IOException {
		final Path schemasDir = locateSchemasDir();
		final Set<String> versions = new TreeSet<>();
		try (Stream<Path> entries = Files.list(schemasDir)) {
			entries
					.filter(Files::isDirectory)
					.map(p -> p.getFileName().toString())
					.filter(name -> name.startsWith("v"))
					.map(name -> name.substring(1))
					.forEach(versions::add);
		}
		return versions;
	}

	/**
	 * Locate {@code schemas/} relative to the current working directory.
	 * Maven runs from the module directory ({@code neoload-project/}), so
	 * the folder lives at {@code ../schemas/}. IDEs may run from the repo
	 * root, in which case it sits at {@code schemas/}.
	 */
	private static Path locateSchemasDir() {
		for (final String candidate : new String[] { "../schemas", "schemas" }) {
			final Path p = Paths.get(candidate).toAbsolutePath().normalize();
			if (Files.isDirectory(p)) {
				return p;
			}
		}
		throw new IllegalStateException(
				"Could not locate the schemas/ directory at the repo root from cwd=" + Paths.get("").toAbsolutePath());
	}
}