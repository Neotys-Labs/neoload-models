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
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	
    	final ClearCache clearCache = ImmutableClearCache.builder().name("web_cache_cleanup").build();
    			
    	ClearCacheWriter.of(clearCache).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
    	final String timestamp = generatedResult.substring(generatedResult.indexOf("ts=") + 4, generatedResult.indexOf("ts=") + 17);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><js-action filename=\"scripts/jsAction_" + WriterUtils.getElementUid(clearCache)+ ".js\" "
    					+ "name=\"web_cache_cleanup\" ts=\"" + timestamp + "\" "
    							+ "uid=\"" + WriterUtils.getElementUid(clearCache)+ "\"/></test-root>";

		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }

}
