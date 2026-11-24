package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.population.Browser;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

/**
 * Browser and cache of the User Paths of a Population: the "Browser", "HTTP/2" and "Handle Cache" columns of
 * the designer's Population table.
 */
public class IOPopulationBrowserCacheTest extends AbstractIOElementsTest {

	private static Project getBrowserAndCache() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addPopulations(Population.builder()
						.name("MyPopulation1")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath11")
								.distribution(75.4)
								.browser(Browser.builder()
										.name("Chrome 124.0")
										.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
										.connections(8)
										.cookies(false)
										.http2(false)
										.addAcceptEncoding(Browser.Encoding.GZIP, Browser.Encoding.DEFLATE)
										.build())
								.cache(UserPathPolicy.Cache.NEW_USER)
								.build())
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath12")
								.distribution(24.6)
								.browser(Browser.builder().http2(false).build())
								.cache(UserPathPolicy.Cache.RETURNING_USER)
								.build())
						.build())
				.build();
	}

	private static Project getDefaults() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addPopulations(Population.builder()
						.name("MyPopulation1")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath11")
								.build())
						.build())
				.build();
	}

	@Test
	public void readBrowserAndCache() throws IOException {
		assertRead("test-populations-browser-cache", getBrowserAndCache());
	}

	@Test
	public void writeBrowserAndCache() throws IOException {
		assertWrite("test-populations-browser-cache", getBrowserAndCache());
	}

	@Test
	public void readDefaultBrowserAndCacheIsTheRecordedBrowser() throws IOException {
		assertRead("test-populations-browser-cache-defaults", getDefaults());
	}

	@Test
	public void writeDefaultBrowserAndCacheWritesNothing() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-populations-browser-cache-defaults", "yaml"));

		final String written = new IO().write(descriptor, IO.Format.YAML);

		assertFalse(written, written.contains(UserPathPolicy.BROWSER));
		assertFalse(written, written.contains(UserPathPolicy.CACHE));
	}

	@Test
	public void readPopulationWithoutBrowserAndCacheLeavesThemToTheirDefault() throws IOException {
		final UserPathPolicy userPath = new IO().read(getFile("test-populations-required-and-optional", "yaml"))
				.getProject().getPopulations().get(0).getUserPaths().get(0);

		assertFalse(userPath.getBrowser().isPresent());
		assertEquals(UserPathPolicy.Cache.AS_RECORDED, userPath.getCache());
	}

	@Test
	public void browserDefaultsAreTheRecordedBrowserSettings() throws IOException {
		final Browser browser = Browser.builder().name("Firefox").build();

		assertFalse(browser.getUserAgent().isPresent());
		assertEquals(6, browser.getConnections());
		assertTrue(browser.getCookies());
		assertTrue(browser.getHttp2());
		assertEquals(List.of(Browser.Encoding.GZIP, Browser.Encoding.DEFLATE, Browser.Encoding.BROTLI), browser.getAcceptEncoding());
	}

	@Test
	public void readZeroConnectionsFailsValidation() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-populations-browser-invalid-connections", "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);

		assertFalse(validation.isValid());
		final String message = validation.getMessage().orElse("");
		assertTrue(message, message.contains("browser.connections': must be greater than or equal to 1 connection."));
	}

	@Test
	public void readUnknownEncodingFails() {
		final String message = readFailure("test-populations-browser-invalid-encoding");

		assertTrue(message, message.contains("\"br\""));
		assertTrue(message, message.contains("brotli"));
	}

	@Test
	public void readEncodingUnknownToNeoLoadFails() {
		final String message = readFailure("test-populations-browser-unsupported-encoding");

		assertTrue(message, message.contains("\"peerdist\""));
		assertTrue(message, message.contains("identity"));
	}

	@Test
	public void readUnknownCacheFails() {
		final String message = readFailure("test-populations-invalid-cache");

		assertTrue(message, message.contains("\"first_visit\""));
		assertTrue(message, message.contains("returning_user"));
	}

	@Test
	public void readUnknownBrowserSettingFails() {
		final String message = readFailure("test-populations-browser-unknown-key");

		assertTrue(message, message.contains("agent"));
	}

	private String readFailure(final String fileName) {
		try {
			new IO().read(getFile(fileName, "yaml"));
			fail("Expected reading " + fileName + " to fail");
			return "";
		} catch (final IOException e) {
			return e.getMessage();
		}
	}
}
