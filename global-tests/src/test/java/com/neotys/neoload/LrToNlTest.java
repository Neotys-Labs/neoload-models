package com.neotys.neoload;

import org.junit.Assert;
import org.junit.Test;

import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.writers.neoload.WriterUtils;

public class LrToNlTest {

	@Test
	public void test_lr_think_time() throws Exception{
		Container model = LrReaderUtil.read("lr_think_time(1);");
		String actualXml = NlWriterUtil.write(model);
		String uid = WriterUtils.getElementUid(model.getChilds().get(0));
		String expectedXml = "<delay-action duration=\"1000\" isThinkTime=\"true\" name=\"delay\" uid=\"" + uid + "\"/>"; 
		Assert.assertEquals(expectedXml, actualXml);
		
		model = LrReaderUtil.read("lr_think_time(lr_eval_string(\"{think_time}0\"))");
		actualXml = NlWriterUtil.write(model);
		uid = WriterUtils.getElementUid(model.getChilds().get(0));
		expectedXml = "<delay-action duration=\"${think_time}0000\" isThinkTime=\"true\" name=\"delay\" uid=\"" + uid + "\"/>"; 
		Assert.assertEquals(expectedXml, actualXml);
		
		model = LrReaderUtil.read("lr_think_time(lr_eval_string(lr_eval_string(\"{think_time}\")));");
		actualXml = NlWriterUtil.write(model);
		uid = WriterUtils.getElementUid(model.getChilds().get(0));
		expectedXml = "<delay-action duration=\"${think_time}000\" isThinkTime=\"true\" name=\"delay\" uid=\"" + uid + "\"/>"; 
		Assert.assertEquals(expectedXml, actualXml);
	}	
	
	@Test
	public void test_lr_eval_string() throws Exception{		
		Assert.assertEquals("", NlWriterUtil.write(LrReaderUtil.read("lr_eval_string(\"a\")")));
		Assert.assertEquals("", NlWriterUtil.write(LrReaderUtil.read("lr_eval_string(\"{a}\")")));
		Assert.assertEquals("", NlWriterUtil.write(LrReaderUtil.read("lr_eval_string(lr_eval_string(\"${a}\"))")));
	}	

}
