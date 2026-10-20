package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.NameValue;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.SoapRequestContent;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;
import java.io.IOException;
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
								.addParameters(NameValue.builder().name("param1").value("value1").build())
								.addParameters(NameValue.builder().name("param2").value("value2").build())
								.addHeaders(NameValue.builder().name("SOAPAction").value("http://example.com/action").build())
								.addHeaders(NameValue.builder().name("Content-Type").value("text/xml; charset=utf-8").build())
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion_1").contains("Design").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion_2").not(true).contains("Maintenance").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion_3").contains("^Analyze.*$").regexp(true).build())
								.addContentAssertions(ContentAssertion.builder().name("assertion_4").xPath("xpath").contains("DevOps").build())
								.addContentAssertions(ContentAssertion.builder().name("assertion_5").jsonPath("jsonpath").contains("Automation").build())
								.durationAssertion(DurationAssertion.builder().lessThan(2048L).build())
								.sizeAssertion(SizeAssertion.builder().greaterThan(1024L).lessThan(2048L).build())
								.slaProfile("MySla")
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	private static Project getSoapRequestLegacyAssertions() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
								.addContentAssertions(ContentAssertion.builder().name("soap_legacy").contains("legacy").build())
								.build())
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addUserPaths(userPath)
				.build();
	}

	@Test
	public void readSoapRequestLegacyAssertions() throws IOException {
		final Project expectedProject = getSoapRequestLegacyAssertions();
		assertNotNull(expectedProject);

		read("test-soap-request-legacy-assertions", expectedProject);
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
								.addParameters(NameValue.builder().name("param1").value("value1").build())
								.addParameters(NameValue.builder().name("param2").value("value2").build())
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
								.addHeaders(NameValue.builder().name("SOAPAction").value("http://example.com/action").build())
								.addHeaders(NameValue.builder().name("Content-Type").value("text/xml; charset=utf-8").build())
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
								.addParameters(NameValue.builder().name("paramNoValue").build())
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
		assertEquals(ImmutableList.of(NameValue.builder().name("paramNoValue").build()), actualSoapRequest.getParameters());
		assertEquals(expectedDescriptor.toString(), actualDescriptor.toString());
	}

	private static Project getSoapRequestWithInterleavedDuplicates() {
		final UserPath userPath = UserPath.builder()
				.name("MyUserPath")
				.actions(Container.builder()
						.name("actions")
						.addSteps(SoapRequest.builder()
								.url("http://host:80/")
								.addParameters(NameValue.builder().name("param1").value("value1").build())
								.addParameters(NameValue.builder().name("param2").build())
								.addParameters(NameValue.builder().name("param1").value("value1").build())
								.addParameters(NameValue.builder().name("param2").build())
								.addHeaders(NameValue.builder().name("Cookie").value("a=1").build())
								.addHeaders(NameValue.builder().name("Accept").value("text/xml").build())
								.addHeaders(NameValue.builder().name("Cookie").value("a=1").build())
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
	public void interleavedDuplicateParametersAndHeadersKeepYamlOrder() throws IOException {
		final Project expectedProject = getSoapRequestWithInterleavedDuplicates();
		final ProjectDescriptor expectedDescriptor = ProjectDescriptor.builder().project(expectedProject).build();

		final IO io = new IO();
		final String yaml = io.write(expectedDescriptor, Format.YAML);
		final ProjectDescriptor actualDescriptor = io.read(yaml);

		final SoapRequest expectedSoapRequest = (SoapRequest) expectedProject.getUserPaths().get(0).getActions().getSteps().get(0);
		final SoapRequest actualSoapRequest = (SoapRequest) actualDescriptor.getProject().getUserPaths().get(0).getActions().getSteps().get(0);
		assertEquals(expectedSoapRequest.getParameters(), actualSoapRequest.getParameters());
		assertEquals(expectedSoapRequest.getHeaders(), actualSoapRequest.getHeaders());
		assertEquals(expectedDescriptor.toString(), actualDescriptor.toString());
	}
}
