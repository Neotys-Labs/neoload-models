package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.SoapRequestContent;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class IOSoapRequestTest extends AbstractIOElementsTest {

	private static Project getSoapRequestOnlyRequired() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private static Project getSoapRequestRequiredAndOptional() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.name("MySoapRequest")
								.description("My SOAP request")
								.url("/soap")
								.server("myServer")
								.putParameters("param1", Optional.of("value1"))
								.putParameters("param2", Optional.of("value2"))
								.putHeaders("SOAPAction", Optional.of("http://example.com/action"))
								.putHeaders("Content-Type", Optional.of("text/xml; charset=utf-8"))
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readSoapRequestOnlyRequired() throws IOException {
		final Project expectedProject = getSoapRequestOnlyRequired();
		assertNotNull(expectedProject);

		read("test-soap-request-only-required", expectedProject);
	}

	@Test
	public void writeSoapRequestOnlyRequired() throws IOException {
		final Project expectedProject = getSoapRequestOnlyRequired();
		assertNotNull(expectedProject);

		write("test-soap-request-only-required", expectedProject);
	}

	@Test
	public void readSoapRequestRequiredAndOptional() throws IOException {
		final Project expectedProject = getSoapRequestRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-soap-request-required-and-optional", expectedProject);
	}

	@Test
	public void writeSoapRequestRequiredAndOptional() throws IOException {
		final Project expectedProject = getSoapRequestRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-soap-request-required-and-optional", expectedProject);
	}

	private static Project getSoapRequestWithParameters() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.putParameters("param1", Optional.of("value1"))
								.putParameters("param2", Optional.of("value2"))
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readSoapRequestWithParameters() throws IOException {
		final Project expectedProject = getSoapRequestWithParameters();
		assertNotNull(expectedProject);

		read("test-soap-request-parameters", expectedProject);
	}

	@Test
	public void writeSoapRequestWithParameters() throws IOException {
		final Project expectedProject = getSoapRequestWithParameters();
		assertNotNull(expectedProject);

		write("test-soap-request-parameters", expectedProject);
	}

	private static Project getSoapRequestWithHeaders() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.putHeaders("SOAPAction", Optional.of("http://example.com/action"))
								.putHeaders("Content-Type", Optional.of("text/xml; charset=utf-8"))
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readSoapRequestWithHeaders() throws IOException {
		final Project expectedProject = getSoapRequestWithHeaders();
		assertNotNull(expectedProject);

		read("test-soap-request-headers", expectedProject);
	}

	@Test
	public void writeSoapRequestWithHeaders() throws IOException {
		final Project expectedProject = getSoapRequestWithHeaders();
		assertNotNull(expectedProject);

		write("test-soap-request-headers", expectedProject);
	}

	private static Project getSoapRequestWithParameterNoValue() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.putParameters("paramNoValue", Optional.empty())
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readSoapRequestWithParameterNoValue() throws IOException {
		final Project expectedProject = getSoapRequestWithParameterNoValue();
		assertNotNull(expectedProject);

		read("test-soap-request-parameter-no-value", expectedProject);
	}

	@Test
	public void writeSoapRequestWithParameterNoValue() throws IOException {
		final Project expectedProject = getSoapRequestWithParameterNoValue();
		assertNotNull(expectedProject);

		write("test-soap-request-parameter-no-value", expectedProject);
	}

	@Test
	public void parameterWithNoValueRoundTrips() throws IOException {
		final Project expectedProject = getSoapRequestWithParameterNoValue();
		final ProjectDescriptor expectedDescriptor = ProjectDescriptor.builder().project(expectedProject).build();

		final IO io = new IO();
		final String yaml = io.write(expectedDescriptor, Format.YAML);
		final ProjectDescriptor actualDescriptor = io.read(yaml);

		final SoapRequest actualSoapRequest = (SoapRequest) actualDescriptor.getProject().getUserPaths().get(0).getActions().getSteps().get(0);
		assertTrue(actualSoapRequest.getParameters().containsKey("paramNoValue"));
		assertFalse(actualSoapRequest.getParameters().get("paramNoValue").iterator().next().isPresent());
		assertEquals(expectedDescriptor.toString(), actualDescriptor.toString());
	}
}
