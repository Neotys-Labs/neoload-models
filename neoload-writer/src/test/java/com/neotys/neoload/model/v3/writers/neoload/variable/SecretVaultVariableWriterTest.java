package com.neotys.neoload.model.v3.writers.neoload.variable;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.variable.SecretVaultVariable;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;

public class SecretVaultVariableWriterTest {

	public static final SecretVaultVariable SECRET_VAULT = SecretVaultVariable.builder()
			.name("db_password")
			.providerId("665f1a2b3c4d5e6f7a8b9c0d")
			.secretIdentifier("my-app/db")
			.build();

	@Test
	public void writeXmlSecretVaultTest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><variable-keyvault name=\"db_password\" order=\"2\" policy=\"4\" range=\"2\""
				+ " whenOutOfValues=\"CYCLE_VALUES\" providerId=\"665f1a2b3c4d5e6f7a8b9c0d\""
				+ " secretPath=\"my-app/db\"/></test-root>";

		(new SecretVaultVariableWriter(SECRET_VAULT)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}
}
