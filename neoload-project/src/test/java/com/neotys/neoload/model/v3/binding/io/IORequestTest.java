package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;


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
								.addHeaders(Header.builder()
										.name("Content-Type")
										.value("text/html; charset=utf-8")
										.build())
								.addHeaders(Header.builder()
										.name("Accept-Encoding")
										.value("gzip, compress, br")
										.build())
								.body("My Body\nline 1\nline 2\n")
								.addExtractors(VariableExtractor.builder()
										.name("MyVariable1")
										.jsonPath("MyJsonPath")
										.build())
								.addAssertions(ContentAssertion.builder()
										.contains("MyUserPath_actions_request_1")
										.build())
								.slaProfile("MySlaProfile")
								.build())
						.addSteps(Request.builder()
								.url("/select?name=neoload")
								.server("neotys")
								.method(Method.POST.name())
								.addHeaders(Header.builder()
										.name("Content-Type")
										.value("text/html; charset=utf-8")
										.build())
								.addHeaders(Header.builder()
										.name("Accept-Encoding")
										.value("gzip, compress, br")
										.build())
								.body("My Body line 1 line 2")
								.addExtractors(VariableExtractor.builder()
										.name("MyVariable1")
										.jsonPath("MyJsonPath")
										.build())
								.addAssertions(ContentAssertion.builder()
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
								.addHeaders(Header.builder()
										.name("Content-Type")
										.value("application/octet-stream")
										.build())
								.bodyBinary(TEXT_PAYLOAD)
								.build())
						.addSteps(Request.builder()
								.url("/upload-raw")
								.server("neotys")
								.method(Method.PUT.name())
								.addHeaders(Header.builder()
										.name("Content-Type")
										.value("application/octet-stream")
										.build())
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
}
