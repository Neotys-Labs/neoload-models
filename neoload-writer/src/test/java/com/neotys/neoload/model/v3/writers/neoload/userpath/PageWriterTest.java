package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.ImmutablePage;
import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Page;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;

public class PageWriterTest {

    @Test
    public void writePageTestClassic() throws ParserConfigurationException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        final ImmutableRequest request = Request.builder().url("http://1.com").build();
        final Page page = ImmutablePage.builder()
                .name("myPage")
                .description("myDescription")
                .isDynamic(true)
                .addChildren(request)
                .build();
        final String requestUid = WriterUtils.getElementUid(request);
        final String expectedResult =
                "<test-root>" +
                    "<http-page executeResourcesDynamically=\"true\" name=\"myPage\" uid=\""+ WriterUtils.getElementUid(page)+"\">" +
                        "<description>myDescription</description>" +
                        "<embedded-action>"+ requestUid +"</embedded-action>" +
                    "</http-page>" +
                    "<http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" name=\"#request#\" path=\"\" slaProfileEnabled=\"false\" uid=\""+requestUid+"\"/>" +
                "</test-root>";
        PageWriter.of(page).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

    @Test
    public void writePageTest() throws ParserConfigurationException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        final Request request1 = Request.builder().url("http://2.com").build();
        final Request request2 = Request.builder().url("http://3.com").build();
        final Page page = ImmutablePage.builder()
                .name("myPage")
                .description("myDescription")
                .thinkTime("10-20")
                .addChildren(request1)
                .addChildren(request2)
                .build();
        final String request1Uid = WriterUtils.getElementUid(request1);
        final String request2Uid = WriterUtils.getElementUid(request2);
        final String expectedResult =
                "<test-root>" +
                    "<http-page executeResourcesDynamically=\"false\" name=\"myPage\" thinkTimeMode=\"MODE_RANGE_THINK_TIME\" thinkTimeRangeEnd=\"20\" thinkTimeRangeStart=\"10\" uid=\""+ WriterUtils.getElementUid(page)+"\">" +
                        "<description>myDescription</description>" +
                        "<embedded-action>"+ request1Uid +"</embedded-action>" +
                        "<embedded-action>"+ request2Uid +"</embedded-action>" +
                    "</http-page>" +
                    "<http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" name=\"#request#\" path=\"\" slaProfileEnabled=\"false\" uid=\""+ request1Uid +"\"/>" +
                    "<http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" name=\"#request#\" path=\"\" slaProfileEnabled=\"false\" uid=\""+ request2Uid +"\"/>" +
                "</test-root>";
        PageWriter.of(page).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

    @Test
    public void writePageSLATest() throws ParserConfigurationException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        final Request request1 = Request.builder().url("http://2.com").build();
        final Request request2 = Request.builder().url("http://3.com").build();
        final Page page = ImmutablePage.builder()
                .name("myPage")
                .description("myDescription")
                .thinkTime("10-20")
                .addChildren(request1)
                .addChildren(request2)
                .slaProfile("toto")
                .build();
        final String request1Uid = WriterUtils.getElementUid(request1);
        final String request2Uid = WriterUtils.getElementUid(request2);
        final String expectedResult =
                "<test-root>" +
                        "<http-page executeResourcesDynamically=\"false\" name=\"myPage\" slaProfileEnabled=\"true\" slaProfileName=\"toto\" thinkTimeMode=\"MODE_RANGE_THINK_TIME\" thinkTimeRangeEnd=\"20\" thinkTimeRangeStart=\"10\" uid=\""+ WriterUtils.getElementUid(page)+"\">" +
                        "<description>myDescription</description>" +
                        "<embedded-action>"+ request1Uid +"</embedded-action>" +
                        "<embedded-action>"+ request2Uid +"</embedded-action>" +
                        "</http-page>" +
                        "<http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" name=\"#request#\" path=\"\" slaProfileEnabled=\"false\" uid=\""+ request1Uid +"\"/>" +
                        "<http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" name=\"#request#\" path=\"\" slaProfileEnabled=\"false\" uid=\""+ request2Uid +"\"/>" +
                        "</test-root>";
        PageWriter.of(page).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
}
