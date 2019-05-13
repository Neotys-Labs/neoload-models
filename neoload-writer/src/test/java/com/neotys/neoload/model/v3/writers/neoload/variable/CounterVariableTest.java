package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.variable.CounterVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class CounterVariableTest {

    public static final CounterVariable COUNTER = CounterVariable.builder()
            .name("CounterVariable")
            .description("Test decription")
            .start(7)
            .end(16)
            .increment(2)
            .order(Variable.Order.SEQUENTIAL)
            .scope(Variable.Scope.GLOBAL)
            .changePolicy(Variable.ChangePolicy.EACH_USE)
            .outOfValue(Variable.OutOfValue.STOP)
            .build();

    @Test
    public void writeXmlCounterTest() throws ParserConfigurationException {
        Document doc = WrittingTestUtils.generateEmptyDocument();
        Element root = WrittingTestUtils.generateTestRootElement(doc);
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root><variable-counter inc=\"2\" max=\"16\"" +
                " name=\"CounterVariable\" order=\"1\" policy=\"1\" range=\"1\"" +
                " starting=\"7\" whenOutOfValues=\"STOP_TEST\">" +
                "<description>Test decription</description>" +
                "</variable-counter></test-root>";

        (new CounterVariableWriter(COUNTER)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
}
