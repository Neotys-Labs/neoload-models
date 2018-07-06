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
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><action-test>11d41df2e5c0549a0080cc401905902ac8c6834879b56495f337d2b3079f6ce2</action-test></test-root>";
    	
    	Page page = ImmutablePage.builder()
                .name("TEST")
                .thinkTime(0)
                .build();
    	WriterUtils.generateEmbeddedAction(doc, root, page, "PagePath", Optional.of("action-test"));
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
}
