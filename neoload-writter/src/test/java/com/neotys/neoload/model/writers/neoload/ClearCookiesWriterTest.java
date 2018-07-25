package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ClearCookies;
import com.neotys.neoload.model.repository.ImmutableClearCookies;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ClearCookiesWriterTest {

	@Test
	public void writeClearCookiesXmlTest() throws ParserConfigurationException, TransformerException {
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		final ClearCookies clearCookies = ImmutableClearCookies.builder().name("web_cleanup_cookies").build();

		ClearCookiesWriter.of(clearCookies).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
		final String generatedResult = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedResult.substring(generatedResult.indexOf("ts=") + 4, generatedResult.indexOf("ts=") + 17);
		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"scripts/jsAction_" + WriterUtils.getElementUid(clearCookies)+ ".js\" "
				+ "name=\"web_cleanup_cookies\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(clearCookies)+ "\"/></test-root>";

		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
	}

}
