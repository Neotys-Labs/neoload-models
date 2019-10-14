package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;

public class SwitchWriterTest {

    @Test
    public void testWriteXML() throws ParserConfigurationException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root>"
                + "<switch-container condition=\"sqdf\" name=\"switcher or Witcher\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SWITCH_TEST) + "\">"
                + "<description>a simple hunter</description>"
                + "<case-statement case-break=\"true\""
                + "        case-uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CASE_SWITCH)+ "\" case-value=\"qsd\""
                + "        element-number=\"1\" execution-type=\"0\""
                + "        slaProfileEnabled=\"false\" weightsEnabled=\"false\">"
                + "<description>Elements executed when the Case equals the Switch valueB.</description>"
                + "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.DELAY_SWITCH_TEST_CASE) +"\"/>"
                + "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_IN_CASE_SWITCH) +"\"/>"
                + "</case-statement>"
                + "<default-statement element-number=\"1\" execution-type=\"0\""
                + "        slaProfileEnabled=\"false\" weightsEnabled=\"false\">"
                + "<description>Elements executed by default when no Case equals the Switch value.</description>"
                + "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.DELAY_SWITCH_TEST_DEFAULT) +"\"/>"
                + "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_IN_DEFAULT_SWITCH) +"\"/>"
                + "</default-statement>"
                + "</switch-container>"
                + "<delay-action duration=\"0\" isThinkTime=\"false\" name=\"delayB\""
                + "    uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.DELAY_SWITCH_TEST_CASE)+ "\"/>"
                + "<basic-logical-action-container element-number=\"1\" execution-type=\"0\"" +
                "    name=\"b\"" +
                "    slaProfileEnabled=\"false\"" +
                "    uid=\""+ WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_IN_CASE_SWITCH) +"\" weightsEnabled=\"false\"/>"
                + "<delay-action duration=\"0\" isThinkTime=\"false\" name=\"delayB\""
                + "    uid=\""+ WriterUtils.getElementUid(WrittingTestUtils.DELAY_SWITCH_TEST_DEFAULT) + "\"/>"
                + "<basic-logical-action-container element-number=\"1\" execution-type=\"0\""
                + "    name=\"b\""
                + "    slaProfileEnabled=\"false\""
                + "    uid=\""+ WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_IN_DEFAULT_SWITCH) +"\" weightsEnabled=\"false\"/>"
                + "</test-root>";
        SwitchWriter.of(WrittingTestUtils.SWITCH_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();

    }

}
