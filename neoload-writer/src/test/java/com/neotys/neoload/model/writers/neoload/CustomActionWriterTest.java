package com.neotys.neoload.model.writers.neoload;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.CustomActionParameter.Type;
import com.neotys.neoload.model.repository.ImmutableCustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomActionParameter;

public class CustomActionWriterTest {

	private static final String CONNECTION_STRING = "   /SAP_CODEPAGE=1100   /FULLMENU SNC_PARTNERNAME=\"\" SNC_QOP=-1 /H/${SAP_IP_ADDRESS}/S/3217 /UPDOWNLOAD_CP=2";
	
	@Test
	public void writeCustomActionXmlTest() throws ParserConfigurationException, TransformerException {
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		final CustomAction customAction = ImmutableCustomAction.builder()
				.name("Connect")
				.type("SapConnect")
				.isHit(true)
				.parameters(ImmutableList.of(ImmutableCustomActionParameter.builder()
						.name("connectionString")
						.value(CONNECTION_STRING)
						.type(Type.TEXT)
						.build()))
				.build();
				
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		CustomActionWriter.of(customAction).writeXML(doc, root, outputfolder);
		final String generatedResultXML = WrittingTestUtils.getXmlString(doc);
				
		final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root>"
				+ "<custom-action actionType=\"SapConnect\" isHit=\"true\" name=\"Connect\" "
				+ "uid=\"" + WriterUtils.getElementUid(customAction)+ "\">"
				+ "<custom-action-parameter name=\"connectionString\" type=\"TEXT\" value=\"" + CONNECTION_STRING.replace("\"", "&quot;") + "\"/>"
				+ "</custom-action></test-root>";
				
		Assertions.assertThat(generatedResultXML).isEqualTo(expectedResultXML);		
	}
	
	@Test
	public void testCopyFileToExtlib() throws ParserConfigurationException, IOException{
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);
		final File library = File.createTempFile("myLibrary", ".jar");
		library.deleteOnExit();
		final CustomAction customAction = ImmutableCustomAction.builder()
				.name("Connect")
				.type("SapConnect")
				.isHit(true)
				.libraryPath(library.toPath())
				.parameters(ImmutableList.of(ImmutableCustomActionParameter.builder()
						.name("connectionString")
						.value(CONNECTION_STRING)
						.type(Type.TEXT)
						.build()))
				.build();
				
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final File extlib = new File(outputfolder + File.separator + "lib" + File.separator + "extlib");					
		CustomActionWriter.of(customAction).writeXML(doc, root, outputfolder);
		assertEquals(1, extlib.listFiles().length);
		assertEquals(library.getName(), extlib.listFiles()[0].getName());						
		assertEquals(extlib.getAbsolutePath(), extlib.listFiles()[0].getParent());
	}
	

}
