package com.neotys.neoload;

import org.junit.Assert;
import org.junit.Test;

import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.writers.neoload.WriterUtils;

public class LrToNlTest {

	@Test
	public void test_lr_think_time() throws Exception{
		final Container model = LrReaderUtil.read("lr_think_time(1);");
		final String actualXml = NlWriterUtil.write(model);
		final String uid = WriterUtils.getElementUid(model.getChilds().get(0));
		final String expectedXml = "<delay-action duration=\"1000\" isThinkTime=\"true\" name=\"delay\" uid=\"" + uid + "\"/>"; 
		Assert.assertEquals(expectedXml, actualXml);
	}	

}
