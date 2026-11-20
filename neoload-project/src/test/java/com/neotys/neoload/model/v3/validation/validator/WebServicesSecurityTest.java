package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.security.WebServicesSecurity;
import com.neotys.neoload.model.v3.project.security.WsDerivedPasswordType;
import com.neotys.neoload.model.v3.project.security.WsEncryptToken;
import com.neotys.neoload.model.v3.project.security.WsKeystore;
import com.neotys.neoload.model.v3.project.security.WsPart;
import com.neotys.neoload.model.v3.project.security.WsRequestProfile;
import com.neotys.neoload.model.v3.project.security.WsResponseProfile;
import com.neotys.neoload.model.v3.project.security.WsSignatureToken;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class WebServicesSecurityTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();

	@Test
	public void validateUniqueProfileNames() {
		final WebServicesSecurity security = WebServicesSecurity.builder()
				.addRequestProfiles(WsRequestProfile.builder().name("Profile1").build())
				.addRequestProfiles(WsRequestProfile.builder().name("Profile2").build())
				.addResponseProfiles(WsResponseProfile.builder().name("Profile1").keystore("./wss-keystores/server.p12").build())
				.addKeystores(WsKeystore.builder().path("./wss-keystores/client.p12").build())
				.addKeystores(WsKeystore.builder().path("./wss-keystores/server.p12").build())
				.build();

		assertTrue(new Validator().validate(security, NeoLoad.class).isValid());
	}

	@Test
	public void validateResponseProfileKeystoreRequired() {
		assertFalse(new Validator().validate(WsResponseProfile.builder().name("Profile").build(), NeoLoad.class).isValid());
	}

	@Test
	public void validateDerivedPasswordTypeIteration() {
		assertTrue(new Validator().validate(WsDerivedPasswordType.builder().salt("mysalt").iteration("1").build(), NeoLoad.class).isValid());
		assertTrue(new Validator().validate(WsDerivedPasswordType.builder().salt("mysalt").iteration("${iterations}").build(), NeoLoad.class).isValid());
		assertEquals("Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR
				+ "Violation 1 - Incorrect value for 'iteration': must match \"^(([1-9]\\d*)|(\\$\\{.+\\}))$\"" + LINE_SEPARATOR,
				new Validator().validate(WsDerivedPasswordType.builder().salt("mysalt").iteration("0").build(), NeoLoad.class).getMessage().get());
	}

	@Test
	public void validateDuplicateKeystorePaths() {
		final WebServicesSecurity security = WebServicesSecurity.builder()
				.addKeystores(WsKeystore.builder().path("./wss-keystores/server.p12").build())
				.addKeystores(WsKeystore.builder().path("./wss-keystores/server.p12").build())
				.build();

		assertEquals("Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR
				+ "Violation 1 - Incorrect value for 'keystores': must contain only unique paths." + LINE_SEPARATOR,
				new Validator().validate(security, NeoLoad.class).getMessage().get());
	}

	@Test
	public void validateTokenKeystoreAndAliasRequired() {
		assertFalse(new Validator().validate(WsEncryptToken.builder().keystore("./wss-keystores/client.p12").build(), NeoLoad.class).isValid());
		assertFalse(new Validator().validate(WsSignatureToken.builder().alias("client").build(), NeoLoad.class).isValid());
	}

	@Test
	public void validateParts() {
		assertTrue(new Validator().validate(WsPart.builder().id("Body-1").build(), NeoLoad.class).isValid());
		assertTrue(new Validator().validate(WsPart.builder().name("Body").build(), NeoLoad.class).isValid());
		assertTrue(new Validator().validate(WsPart.builder().name("Body").namespace("http://example.com").build(), NeoLoad.class).isValid());
		assertFalse(new Validator().validate(WsPart.builder().namespace("http://example.com").build(), NeoLoad.class).isValid());
		assertFalse(new Validator().validate(WsPart.builder().build(), NeoLoad.class).isValid());
		assertFalse(new Validator().validate(WsPart.builder().id("Body-1").name("Body").build(), NeoLoad.class).isValid());
	}

	@Test
	public void validateDuplicateRequestProfileNames() {
		final WebServicesSecurity security = WebServicesSecurity.builder()
				.addRequestProfiles(WsRequestProfile.builder().name("Profile").build())
				.addRequestProfiles(WsRequestProfile.builder().name("Profile").build())
				.build();

		assertEquals("Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR
				+ "Violation 1 - Incorrect value for 'request_profiles': must contain only unique names." + LINE_SEPARATOR,
				new Validator().validate(security, NeoLoad.class).getMessage().get());
	}

	@Test
	public void validateDuplicateResponseProfileNames() {
		final WebServicesSecurity security = WebServicesSecurity.builder()
				.addResponseProfiles(WsResponseProfile.builder().name("Profile").keystore("./wss-keystores/server.p12").build())
				.addResponseProfiles(WsResponseProfile.builder().name("Profile").keystore("./wss-keystores/server.p12").build())
				.build();

		assertEquals("Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR
				+ "Violation 1 - Incorrect value for 'response_profiles': must contain only unique names." + LINE_SEPARATOR,
				new Validator().validate(security, NeoLoad.class).getMessage().get());
	}
}
