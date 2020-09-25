package com.neotys.neoload.model.v3.writers.neoload.userpath.assertion;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;

public class AssertionsWriterTest {

    @Test
    public void writeXmlFforContentAssertionTest() throws ParserConfigurationException {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.contains("contains_1")
    					.build(),   
    			ContentAssertion.builder()
    					.not(true)
    					.contains("contains_2")
    					.regexp(false)
    					.build());
    	
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
        
    	AssertionsWriter.of(assertions).writeXML(document, root);
        
    	Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        
        final Node xmlAssertions = root.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getNodeName()).isEqualTo("assertion-content");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("contains_1");

        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getNodeName()).isEqualTo("assertion-content");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("contains_2");
    }

    @Test
    public void writeXmlFforPluginContentAssertionTest() throws ParserConfigurationException {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.xPath("xpath_1")
    					.contains("contains_1")
    					.build(),   
    			ContentAssertion.builder()
    					.xPath("xpath_2")
    					.not(true)
    					.contains("contains_2")
    					.regexp(false)
    					.build());
    	
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
        
    	AssertionsWriter.of(assertions).writeXML(document, root);
        
    	Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        
        final Node xmlAssertions = root.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getNodeName()).isEqualTo("assertion-plugin-content");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("contains_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("byXPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("xPath").getNodeValue()).isEqualTo("xpath_1");
                
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getNodeName()).isEqualTo("assertion-plugin-content");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("contains_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("byXPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("xPath").getNodeValue()).isEqualTo("xpath_2");
    }

    @Test
    public void writeXmlFforJsonContentAssertionTest() throws ParserConfigurationException {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.jsonPath("jsonpath_1")
    					.contains("contains_1")
    					.build(),   
    			ContentAssertion.builder()
    					.jsonPath("jsonpath_2")
    					.not(true)
    					.contains("contains_2")
    					.regexp(false)
    					.build());
    	
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
        
    	AssertionsWriter.of(assertions).writeXML(document, root);
        
    	Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        
        final Node xmlAssertions = root.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getNodeName()).isEqualTo("assertion-json-content");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("contains_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("byJsonPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("jsonPath").getNodeValue()).isEqualTo("jsonpath_1");
                
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getNodeName()).isEqualTo("assertion-json-content");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("contains_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("byJsonPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("jsonPath").getNodeValue()).isEqualTo("jsonpath_2");
    }

    @Test
    public void writeXmlFforResponseAssertionTest() throws ParserConfigurationException {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.contains("contains_1")
    					.regexp(true)
    					.build(),   
    			ContentAssertion.builder()
    					.not(true)
    					.contains("contains_2")
    					.regexp(true)
    					.build());
    	
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
        
    	AssertionsWriter.of(assertions).writeXML(document, root);
        
    	Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        
        final Node xmlAssertions = root.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getNodeName()).isEqualTo("assertion-response");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("containsType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("matchType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("testString");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("contains_1");

        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getNodeName()).isEqualTo("assertion-response");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("containsType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("matchType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("testString");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getChildNodes().item(0).getTextContent()).isEqualTo("contains_2");
    }

    @Test
    public void writeXmlFforPluginResponseAssertionTest() throws ParserConfigurationException {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.xPath("xpath_1")
    					.contains("contains_1")
    					.regexp(true)
    					.build(),   
    			ContentAssertion.builder()
    					.xPath("xpath_2")
    					.not(true)
    					.contains("contains_2")
    					.regexp(true)
    					.build());
    	
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
        
    	AssertionsWriter.of(assertions).writeXML(document, root);
        
    	Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        
        final Node xmlAssertions = root.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getNodeName()).isEqualTo("assertion-plugin-response");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("containsType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("matchType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("byXPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("xPath").getNodeValue()).isEqualTo("xpath_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("testString");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("contains_1");
                
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getNodeName()).isEqualTo("assertion-plugin-response");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("containsType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("matchType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("byXPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("xPath").getNodeValue()).isEqualTo("xpath_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("testString");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getChildNodes().item(0).getTextContent()).isEqualTo("contains_2");
    }

    @Test
    public void writeXmlFforJsonResponseAssertionTest() throws ParserConfigurationException {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.jsonPath("jsonpath_1")
    					.contains("contains_1")
    					.regexp(true)
    					.build(),  
    			ContentAssertion.builder()
    					.jsonPath("jsonpath_2")
    					.not(true)
    					.contains("contains_2")
    					.regexp(true)
    					.build());
    	
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
        
    	AssertionsWriter.of(assertions).writeXML(document, root);
        
    	Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        
        final Node xmlAssertions = root.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getNodeName()).isEqualTo("assertion-json-response");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("containsType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("matchType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("byJsonPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getAttributes().getNamedItem("jsonPath").getNodeValue()).isEqualTo("jsonpath_1");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("testString");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("contains_1");
  
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getNodeName()).isEqualTo("assertion-json-response");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("containsType").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("matchType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("byJsonPath").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getAttributes().getNamedItem("jsonPath").getNodeValue()).isEqualTo("jsonpath_2");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("testString");
        Assertions.assertThat(xmlAssertions.getChildNodes().item(1).getChildNodes().item(0).getTextContent()).isEqualTo("contains_2");
    }
}
