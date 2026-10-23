package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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

	private List<NameValue> readHeaders(final String fileName) throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile(fileName, "yaml"));
		final Request request = (Request) descriptor.getProject().getUserPaths().get(0).getActions().getSteps().get(0);
		return request.getHeaders();
	}

	@Test
	public void readHeaderAsNameValueObject() throws IOException {
		final List<NameValue> headers = readHeaders("test-request-header-name-value");

		assertEquals(ImmutableList.of(NameValue.builder().name("User-Agent").value("NeoLoad-WebUser").build()), headers);
	}

	@Test
	public void readExistingHeaderForms() throws IOException {
		final List<NameValue> headers = readHeaders("test-request-header-existing-forms");

		assertEquals(ImmutableList.of(
				NameValue.builder().name("User-Agent").value("NeoLoad-WebUser").build(),
				NameValue.builder().name("Accept").build(),
				NameValue.builder().name("name").value("foo").build()), headers);
	}

	@Test
	public void readHeaderAsNameValueObjectWithNullValue() throws IOException {
		final List<NameValue> headers = readHeaders("test-request-header-name-value-null");

		assertEquals(ImmutableList.of(NameValue.builder().name("X-Empty").build()), headers);
	}

	@Test
	public void writeNameValueObjectHeaderAsSingleEntryMap() throws IOException {
		final IO io = new IO();
		final ProjectDescriptor descriptor = io.read(getFile("test-request-header-name-value", "yaml"));

		assertTrue(io.write(descriptor, Format.YAML).contains("- User-Agent: NeoLoad-WebUser"));
	}

	@Test
	public void rejectHeaderObjectWithExtraKeys() {
		final IOException error = assertThrows(IOException.class, () -> readHeaders("test-request-header-name-value-extra-key"));

		assertTrue(error.getMessage(), error.getMessage().contains("a name, a single-entry map or a {name, value} object"));
	}

	@Test
	public void rejectEmptyHeaderObject() {
		assertThrows(IOException.class, () -> readHeaders("test-request-header-empty-object"));
	}
}
