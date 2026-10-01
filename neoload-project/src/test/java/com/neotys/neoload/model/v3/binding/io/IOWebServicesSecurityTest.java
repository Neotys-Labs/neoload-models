package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.security.WebServicesSecurity;
import com.neotys.neoload.model.v3.project.security.WsKeystore;
import com.neotys.neoload.model.v3.project.security.WsRequestProfile;
import com.neotys.neoload.model.v3.project.security.WsResponseProfile;
import com.neotys.neoload.model.v3.project.security.WsSecurityHeader;
import com.neotys.neoload.model.v3.project.security.WsTimestampToken;
import com.neotys.neoload.model.v3.project.security.WsToken;
import com.neotys.neoload.model.v3.project.security.WsUsernameToken;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.SoapRequestContent;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import java.io.IOException;
import org.junit.Test;

public class IOWebServicesSecurityTest extends AbstractIOElementsTest {

	private static Project getWebServicesSecurityOnlyRequired() {
		return Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addKeystores(WsKeystore.builder().name("ServerKeystore").path("./wss-keystores/server.p12").build())
						.addRequestProfiles(WsRequestProfile.builder().name("MyRequestProfile").build())
						.addResponseProfiles(WsResponseProfile.builder().name("MyResponseProfile").build())
						.build())
				.build();
	}

	private static Project getWebServicesSecurityRequiredAndOptional() {
		return Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addKeystores(WsKeystore.builder()
								.name("ServerKeystore")
								.path("./wss-keystores/server.p12")
								.password("secret")
								.build())
						.addRequestProfiles(WsRequestProfile.builder()
								.name("MyRequestProfile")
								.addHeaders(WsSecurityHeader.builder()
										.actor("http://example.com/actor")
										.mustUnderstand(true)
										.addTokens(WsToken.builder()
												.usernameToken(WsUsernameToken.builder()
														.username("user")
														.password("secret")
														.passwordType(WsUsernameToken.PasswordType.DIGEST)
														.nonce(true)
														.created(true)
														.salt("mysalt")
														.iteration(1000)
														.build())
												.build())
										.addTokens(WsToken.builder()
												.timestamp(WsTimestampToken.builder()
														.id("TS-1")
														.timeToLive(300)
														.milliseconds(true)
														.build())
												.build())
										.build())
								.build())
						.addResponseProfiles(WsResponseProfile.builder()
								.name("MyResponseProfile")
								.keystore("ServerKeystore")
								.build())
						.build())
				.build();
	}

	private static Project getSoapRequestSecurityProfiles() {
		return Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.name("actions")
								.addSteps(SoapRequest.builder()
										.name("MySoapRequest")
										.url("/soap")
										.server("myServer")
										.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
										.requestSecurityProfile("MyRequestProfile")
										.responseSecurityProfile("MyResponseProfile")
										.build())
								.build())
						.build())
				.build();
	}

	@Test
	public void readWebServicesSecurityOnlyRequired() throws IOException {
		read("test-web-services-security-only-required", getWebServicesSecurityOnlyRequired());
	}

	@Test
	public void writeWebServicesSecurityOnlyRequired() throws IOException {
		write("test-web-services-security-only-required", getWebServicesSecurityOnlyRequired());
	}

	@Test
	public void readWebServicesSecurityRequiredAndOptional() throws IOException {
		read("test-web-services-security-required-and-optional", getWebServicesSecurityRequiredAndOptional());
	}

	@Test
	public void writeWebServicesSecurityRequiredAndOptional() throws IOException {
		write("test-web-services-security-required-and-optional", getWebServicesSecurityRequiredAndOptional());
	}

	@Test
	public void readSoapRequestSecurityProfiles() throws IOException {
		read("test-soap-request-security-profiles", getSoapRequestSecurityProfiles());
	}

	@Test
	public void writeSoapRequestSecurityProfiles() throws IOException {
		write("test-soap-request-security-profiles", getSoapRequestSecurityProfiles());
	}

	@Test
	public void soapRequestWithoutProfilesHasNoSecuritySection() throws IOException {
		final Project project = Project.builder()
				.name("MyProject")
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.name("actions")
								.addSteps(SoapRequest.builder()
										.url("http://host:80/")
										.content(SoapRequestContent.builder().path("./requests/mySOAPRequest.xml").build())
										.build())
								.build())
						.build())
				.build();

		final String written = new IO().write(project, IO.Format.YAML);

		assertFalse(project.getWebServicesSecurity().isPresent());
		assertFalse(written.contains("security_profile"));
		assertFalse(written.contains("web_services_security"));
		assertTrue(written.contains("soap_request"));
	}
}
