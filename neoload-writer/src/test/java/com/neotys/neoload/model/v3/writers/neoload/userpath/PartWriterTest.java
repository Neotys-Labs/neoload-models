package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.Part;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;

public class PartWriterTest {

    @Test
    public void filePartwriteXmlTest() throws ParserConfigurationException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        Part part = Part.builder().filename("/myPath/myfilename").sourceFilename("/myPath/mysourcefilename").name("myFilePart").contentType("application/json").build();

        final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root><multipart-file attachedFilename=\"/myPath/mysourcefilename\"\n" +
                "            contentType=\"application/json\" filename=\"/myPath/myfilename\" name=\"myFilePart\"/></test-root>";

        PartWriter.of(part).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();

    }

    @Test
    public void stringPartwriteXmlTest() throws ParserConfigurationException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        Part part = Part.builder().value("My Part Value").name("myStringPart").contentType("application/json").build();

        final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root><multipart-string name=\"myStringPart\" \n" +
                "            value=\"My Part Value\" valueMode=\"USE_VALUE\" contentType=\"application/json\"/></test-root>";

        PartWriter.of(part).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();

    }
}
