package com.neotys.neoload.model.writers.neoload;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ImmutableSaveString;
import com.neotys.neoload.model.repository.SaveString;
public class SaveStringWriterTest {
	
	@Test
	public void writeSaveStringXmlTest() throws ParserConfigurationException, TransformerException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);

		final SaveString saveString = ImmutableSaveString.builder()
				.name("Set variable think_time")
				.variableName("think_time")
				.variableValue("1")				
				.build();
		
		SaveStringWriter.of(saveString).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
		String generatedResult = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedResult.substring(generatedResult.indexOf("ts=") + 4, generatedResult.indexOf("ts=") + 17);
		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"scripts/jsAction_" + WriterUtils.getElementUid(saveString)+ ".js\" "
				+ "name=\"Set variable think_time\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(saveString)+ "\"/></test-root>";

		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
	}	
}
