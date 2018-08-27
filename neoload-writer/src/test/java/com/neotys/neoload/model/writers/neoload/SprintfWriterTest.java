package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ImmutableSprintf;
import com.neotys.neoload.model.repository.Sprintf;

public class SprintfWriterTest {

	@Test
	public void writeSprintfXmlTest() throws ParserConfigurationException, TransformerException, IOException {
		// sprintf(formattedVariable,"format_%s",rawVariable);
		assertXMLDefAndJSContent("\"format_%s\"", 
				ImmutableList.of("rawVariable"), 
				"var sprintf = java.lang.String.format(\"format_%s\", context.variableManager.getValue(\"rawVariable\"));\ncontext.variableManager.setValue(\"formattedVariable\", sprintf);");
		
		// sprintf(formattedVariable,"format_%s",${Type});
		assertXMLDefAndJSContent("\"format_%s\"", 
				ImmutableList.of("Type"), 
				"var sprintf = java.lang.String.format(\"format_%s\", context.variableManager.getValue(\"Type\"));\ncontext.variableManager.setValue(\"formattedVariable\", sprintf);");
		
		// sprintf(formattedVariable,"format_%s_%s",variable1, variable2);
		assertXMLDefAndJSContent("\"format_%s_%s\"", 
				ImmutableList.of("variable1", "variable2"), 
				"var sprintf = java.lang.String.format(\"format_%s_%s\", context.variableManager.getValue(\"variable1\"), context.variableManager.getValue(\"variable2\"));\ncontext.variableManager.setValue(\"formattedVariable\", sprintf);");
		
		// sprintf(formattedVariable,"{format_%s}",rawVariable);
		assertXMLDefAndJSContent("\"{format_%s}\"", 
				ImmutableList.of("rawVariable"), 
				"var sprintf = java.lang.String.format(\"{format_%s}\", context.variableManager.getValue(\"rawVariable\"));\ncontext.variableManager.setValue(\"formattedVariable\", sprintf);");
		
		// NOT SUPPORTED YET sprintf(formattedVariable,"format_%d",i+1);
		//assertXMLDefAndJSContent("\"format_%d\"", 
		// 		ImmutableList.of("i+1"), 
		// 		"var sprintf = java.lang.String.format(\"format_%d\", context.variableManager.getValue(\"rawVariable\")+1);\ncontext.variableManager.setValue(\"formattedVariable\", sprintf);");
		
		// sprintf(formattedVariable,"");
		assertXMLDefAndJSContent("\"\"", 
				ImmutableList.of(), 
				"var sprintf = java.lang.String.format(\"\");\ncontext.variableManager.setValue(\"formattedVariable\", sprintf);");
	}

	private static void assertXMLDefAndJSContent(final String format, final List<String> arg, final String expectedResultJS) throws ParserConfigurationException, TransformerException, IOException {
		final Sprintf sprintf = ImmutableSprintf.builder().name("sprintf").variableName("formattedVariable").format(format).args(arg).build();
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		SprintfWriter.of(sprintf).writeXML(doc, root, outputfolder);
		final String actualResultXML = WrittingTestUtils.getXmlString(doc);
		final String timestamp = actualResultXML.substring(actualResultXML.indexOf("ts=") + 4, actualResultXML.indexOf("ts=") + 17);
		final String jsFile = "scripts/jsAction_" + WriterUtils.getElementUid(sprintf) + ".js";
		final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"" + jsFile + "\" "
				+ "name=\"sprintf\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(sprintf) + "\"/></test-root>";
		final String actualJS = Files.asCharSource(new File(outputfolder + File.separator + jsFile), Charset.defaultCharset()).read();
		Assertions.assertThat(actualResultXML).isEqualTo(expectedResultXML);
		Assertions.assertThat(actualJS).isEqualTo(expectedResultJS);
	}
}
