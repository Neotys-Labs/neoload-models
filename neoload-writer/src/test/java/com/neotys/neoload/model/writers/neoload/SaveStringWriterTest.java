package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

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
	public void writeSaveStringXmlTest() throws ParserConfigurationException, TransformerException, IOException {
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		final SaveString saveString = ImmutableSaveString.builder()
				.name("Set variable think_time")
				.variableName("think_time")
				.variableValue("1")				
				.build();
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		SaveStringWriter.of(saveString).writeXML(doc, root, outputfolder);
		String generatedResultXML = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedResultXML.substring(generatedResultXML.indexOf("ts=") + 4, generatedResultXML.indexOf("ts=") + 17);
		final String jsFile = "scripts/jsAction_" + WriterUtils.getElementUid(saveString)+ ".js";
		final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"" + jsFile + "\" "
				+ "name=\"Set variable think_time\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(saveString)+ "\"/></test-root>";
		final String expectedResultJS = "context.variableManager.setValue(\"think_time\", \"1\");";
		final String generatedJS = Files.asCharSource(new File(outputfolder + File.separator + jsFile), Charset.defaultCharset()).read();	
		Assertions.assertThat(generatedResultXML).isEqualTo(expectedResultXML);
		Assertions.assertThat(generatedJS).isEqualTo(expectedResultJS);
	}	
}
