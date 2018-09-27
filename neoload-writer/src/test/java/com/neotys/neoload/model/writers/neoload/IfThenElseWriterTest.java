package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class IfThenElseWriterTest {
	
	@Test
    public void testWriteXML() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><if-action name=\"condition\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.IF_THEN_TEST_1)
    			+ "\"><then-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SET_OK_CODE_CUSTOM_ACTION)+ "\"/>"
    			+ "</then-container><else-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SET_OK_CODE_CUSTOM_ACTION)+ "\"/>"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.IS_OBJECT_AVAILABLE_CUSTOM_ACTION)+ "\"/>"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.IF_THEN_TEST_2)+ "\"/></else-container>"
    			+ "<conditions match-type=\"1\"><condition operand1=\"${sapgui_is_object_available_1}\" operand2=\"true\" operator=\"EQUALS\"/>"
    			+ "</conditions></if-action><custom-action actionType=\"SetText\" isHit=\"false\" name=\"setOkCode\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SET_OK_CODE_CUSTOM_ACTION)+ "\">"
    			+ "<custom-action-parameter name=\"objectId\" type=\"TEXT\" value=\"x\"/></custom-action>"
    			+ "<custom-action actionType=\"SetText\" isHit=\"false\" name=\"setOkCode\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SET_OK_CODE_CUSTOM_ACTION)+ "\">"
    			+ "<custom-action-parameter name=\"objectId\" type=\"TEXT\" value=\"x\"/></custom-action><custom-action actionType=\"IsAvailable\" "
    			+ "isHit=\"false\" name=\"isObjectAvailable\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.IS_OBJECT_AVAILABLE_CUSTOM_ACTION)+ "\">"
    			+ "<custom-action-parameter name=\"objectId\" type=\"TEXT\" value=\"x\"/></custom-action>"
    			+ "<if-action name=\"condition\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.IF_THEN_TEST_2)+ "\">"
    			+ "<then-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SET_OK_CODE_CUSTOM_ACTION)+ "\"/>"
    			+ "</then-container><else-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\"/>"
    			+ "<conditions match-type=\"1\"><condition operand1=\"${sapgui_is_object_available_2}\" operand2=\"true\" operator=\"EQUALS\"/>"
    			+ "</conditions></if-action><custom-action actionType=\"SetText\" isHit=\"false\" "
    			+ "name=\"setOkCode\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SET_OK_CODE_CUSTOM_ACTION)+ "\">"
				+ "<custom-action-parameter name=\"objectId\" type=\"TEXT\" value=\"x\"/></custom-action>"
    			+ "</test-root>";
    	IfThenElseWriter.of(WrittingTestUtils.IF_THEN_TEST_1).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);

    }

}
