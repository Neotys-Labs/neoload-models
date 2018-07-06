package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ClearCache;
import com.neotys.neoload.model.repository.ImmutableClearCache;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ClearCacheWriterTest {
	
	@Test
    public void writeClearCacheXmlTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	
    	final ClearCache clearCache = ImmutableClearCache.builder().name("web_cache_cleanup").build();
    			
    	ClearCacheWriter.of(clearCache).writeXML(doc, root, "web_cache_cleanup", Files.createTempDir().getAbsolutePath());
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
    	final String timestamp = generatedResult.substring(generatedResult.indexOf("ts=") + 4, generatedResult.indexOf("ts=") + 17);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><js-action filename=\"scripts/jsAction_a19e40d7b1fff56a6612f73cb757319b53f65d05db79f2a72b98f60d6bf44b5d.js\" name=\"web_cache_cleanup\" ts=\"" + timestamp + "\" uid=\"a19e40d7b1fff56a6612f73cb757319b53f65d05db79f2a72b98f60d6bf44b5d\"/></test-root>";

		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);

    }

}
