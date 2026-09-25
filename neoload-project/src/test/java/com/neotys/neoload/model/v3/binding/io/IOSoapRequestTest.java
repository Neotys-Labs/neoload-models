package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.SoapRequestContent;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
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
}
