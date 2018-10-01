package com.neotys.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.IfThenElse;
import com.neotys.neoload.model.writers.neoload.WriterUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

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
	
	private static final AtomicInteger ATOI_COUNTER = new AtomicInteger(0);
	private static final AtomicInteger IS_OBJECT_AVAILABLE_COUNTER = new AtomicInteger(0);
		
	@Test
	public void test_lr_save_string() throws Exception{
		final String setVariable = "Set variable ";
		assertJavascriptXMLandContent("lr_save_string(\"\",\"a\");", "context.variableManager.setValue(\"a\", \"\");", setVariable+"a");	
		assertJavascriptXMLandContent("lr_save_string( \"variableValue1\" , \"variableName\" ) ; ; ", "context.variableManager.setValue(\"variableName\", \"variableValue1\");", setVariable+"variableName");	
		assertJavascriptXMLandContent("lr_save_string(\"variableValue2\",lr_eval_string(\"{variableName}\"));", "context.variableManager.setValue(context.variableManager.getValue(\"variableName\"), \"variableValue2\");", setVariable+"__variableName_");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"{variableValue2}\"),\"{variableName}\");", "context.variableManager.setValue(\"{variableName}\", context.variableManager.getValue(\"variableValue2\"));", setVariable+"_variableName_");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"variableValue2\"),\"variableName\");", "context.variableManager.setValue(\"variableName\", \"variableValue2\");", setVariable+"variableName");
		assertJavascriptXMLandContent("lr_save_string(lr_eval_string(\"{variableValue2}\"),\"variableName\");", "context.variableManager.setValue(\"variableName\", context.variableManager.getValue(\"variableValue2\"));", setVariable+"variableName");
		assertJavascriptXMLandContent("atoi(\"1\");", "context.variableManager.setValue(\"atoi_" + ATOI_COUNTER.incrementAndGet() + "\", parseInt(\"1\"));", "atoi_" + ATOI_COUNTER.get());
		assertJavascriptXMLandContent("atoi(\"1\");", "context.variableManager.setValue(\"atoi_" + ATOI_COUNTER.incrementAndGet() + "\", parseInt(\"1\"));", "atoi_" + ATOI_COUNTER.get());
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
	
	@Test
	public void test_lr_save_string_atoi() throws Exception{
		// Read
		final Container model = LrReaderUtil.read("lr_save_string(atoi(\"1\"),\"think_time\");");
		// Write
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final String actualXml = NlWriterUtil.write(model, outputfolder);
		// Check XML content
		final String firstXMLContentExpected = NlWriterUtil.getExpectedJSXml(model.getChilds().get(0), actualXml, "atoi_" + ATOI_COUNTER.incrementAndGet());
		final String secondXMLContentExpected = NlWriterUtil.getExpectedJSXml(model.getChilds().get(1), actualXml, "Set variable think_time"); 
		Assert.assertEquals(firstXMLContentExpected + secondXMLContentExpected, actualXml);
		// Check JS content
		final String uid1 = WriterUtils.getElementUid(model.getChilds().get(0));
		Assertions.assertThat(NlWriterUtil.readFile(outputfolder + "/scripts/jsAction_" + uid1 + ".js")).isEqualTo("context.variableManager.setValue(\"atoi_" + ATOI_COUNTER.get() + "\", parseInt(\"1\"));");
		final String uid2 = WriterUtils.getElementUid(model.getChilds().get(1));
		Assertions.assertThat(NlWriterUtil.readFile(outputfolder + "/scripts/jsAction_" + uid2 + ".js")).isEqualTo("context.variableManager.setValue(\"think_time\", context.variableManager.getValue(\"atoi_" + ATOI_COUNTER.get() + "\"));");				
	}
	
	@Test
	public void test_if_then_else() throws Exception{
		// Read
		final Container model = LrReaderUtil.read("if (sapgui_is_object_available(\"wnd[1]\")){lr_think_time(2);} else {lr_think_time(3);}");
		// Write
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final String actualXml = NlWriterUtil.write(model, outputfolder);
		// Check XML content
		final IfThenElse ifThenElse = (IfThenElse) model.getChilds().get(1);
		final int isObjectAvailable = IS_OBJECT_AVAILABLE_COUNTER.incrementAndGet();
		final String expectedXml = "<custom-action actionType=\"SapIsAvailable\" isHit=\"false\" "
				+ "name=\"isObjectAvailable\" uid=\"" + WriterUtils.getElementUid(model.getChilds().get(0))
				+ "\"><custom-action-parameter name=\"objectId\" type=\"TEXT\" "
				+ "value=\"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/wnd[1]\"/>"
				+ "<custom-action-parameter name=\"variable\" type=\"TEXT\" "
				+ "value=\"sapgui_is_object_available_" + isObjectAvailable + "\"/></custom-action>"
				+ "<if-action name=\"condition\" uid=\"" + WriterUtils.getElementUid(ifThenElse)
				+ "\"><description>sapgui_is_object_available(\"wnd[1]\")</description>"
				+ "<then-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
				+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0))
				+ "\"/></then-container><else-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
				+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(ifThenElse.getElse().getChilds().get(0))
				+ "\"/></else-container><conditions match-type=\"1\"><condition operand1=\"${sapgui_is_object_available_" + isObjectAvailable + "}\" "
				+ "operand2=\"true\" operator=\"EQUALS\"/></conditions></if-action><delay-action duration=\"2000\" "
				+ "isThinkTime=\"true\" name=\"delay\" uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0)) 
				+ "\"/><delay-action duration=\"3000\" isThinkTime=\"true\" name=\"delay\" uid=\"" 
				+ WriterUtils.getElementUid(ifThenElse.getElse().getChilds().get(0)) + "\"/>";
 		Assert.assertEquals(expectedXml, actualXml);						
	}
	
	@Test
	public void test_if_then_with_curly_brackets() throws Exception{
		// Read
		final Container model = LrReaderUtil.read("if (sapgui_is_object_available(\"wnd[1]\")){lr_think_time(2);}");
		// Write
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final String actualXml = NlWriterUtil.write(model, outputfolder);
		// Check XML content
		final IfThenElse ifThenElse = (IfThenElse) model.getChilds().get(1);
		final int isObjectAvailable = IS_OBJECT_AVAILABLE_COUNTER.incrementAndGet();
		final String expectedXml = "<custom-action actionType=\"SapIsAvailable\" isHit=\"false\" "
				+ "name=\"isObjectAvailable\" uid=\"" + WriterUtils.getElementUid(model.getChilds().get(0))
				+ "\"><custom-action-parameter name=\"objectId\" type=\"TEXT\" "
				+ "value=\"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/wnd[1]\"/>"
				+ "<custom-action-parameter name=\"variable\" type=\"TEXT\" "
				+ "value=\"sapgui_is_object_available_" + isObjectAvailable + "\"/></custom-action>"
				+ "<if-action name=\"condition\" uid=\"" + WriterUtils.getElementUid(ifThenElse)
				+ "\"><description>sapgui_is_object_available(\"wnd[1]\")</description>"
				+ "<then-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
				+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0))
				+ "\"/></then-container><else-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\"/>"
				+ "<conditions match-type=\"1\"><condition operand1=\"${sapgui_is_object_available_" + isObjectAvailable + "}\" "
				+ "operand2=\"true\" operator=\"EQUALS\"/></conditions></if-action><delay-action duration=\"2000\" "
				+ "isThinkTime=\"true\" name=\"delay\" uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0)) 
				+ "\"/>";
 		Assert.assertEquals(expectedXml, actualXml);						
	}
	
	@Test
	public void test_if_then_without_curly_brackets() throws Exception{
		// Read
		final Container model = LrReaderUtil.read("if (sapgui_is_object_available(\"wnd[1]\"))lr_think_time(2);");
		// Write
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final String actualXml = NlWriterUtil.write(model, outputfolder);
		// Check XML content
		final IfThenElse ifThenElse = (IfThenElse) model.getChilds().get(1);
		final int isObjectAvailable = IS_OBJECT_AVAILABLE_COUNTER.incrementAndGet();
		final String expectedXml = "<custom-action actionType=\"SapIsAvailable\" isHit=\"false\" "
				+ "name=\"isObjectAvailable\" uid=\"" + WriterUtils.getElementUid(model.getChilds().get(0))
				+ "\"><custom-action-parameter name=\"objectId\" type=\"TEXT\" "
				+ "value=\"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/wnd[1]\"/>"
				+ "<custom-action-parameter name=\"variable\" type=\"TEXT\" "
				+ "value=\"sapgui_is_object_available_" + isObjectAvailable + "\"/></custom-action>"
				+ "<if-action name=\"condition\" uid=\"" + WriterUtils.getElementUid(ifThenElse)
				+ "\"><description>sapgui_is_object_available(\"wnd[1]\")</description>"
				+ "<then-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
				+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0))
				+ "\"/></then-container><else-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\"/>"
				+ "<conditions match-type=\"1\"><condition operand1=\"${sapgui_is_object_available_" + isObjectAvailable + "}\" "
				+ "operand2=\"true\" operator=\"EQUALS\"/></conditions></if-action><delay-action duration=\"2000\" "
				+ "isThinkTime=\"true\" name=\"delay\" uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0)) 
				+ "\"/>";
 		Assert.assertEquals(expectedXml, actualXml);						
	}
	
	@Test
	public void test_if_then_negative_condition() throws Exception{
		// Read
		final Container model = LrReaderUtil.read("if (!sapgui_is_object_available(\"wnd[1]\")) { lr_think_time(2); }");
		// Write
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		final String actualXml = NlWriterUtil.write(model, outputfolder);
		// Check XML content
		final IfThenElse ifThenElse = (IfThenElse) model.getChilds().get(1);
		final int isObjectAvailable = IS_OBJECT_AVAILABLE_COUNTER.incrementAndGet();
		final String expectedXml = "<custom-action actionType=\"SapIsAvailable\" isHit=\"false\" "
				+ "name=\"isObjectAvailable\" uid=\"" + WriterUtils.getElementUid(model.getChilds().get(0))
				+ "\"><custom-action-parameter name=\"objectId\" type=\"TEXT\" "
				+ "value=\"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/wnd[1]\"/>"
				+ "<custom-action-parameter name=\"variable\" type=\"TEXT\" "
				+ "value=\"sapgui_is_object_available_" + isObjectAvailable + "\"/></custom-action>"
				+ "<if-action name=\"condition\" uid=\"" + WriterUtils.getElementUid(ifThenElse)
				+ "\"><description>!sapgui_is_object_available(\"wnd[1]\")</description>"
				+ "<then-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\">"
				+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0))
				+ "\"/></then-container><else-container element-number=\"1\" execution-type=\"0\" weightsEnabled=\"false\"/>"
				+ "<conditions match-type=\"1\"><condition operand1=\"${sapgui_is_object_available_" + isObjectAvailable + "}\" "
				+ "operand2=\"false\" operator=\"EQUALS\"/></conditions></if-action><delay-action duration=\"2000\" "
				+ "isThinkTime=\"true\" name=\"delay\" uid=\"" + WriterUtils.getElementUid(ifThenElse.getThen().getChilds().get(0)) 
				+ "\"/>";
 		Assert.assertEquals(expectedXml, actualXml);						
	}
	
}
