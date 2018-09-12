package com.neotys.neoload;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.Files;
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
	
	@Test
	public void test_lr_save_string() throws Exception{
		final String setVariable = "Set variable ";
		assertJavascriptXMLandContent("lr_save_string(\"\",\"a\");", "context.variableManager.setValue(\"a\", \"\");", setVariable+"a");	
		assertJavascriptXMLandContent("lr_save_string( \"variableValue1\" , \"variableName\" ) ; ; ", "context.variableManager.setValue(\"variableName\", \"variableValue1\");", setVariable+"variableName");	
		assertJavascriptXMLandContent("lr_save_string(\"variableValue2\",lr_eval_string(\"{variableName}\"));", "context.variableManager.setValue(context.variableManager.getValue(\"variableName\"), \"variableValue2\");", setVariable+"__variableName_");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"{variableValue2}\"),\"{variableName}\");", "context.variableManager.setValue(\"{variableName}\", context.variableManager.getValue(\"variableValue2\"));", setVariable+"_variableName_");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"variableValue2\"),\"variableName\");", "context.variableManager.setValue(\"variableName\", \"variableValue2\");", setVariable+"variableName");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"{variableValue2}\"),\"variableName\");", "context.variableManager.setValue(\"variableName\", context.variableManager.getValue(\"variableValue2\"));", setVariable+"variableName");
		assertJavascriptXMLandContent("atoi(\"1\");", "context.variableManager.setValue(\"atoi_1\", parseInt(\"1\"));", "atoi_1");
		assertJavascriptXMLandContent("atoi(\"1\");", "context.variableManager.setValue(\"atoi_2\", parseInt(\"1\"));", "atoi_2");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"{atoi_2}\"),\"think_time\");", "context.variableManager.setValue(\"think_time\", context.variableManager.getValue(\"atoi_2\"));", setVariable + "think_time");			
	}

	public void assertJavascriptXMLandContent(final String lrMethod, final String expectedResultJS, final String expectedJSName) throws Exception {
		// Read
		final Container model = LrReaderUtil.read(lrMethod);
		// Write
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final String actualXml = NlWriterUtil.write(model, outputfolder);
		// Check XML content
		Assert.assertEquals(NlWriterUtil.getExpectedJSXml(model.getChilds().get(0), actualXml, expectedJSName), actualXml);
		// Check JS content
		final String uid = WriterUtils.getElementUid(model.getChilds().get(0));
		Assertions.assertThat(NlWriterUtil.readFile(outputfolder + "/scripts/jsAction_" + uid + ".js")).isEqualTo(expectedResultJS);
	}

	
}
