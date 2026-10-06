package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.security.WebServicesSecurity;
import com.neotys.neoload.model.v3.project.security.WsDerivedPasswordType;
import com.neotys.neoload.model.v3.project.security.WsKeystore;
import com.neotys.neoload.model.v3.project.security.WsRequestProfile;
import com.neotys.neoload.model.v3.project.security.WsResponseProfile;
import com.neotys.neoload.model.v3.project.security.WsSecurityHeader;
import com.neotys.neoload.model.v3.project.security.WsSimplePasswordType;
import com.neotys.neoload.model.v3.project.security.WsTimestampToken;
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
						.addKeystores(WsKeystore.builder().path("./wss-keystores/server.p12").build())
						.addRequestProfiles(WsRequestProfile.builder().name("MyRequestProfile").build())
						.addResponseProfiles(WsResponseProfile.builder().name("MyResponseProfile").keystore("./wss-keystores/server.p12").build())
						.build())
				.build();
	}

	private static Project getWebServicesSecurityRequiredAndOptional() {
		return Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addKeystores(WsKeystore.builder()
								.path("./wss-keystores/server.p12")
								.password("secret")
								.build())
						.addRequestProfiles(WsRequestProfile.builder()
								.name("MyRequestProfile")
								.addHeaders(WsSecurityHeader.builder()
										.actor("http://example.com/actor")
										.mustUnderstand(true)
										.addTokens(WsUsernameToken.builder()
												.username("user")
												.password("secret")
												.passwordType(WsSimplePasswordType.PLAIN_TEXT)
												.nonce(true)
												.created(true)
												.build())
										.addTokens(WsTimestampToken.builder()
												.id("TS-1")
												.timeToLive(600)
												.milliseconds(false)
												.build())
										.build())
								.build())
						.addResponseProfiles(WsResponseProfile.builder()
								.name("MyResponseProfile")
								.keystore("./wss-keystores/server.p12")
								.build())
						.build())
				.build();
	}

	private static Project getWebServicesSecurityDerivedPasswordType() {
		return Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addRequestProfiles(WsRequestProfile.builder()
								.name("MyRequestProfile")
								.addHeaders(WsSecurityHeader.builder()
										.addTokens(WsUsernameToken.builder()
												.username("user")
												.password("secret")
												.passwordType(WsDerivedPasswordType.builder().salt("mysalt").iteration("1000").build())
												.build())
										.build())
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
		assertRead("test-web-services-security-only-required", getWebServicesSecurityOnlyRequired());
	}

	@Test
	public void writeWebServicesSecurityOnlyRequired() throws IOException {
		assertWrite("test-web-services-security-only-required", getWebServicesSecurityOnlyRequired());
	}

	@Test
	public void readWebServicesSecurityRequiredAndOptional() throws IOException {
		assertRead("test-web-services-security-required-and-optional", getWebServicesSecurityRequiredAndOptional());
	}

	@Test
	public void writeWebServicesSecurityRequiredAndOptional() throws IOException {
		assertWrite("test-web-services-security-required-and-optional", getWebServicesSecurityRequiredAndOptional());
	}

	@Test
	public void readWebServicesSecurityDerivedPasswordType() throws IOException {
		assertRead("test-web-services-security-derived-password-type", getWebServicesSecurityDerivedPasswordType());
	}

	@Test
	public void writeWebServicesSecurityDerivedPasswordType() throws IOException {
		assertWrite("test-web-services-security-derived-password-type", getWebServicesSecurityDerivedPasswordType());
	}

	@Test
	public void readSoapRequestSecurityProfiles() throws IOException {
		assertRead("test-soap-request-security-profiles", getSoapRequestSecurityProfiles());
	}

	@Test
	public void writeSoapRequestSecurityProfiles() throws IOException {
		assertWrite("test-soap-request-security-profiles", getSoapRequestSecurityProfiles());
	}

	@Test
	public void timestampDefaultsNotWritten() throws IOException {
		final String written = new IO().write(Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addRequestProfiles(WsRequestProfile.builder()
								.name("MyRequestProfile")
								.addHeaders(WsSecurityHeader.builder()
										.addTokens(WsTimestampToken.builder().build())
										.build())
								.build())
						.build())
				.build(), IO.Format.YAML);

		assertFalse(written.contains(WsTimestampToken.TIME_TO_LIVE));
		assertFalse(written.contains(WsTimestampToken.MILLISECONDS));
	}

	@Test
	public void defaultPasswordTypeNotWritten() throws IOException {
		final WsUsernameToken token = WsUsernameToken.builder().username("user").build();
		final String written = new IO().write(Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addRequestProfiles(WsRequestProfile.builder()
								.name("MyRequestProfile")
								.addHeaders(WsSecurityHeader.builder().addTokens(token).build())
								.build())
						.build())
				.build(), IO.Format.YAML);

		assertEquals(WsSimplePasswordType.DIGEST, token.getPasswordType());
		assertFalse(written.contains(WsUsernameToken.PASSWORD_TYPE));
	}

	@Test
	public void digestNonceAndCreatedNotWritten() throws IOException {
		final WsUsernameToken token = WsUsernameToken.builder().username("user").nonce(true).created(true).build();
		final String written = new IO().write(Project.builder()
				.name("MyProject")
				.webServicesSecurity(WebServicesSecurity.builder()
						.addRequestProfiles(WsRequestProfile.builder()
								.name("MyRequestProfile")
								.addHeaders(WsSecurityHeader.builder().addTokens(token).build())
								.build())
						.build())
				.build(), IO.Format.YAML);

		assertFalse(token.getNonce());
		assertFalse(token.getCreated());
		assertFalse(written.contains(WsUsernameToken.NONCE));
		assertFalse(written.contains(WsUsernameToken.CREATED));
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
