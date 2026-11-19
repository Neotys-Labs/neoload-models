package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.server.ServerAuthentication;
import com.neotys.neoload.model.v3.project.server.UrlRewriting;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * Server settings of schema 3.2: regular ({@code tcp}) servers, the {@code authentications} list with the preferred
 * protocol, URL rewriting, the port range and the strict reading of a server.
 */
public class IOServerSettingsTest extends AbstractIOElementsTest {

	private static Project getAllSettings() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addServers(Server.builder()
						.name("backend")
						.host("backend.example.com")
						.scheme(Server.Scheme.HTTPS)
						.port("8443")
						.addAuthentications(ServerAuthentication.builder()
								.login("${user}")
								.password("${password}")
								.realm("CORP")
								.preferredProtocol(ServerAuthentication.PreferredProtocol.KERBEROS)
								.build())
						.addAuthentications(ServerAuthentication.builder()
								.login("admin")
								.password("secret")
								.preferredProtocol(ServerAuthentication.PreferredProtocol.NTLM)
								.build())
						.urlRewriting(UrlRewriting.builder()
								.sessionArgument("jsessionid")
								.inPath(true)
								.pathSeparator("/")
								.equalsSign(false)
								.build())
						.build())
				.addServers(Server.builder()
						.name("shop")
						.host("shop.example.com")
						.port("80")
						.urlRewriting(UrlRewriting.builder().sessionArgument("phpsessid").build())
						.build())
				.addServers(Server.builder()
						.name("media")
						.type(Server.Type.TCP)
						.host("media.example.com")
						.port("65535")
						.build())
				.build();
	}

	private static Project getUrlRewritingDefaults() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addServers(Server.builder()
						.name("shop")
						.host("shop.example.com")
						.port("80")
						.urlRewriting(UrlRewriting.builder().sessionArgument("phpsessid").build())
						.build())
				.addServers(Server.builder()
						.name("backend")
						.host("backend.example.com")
						.port("80")
						.urlRewriting(UrlRewriting.builder().sessionArgument("jsessionid").inPath(true).build())
						.build())
				.build();
	}

	@Test
	public void readAllSettings() throws IOException {
		assertRead("test-servers-all-settings", getAllSettings());
	}

	@Test
	public void writeAllSettings() throws IOException {
		assertWrite("test-servers-all-settings", getAllSettings());
	}

	@Test
	public void allSettingsAreValid() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-servers-all-settings", "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);

		assertTrue(validation.getMessage().orElse(""), validation.isValid());
	}

	private static Project getEmptyCredentials() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addServers(Server.builder()
						.name("backend")
						.host("backend.example.com")
						.port("80")
						.addAuthentications(ServerAuthentication.builder().login("").password("").build())
						.addAuthentications(ServerAuthentication.builder().login("guest").password("").realm("CORP").build())
						.build())
				.build();
	}

	/** The GUI Authorization table accepts empty credentials: an item of authentications too, written explicitly. */
	@Test
	public void readEmptyCredentials() throws IOException {
		assertRead("test-servers-empty-credentials", getEmptyCredentials());
	}

	@Test
	public void writeEmptyCredentials() throws IOException {
		assertWrite("test-servers-empty-credentials", getEmptyCredentials());
	}

	@Test
	public void readBasicAuthenticationWithEmptyPasswordFailsValidation() throws IOException {
		final String message = validationFailure("test-servers-basic-authentication-empty-password");

		assertTrue(message, message.contains("'project.servers[0].authentication.password': missing value or value is empty."));
	}

	@Test
	public void readUrlRewritingDefaultsAreDropped() throws IOException {
		assertRead("test-servers-url-rewriting-defaults", getUrlRewritingDefaults());
	}

	@Test
	public void writeUrlRewritingDefaultsWritesNothing() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-servers-url-rewriting-defaults", "yaml"));

		final String written = new IO().write(descriptor, IO.Format.YAML);

		assertFalse(written, written.contains("in_path: false"));
		assertFalse(written, written.contains(UrlRewriting.PATH_SEPARATOR));
		assertFalse(written, written.contains(UrlRewriting.EQUALS_SIGN));
	}

	@Test
	public void readServerWithoutTypeIsHttp() throws IOException {
		final Server server = new IO().read(getFile("test-servers-only-required", "yaml")).getProject().getServers().get(0);

		assertEquals(Server.Type.HTTP, server.getType());
		assertTrue(server.getAuthentications().isEmpty());
		assertFalse(server.getUrlRewriting().isPresent());
	}

	@Test
	public void readUnknownKeyFails() {
		final String message = readFailure("test-servers-unknown-key");

		assertTrue(message, message.contains("Unrecognized field \"prot\""));
	}

	@Test
	public void readDescriptionFails() {
		final String message = readFailure("test-servers-description");

		assertTrue(message, message.contains("Unrecognized field \"description\""));
	}

	@Test
	public void readHostInsideAnotherKeyIsNotTheServerHost() {
		final String message = readFailure("test-servers-nested-host");

		assertTrue(message, message.contains("'host' field is mandatory for Server"));
	}

	@Test
	public void readTcpServerWithSchemeFails() {
		final String message = readFailure("test-servers-tcp-with-scheme");

		assertTrue(message, message.contains("The tcp server 'media' only accepts name, type, host and port, not 'scheme'."));
	}

	@Test
	public void readTcpServerWithoutPortFails() {
		final String message = readFailure("test-servers-tcp-without-port");

		assertTrue(message, message.contains("'port' field is mandatory for the tcp server 'media'."));
	}

	@Test
	public void readPortAbove65535FailsValidation() throws IOException {
		final String message = validationFailure("test-servers-port-too-high");

		assertTrue(message, message.contains("'project.servers[0].port': must be a port between 1 and 65535, or a variable."));
	}

	@Test
	public void readPortZeroFailsValidation() throws IOException {
		final String message = validationFailure("test-servers-port-zero");

		assertTrue(message, message.contains("'project.servers[0].port': must be a port between 1 and 65535, or a variable."));
	}

	@Test
	public void readTwoAuthenticationFormsFails() {
		final String message = readFailure("test-servers-two-authentication-forms");

		assertTrue(message, message.contains("Only one authentication can be defined in a Server"));
	}

	@Test
	public void readTwoAuthenticationsWithTheSameRealmFails() {
		final String message = readFailure("test-servers-duplicate-realm");

		assertTrue(message, message.contains("Two authentications of the server 'backend' use the realm 'CORP'"));
	}

	@Test
	public void readUnknownPreferredProtocolFails() {
		final String message = readFailure("test-servers-unknown-preferred-protocol");

		assertTrue(message, message.contains("digest"));
	}

	@Test
	public void readPathSeparatorWithoutInPathFails() {
		final String message = readFailure("test-servers-url-rewriting-separator-without-in-path");

		assertTrue(message, message.contains("'path_separator' of the URL rewriting of the server 'backend' needs 'in_path: true'."));
	}

	@Test
	public void readUrlRewritingWithoutSessionArgumentFailsValidation() throws IOException {
		final String message = validationFailure("test-servers-url-rewriting-without-session-argument");

		assertTrue(message, message.contains("'project.servers[0].url_rewriting.session_argument'"));
	}

	private String validationFailure(final String fileName) throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile(fileName, "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);

		assertFalse(validation.isValid());
		return validation.getMessage().orElse("");
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
