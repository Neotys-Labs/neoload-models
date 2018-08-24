package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ClearCache;
import com.neotys.neoload.model.repository.ImmutableClearCache;

public class ClearCacheWriterTest {
	
	@Test
    public void writeClearCacheXmlTest() throws ParserConfigurationException, TransformerException, IOException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	
    	final ClearCache clearCache = ImmutableClearCache.builder().name("web_cache_cleanup").build();
    	final String outputfolder = Files.createTempDir().getAbsolutePath();
    	ClearCacheWriter.of(clearCache).writeXML(doc, root, outputfolder);
    	final String generatedResultXML = WrittingTestUtils.getXmlString(doc);
    	final String timestamp = generatedResultXML.substring(generatedResultXML.indexOf("ts=") + 4, generatedResultXML.indexOf("ts=") + 17);
    	final String jsFile = "scripts/jsAction_" + WriterUtils.getElementUid(clearCache)+ ".js";
    	final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><js-action filename=\"" + jsFile + "\" "
    					+ "name=\"web_cache_cleanup\" ts=\"" + timestamp + "\" "
    							+ "uid=\"" + WriterUtils.getElementUid(clearCache)+ "\"/></test-root>";
    	final String expectedResultJS = "// Deletes all cache information for the Virtual User instance.\ncontext.currentVU.clearCache();";
    	final String generatedJS = Files.asCharSource(new File(outputfolder + File.separator + jsFile), Charset.defaultCharset()).read();    	
		Assertions.assertThat(generatedResultXML).isEqualTo(expectedResultXML);
		Assertions.assertThat(generatedJS).isEqualTo(expectedResultJS);
    }

}
