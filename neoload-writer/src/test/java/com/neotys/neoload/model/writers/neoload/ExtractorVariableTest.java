package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.VariableExtractor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ExtractorVariableTest {
	
	
	@Test
	public void writeXmlTestHeaders() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = ImmutableVariableExtractor.builder()
				.name("webRegSaveParam_TEST")
				.startExpression("left boundary")
				.endExpression("right boundary")
				.nbOccur(2)
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.HEADERS)
				.build();
		
	
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"0\" endsWithSimple=\"right boundary\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"1\" extractTypeAdv=\"0\" extractTypeSimple=\"1\" matchNumber=\"2\" matchNumberAdv=\"2\" matchNumberSimple=\"2\" name=\"webRegSaveParam_TEST\" "
    			+ "regExp=\"left boundary(.*?)right boundary\" "
    			+ "regExpAdv=\"left boundary(.*?)right boundary\" setDefaultValue=\"false\" startsWithSimple=\"left boundary\" "
    			+ "template=\"$1$\" templateAdv=\"$1$\">"
				+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";
	
    	
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ExtractorWriter.of(inputExtractor).writeXML(doc, root);
		String generatedExtractor = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedExtractor).isEqualTo(expectedResult);
	}
	
	@Test
	public void writeXmlTestBody() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = ImmutableVariableExtractor.builder()
				.name("webRegSaveParam_TEST")
				.startExpression("left boundary")
				.endExpression("right boundary")
				.nbOccur(2)
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.BODY)
				.build();
		
	
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"0\" endsWithSimple=\"right boundary\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"0\" extractTypeAdv=\"0\" extractTypeSimple=\"0\" matchNumber=\"2\" matchNumberAdv=\"2\" matchNumberSimple=\"2\" name=\"webRegSaveParam_TEST\" "
    			+ "regExp=\"left boundary(.*?)right boundary\" "
    			+ "regExpAdv=\"left boundary(.*?)right boundary\" setDefaultValue=\"false\" startsWithSimple=\"left boundary\" "
    			+ "template=\"$1$\" templateAdv=\"$1$\">"
				+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";
	
    	
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ExtractorWriter.of(inputExtractor).writeXML(doc, root);
		String generatedExtractor = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedExtractor).isEqualTo(expectedResult);
	}
	
	@Test
	public void writeXmlTestBoth() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = ImmutableVariableExtractor.builder()
				.name("webRegSaveParam_TEST")
				.startExpression("left boundary")
				.endExpression("right boundary")
				.nbOccur(2)
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.BOTH)
				.build();
		
	
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"0\" endsWithSimple=\"right boundary\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"5\" extractTypeAdv=\"0\" extractTypeSimple=\"5\" matchNumber=\"2\" matchNumberAdv=\"2\" matchNumberSimple=\"2\" "
    			+ "name=\"webRegSaveParam_TEST\" "
    			+ "regExp=\"left boundary(.*?)right boundary\" "
    			+ "regExpAdv=\"left boundary(.*?)right boundary\" setDefaultValue=\"false\" startsWithSimple=\"left boundary\" "
    			+ "template=\"$1$\" templateAdv=\"$1$\">"
				+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";
	
    	
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ExtractorWriter.of(inputExtractor).writeXML(doc, root);
		String generatedExtractor = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedExtractor).isEqualTo(expectedResult);
	}
	
	@Test
	public void writeXmlTestRegexp() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = ImmutableVariableExtractor.builder()
				.name("SecurityString")
				.regExp("\"[A-Z0-9a-z\\\\+]+==\\|([0-9]+)\"\\];")		
				.group("3")
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.BOTH)
				.build();
		
	
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"1\" endsWithSimple=\"\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"5\" extractTypeAdv=\"0\" extractTypeSimple=\"5\" matchNumber=\"1\" matchNumberAdv=\"1\" matchNumberSimple=\"1\" name=\"SecurityString\" "
    			+ "regExp=\"&quot;[A-Z0-9a-z\\\\+]+==\\|([0-9]+)&quot;\\];\" "
    			+ "regExpAdv=\"&quot;[A-Z0-9a-z\\\\+]+==\\|([0-9]+)&quot;\\];\" setDefaultValue=\"false\" "
    			+ "startsWithSimple=\"\" template=\"$3$\" templateAdv=\"$3$\">"
    			+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" "
    			+ "type=\"4\"/></variable-extractor></test-root>";
	    	
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ExtractorWriter.of(inputExtractor).writeXML(doc, root);
		String generatedExtractor = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedExtractor).isEqualTo(expectedResult);
	}
	
	@Test
	public void writeXmlTestXPath() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = ImmutableVariableExtractor.builder()
				.name("ExtractedVariable_0")
				.xPath("myXPATH")
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.XPATH)
				.build();
		
	
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" defaultValue=\"&lt;NOT FOUND&gt;\" "
    			+ "displayMode=\"0\" endsWithSimple=\"\" extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"3\" extractTypeAdv=\"0\" extractTypeSimple=\"3\" matchNumber=\"1\" "
    			+ "matchNumberAdv=\"1\" matchNumberSimple=\"1\" name=\"ExtractedVariable_0\" "
    			+ "regExp=\"(.*?)\" regExpAdv=\"(.*?)\" "
    			+ "setDefaultValue=\"false\" startsWithSimple=\"\" template=\"$1$\" templateAdv=\"$1$\" "
    			+ "xpath=\"myXPATH\">"
    			+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";
	    	
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ExtractorWriter.of(inputExtractor).writeXML(doc, root);
		String generatedExtractor = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedExtractor).isEqualTo(expectedResult);
	}
	
	@Test
	public void writeXmlTestJsonPath() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = ImmutableVariableExtractor.builder()
				.name("ExtractedVariable_0")
				.jsonPath("myJSONPath")
				.exitOnError(true)
				.extractType(VariableExtractor.ExtractType.JSON)
				.build();
		
		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" defaultValue=\"&lt;NOT FOUND&gt;\" "
    			+ "displayMode=\"0\" endsWithSimple=\"\" extractFromVarName=\"\" extractFromVarNameAdv=\"\" "
    			+ "extractFromVarNameSimple=\"\" extractType=\"4\" extractTypeAdv=\"0\" extractTypeSimple=\"4\" "
    			+ "jsonpath=\"myJSONPath\" matchNumber=\"1\" matchNumberAdv=\"1\" matchNumberSimple=\"1\" "
    			+ "name=\"ExtractedVariable_0\" regExp=\"(.*?)\" "
    			+ "regExpAdv=\"(.*?)\" setDefaultValue=\"false\" startsWithSimple=\"\" template=\"$1$\" "
    			+ "templateAdv=\"$1$\">"
    			+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";
	    	
		
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ExtractorWriter.of(inputExtractor).writeXML(doc, root);
		String generatedExtractor = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedExtractor).isEqualTo(expectedResult);
	}
	
}
