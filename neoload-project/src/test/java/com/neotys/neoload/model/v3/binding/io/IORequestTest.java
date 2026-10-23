package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;

public class IORequestTest extends AbstractIOElementsTest {

	private static final byte[] TEXT_PAYLOAD = "Hello binary world!".getBytes(StandardCharsets.UTF_8);
	private static final byte[] RAW_PAYLOAD = { 0x00, 0x01, 0x02, (byte) 0xFF, (byte) 0xFE };

	private static Project getRequestOnlyRequired() {

		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("http://www.neotys.com/select?name:neoload")
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private static Project getRequestRequiredAndOptional() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("/select?name=neoload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(NameValue.builder().name("Content-Type").value("text/html; charset=utf-8").build())
								.addHeaders(NameValue.builder().name("Accept-Encoding").value("gzip, compress, br").build())
								.body("My Body\nline 1\nline 2\n")
								.addExtractors(VariableExtractor.builder()
										.name("MyVariable1")
										.jsonPath("MyJsonPath")
										.build())
								.addContentAssertions(ContentAssertion.builder()
										.contains("MyUserPath_actions_request_1")
										.build())
								.slaProfile("MySlaProfile")
								.internalRecordedId("r0001")
								.build())
						.addSteps(Request.builder()
								.url("/select?name=neoload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(NameValue.builder().name("Content-Type").value("text/html; charset=utf-8").build())
								.addHeaders(NameValue.builder().name("Accept-Encoding").value("gzip, compress, br").build())
								.body("My Body line 1 line 2")
								.addExtractors(VariableExtractor.builder()
										.name("MyVariable1")
										.jsonPath("MyJsonPath")
										.build())
								.addContentAssertions(ContentAssertion.builder()
										.contains("MyUserPath_actions_request_2")
										.build())
								.slaProfile("MySlaProfile")
								.build())
						.build())
				.build();


		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readUserPathsOnlyRequired() throws IOException {
		final Project expectedProject = getRequestOnlyRequired();
		assertNotNull(expectedProject);

		read("test-request-only-required", expectedProject);
	}

	@Test
	public void writeRequestOnlyRequired() throws IOException {
		final Project expectedProject = getRequestOnlyRequired();
		assertNotNull(expectedProject);

		write("test-request-only-required", expectedProject);
	}

	private static Project getRequestBinaryBody() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("/upload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(NameValue.builder().name("Content-Type").value("application/octet-stream").build())
								.bodyBinary(TEXT_PAYLOAD)
								.build())
						.addSteps(Request.builder()
								.url("/upload-raw")
								.server("neotys")
								.method(Method.PUT.name())
								.addHeaders(NameValue.builder().name("Content-Type").value("application/octet-stream").build())
								.bodyBinary(RAW_PAYLOAD)
								.slaProfile("MySlaProfile")
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readRequestBinaryBody() throws IOException {
		final Project expectedProject = getRequestBinaryBody();
		assertNotNull(expectedProject);

		read("test-request-binary-body", expectedProject);
	}

	@Test
	public void writeRequestBinaryBody() throws IOException {
		final Project expectedProject = getRequestBinaryBody();
		assertNotNull(expectedProject);

		write("test-request-binary-body", expectedProject);
	}

	private static Project getRequestBinarySourceFile() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(Request.builder()
								.url("/upload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(NameValue.builder().name("Content-Type").value("application/octet-stream").build())
								.binarySourceFile("payloads/hello.bin")
								.build())
						.addSteps(Request.builder()
								.url("/upload-raw")
								.server("neotys")
								.method(Method.PUT.name())
								.addHeaders(NameValue.builder().name("Content-Type").value("application/octet-stream").build())
								.binarySourceFile("payload.bin")
								.slaProfile("MySlaProfile")
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readRequestBinarySourceFile() throws IOException {
		final Project expectedProject = getRequestBinarySourceFile();
		assertNotNull(expectedProject);

		read("test-request-binary-source-file", expectedProject);
	}

	@Test
	public void writeRequestBinarySourceFile() throws IOException {
		final Project expectedProject = getRequestBinarySourceFile();
		assertNotNull(expectedProject);

		write("test-request-binary-source-file", expectedProject);
	}

	@Test
	public void readUserPathsRequiredAndOptional() throws IOException {
		final Project expectedProject = getRequestRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-request-required-and-optional", expectedProject);
	}

	@Test
	public void writeRequestRequiredAndOptional() throws IOException {
		final Project expectedProject = getRequestRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-request-required-and-optional", expectedProject);
	}

	private static final String TEST_URL = "http://www.neotys.com/select";
	private static final String SOAP_URL = "http://host:80/";
	private static final String SOAP_PATH = "./requests/mySOAPRequest.xml";

	private static SoapRequest.Builder soapRequest() {
		return SoapRequest.builder().url(SOAP_URL).content(SoapRequestContent.builder().path(SOAP_PATH).build());
	}

	private static Project projectOf(final Step... steps) {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder().name("actions").addSteps(steps).build())
						.build())
				.build();
	}

	private static Project getFollowRedirects() {
		return projectOf(Request.builder().url(TEST_URL).followRedirects(true).build(),
				soapRequest().followRedirects(true).build());
	}

	private static Project getCharset() {
		return projectOf(
				Request.builder().url(TEST_URL).method(Method.POST.name()).body("My Body").charset("UTF-8").build(),
				Request.builder().url(TEST_URL).method(Method.PUT.name()).body("My Body").charset("UTF-8").build(),
				soapRequest().charset("UTF-8").build());
	}

	private static Project getResponseStorage() {
		return projectOf(
				Request.builder().url(TEST_URL)
						.responseStorage(ResponseStorage.builder()
								.path("responses/login_{ID}.html")
								.variable("loginResponseFile")
								.deleteWhenTestFinished(true)
								.build())
						.build(),
				soapRequest()
						.responseStorage(ResponseStorage.builder().path("responses/order_${orderId}_{ID}.xml").build())
						.build());
	}


	@Test
	public void readFollowRedirects() throws IOException {
		final Project expectedProject = getFollowRedirects();
		assertNotNull(expectedProject);

		read("test-request-follow-redirects", expectedProject);
	}

	@Test
	public void writeFollowRedirects() throws IOException {
		final Project expectedProject = getFollowRedirects();
		assertNotNull(expectedProject);

		write("test-request-follow-redirects", expectedProject);
	}

	@Test
	public void readDeprecatedFollowRedirectsAlias() throws IOException {
		final Project expectedProject = getFollowRedirects();
		assertNotNull(expectedProject);

		read("test-request-follow-redirects-deprecated", expectedProject);
	}

	@Test
	public void readBothFollowRedirectsKeysFails() throws IOException {
		for (final String extension : new String[] { "yaml", "json" }) {
			final String content = getContent(getFile("test-request-redirect-keys-conflict", extension), StandardCharsets.UTF_8);
			try {
				new IO().read(content);
				fail("Reading the " + extension + " fixture must fail");
			} catch (final JsonMappingException e) {
				assertTrue(e.getMessage(), e.getMessage().contains("followRedirects") && e.getMessage().contains("follow_redirects"));
			}
		}
	}

	@Test
	public void readCharset() throws IOException {
		final Project expectedProject = getCharset();
		assertNotNull(expectedProject);

		read("test-request-charset", expectedProject);
	}

	@Test
	public void writeCharset() throws IOException {
		final Project expectedProject = getCharset();
		assertNotNull(expectedProject);

		write("test-request-charset", expectedProject);
	}

	@Test
	public void readCharsetIgnoredWithoutBody() throws IOException {
		final Project expectedProject = projectOf(Request.builder().url(TEST_URL).build());
		assertNotNull(expectedProject);

		read("test-request-charset-get-ignored", expectedProject);
	}

	@Test
	public void writeCharsetIgnoredWithoutBody() throws IOException {
		final Request request = Request.builder().url("http://www.neotys.com/select?name:neoload").charset("UTF-8").build();
		assertFalse(request.getCharset().isPresent());
		write("test-request-only-required", projectOf(request));
	}

	@Test
	public void readResponseStorage() throws IOException {
		final Project expectedProject = getResponseStorage();
		assertNotNull(expectedProject);

		read("test-request-response-storage", expectedProject);
	}

	@Test
	public void writeResponseStorage() throws IOException {
		final Project expectedProject = getResponseStorage();
		assertNotNull(expectedProject);

		write("test-request-response-storage", expectedProject);
	}

	@Test
	public void defaultAdvancedSettingsAreNotWritten() throws IOException {
		final Project project = projectOf(Request.builder().url(TEST_URL).build(), soapRequest().build());
		final IO io = new IO();
		for (final Format format : Format.values()) {
			final String written = io.write(project, format);
			for (final String key : new String[] { "follow_redirects", "followRedirects", "charset", "response_storage" }) {
				assertFalse(format + " must not contain " + key, written.contains(key));
			}
		}
		final Project readBack = io.read(io.write(project, Format.YAML)).getProject();
		final Request request = (Request) (readBack.getUserPaths().get(0).getActions()).getSteps().get(0);
		final SoapRequest soap = (SoapRequest) (readBack.getUserPaths().get(0).getActions()).getSteps().get(1);
		assertEquals(false, request.getFollowRedirects());
		assertFalse(request.getCharset().isPresent());
		assertFalse(request.getResponseStorage().isPresent());
		assertEquals(false, soap.getFollowRedirects());
		assertFalse(soap.getCharset().isPresent());
		assertFalse(soap.getResponseStorage().isPresent());
	}

	private static String requestWithHeader(final String header) {
		return "name: MyProject\n"
				+ "user_paths:\n"
				+ "- name: MyUserPath\n"
				+ "  actions:\n"
				+ "    steps:\n"
				+ "    - request:\n"
				+ "        url: /select\n"
				+ "        headers:\n"
				+ "        " + header + "\n";
	}

	private static List<NameValue> readHeaders(final String header) throws IOException {
		final ProjectDescriptor descriptor = new IO().read(requestWithHeader(header));
		final Request request = (Request) descriptor.getProject().getUserPaths().get(0).getActions().getSteps().get(0);
		return request.getHeaders();
	}

	@Test
	public void readHeaderAsNameValueObject() throws IOException {
		final List<NameValue> headers = readHeaders("- name: User-Agent\n          value: NeoLoad-WebUser");

		assertEquals(ImmutableList.of(NameValue.builder().name("User-Agent").value("NeoLoad-WebUser").build()), headers);
	}

	@Test
	public void readExistingHeaderForms() throws IOException {
		assertEquals(ImmutableList.of(NameValue.builder().name("User-Agent").value("NeoLoad-WebUser").build()),
				readHeaders("- User-Agent: NeoLoad-WebUser"));
		assertEquals(ImmutableList.of(NameValue.builder().name("User-Agent").build()),
				readHeaders("- User-Agent"));
		assertEquals(ImmutableList.of(NameValue.builder().name("name").value("foo").build()),
				readHeaders("- name: foo"));
	}

	@Test
	public void readHeaderAsNameValueObjectWithNullValue() throws IOException {
		final List<NameValue> headers = readHeaders("- name: X-Empty\n          value:");

		assertEquals(readHeaders("- X-Empty:"), headers);
		assertEquals("X-Empty", headers.get(0).getName());
	}

	@Test
	public void writeNameValueObjectHeaderAsSingleEntryMap() throws IOException {
		final IO io = new IO();
		final ProjectDescriptor descriptor = io.read(requestWithHeader("- name: User-Agent\n          value: NeoLoad-WebUser"));

		assertTrue(io.write(descriptor, Format.YAML).contains("- User-Agent: NeoLoad-WebUser"));
	}

	@Test
	public void rejectHeaderObjectWithExtraKeys() {
		final IOException error = assertThrows(IOException.class, () -> readHeaders("- {name: A, value: B, other: C}"));

		assertTrue(error.getMessage(), error.getMessage().contains("a name, a single-entry map or a {name, value} object"));
	}

	@Test
	public void rejectEmptyHeaderObject() {
		assertThrows(IOException.class, () -> readHeaders("- {}"));
	}
}
