package com.neotys.neoload.model.writers.neoload;

import com.google.common.collect.ImmutableList;
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
                .isDynamic(false)
                .build();
        assertThat(WriterUtils.<ElementWriter>getWriterFor(page).getClass().getSimpleName()).isEqualTo("PageWriter");
    }


    @Test
    public void getWriterForVariableTest() {
        FileVariable var = ImmutableFileVariable.builder()
                .name("TEST")
                .fileName("myfile")
                .policy(Variable.VariablePolicy.EACH_USE)
                .order(Variable.VariableOrder.SEQUENTIAL)
                .firstLineIsColumnName(false)
                .columnsNames(ImmutableList.of("col1"))
                .columnsDelimiter(",")
                .scope(Variable.VariableScope.LOCAL)
                .numOfFirstRowData(0)
                .build();
        assertThat(WriterUtils.<VariableWriter>getWriterFor(var).getClass().getSimpleName()).isEqualTo("FileVariableWriter");
    }
    
    @Test
    public void generateEmbeddedActionTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	
    	final Page page = ImmutablePage.builder()
                .name("TEST")
                .thinkTime(0)
                .isDynamic(false)
                .build();
    	WriterUtils.generateEmbeddedAction(doc, root, page, Optional.of("action-test"));
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><action-test>" + WriterUtils.getElementUid(page)+ "</action-test></test-root>";
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
}
