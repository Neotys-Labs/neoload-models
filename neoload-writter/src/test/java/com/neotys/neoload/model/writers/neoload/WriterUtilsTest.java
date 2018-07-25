package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.*;

public class WriterUtilsTest {

    @Test
    public void getWriterForPageTest() {
        Page page = ImmutablePage.builder()
                .name("TEST")
                .thinkTime(0)
                .build();
        assertThat(WriterUtils.getWriterFor(page).getClass().getSimpleName()).isEqualTo("PageWriter");
    }
    
    @Test
    public void generateEmbeddedActionTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	
    	final Page page = ImmutablePage.builder()
                .name("TEST")
                .thinkTime(0)
                .build();
    	WriterUtils.generateEmbeddedAction(doc, root, page, "PagePath", Optional.of("action-test"));
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><action-test>" + WriterUtils.getElementUid(page)+ "</action-test></test-root>";
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
}
