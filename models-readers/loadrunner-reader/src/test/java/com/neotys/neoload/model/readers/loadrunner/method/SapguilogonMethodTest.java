package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.CustomActionParameter.Type;
@SuppressWarnings("squid:S2699")
public class SapguilogonMethodTest {
	
	@Test
	public void test_sapgui_logon_() {
		final List<Element> customAcions = (new SapguilogonMethod()).getElement(LOAD_RUNNER_VISITOR,
				ImmutableMethodCall.builder()
					.name("sapgui_logon")
					.addParameters("\"{username}\"")
					.addParameters("\"{password}\"")
					.addParameters("\"{client}\"")
					.addParameters("\"EN\"")
					.build()
				, METHOD_CALL_CONTEXT);
		
		assertEquals(5, customAcions.size());
		final CustomAction username = (CustomAction) customAcions.get(0);
		assertEquals("SetText GuiTextField (username)", username.getName());
		assertEquals("SapSetText", username.getType());
		assertEquals(false, username.isHit());
		assertEquals(4, username.getParameters().size());
		assertEquals("objectId", username.getParameters().get(0).getName());
		assertEquals(Type.TEXT, username.getParameters().get(0).getType());
		assertEquals("${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-BNAME", username.getParameters().get(0).getValue());
		assertEquals("objectName", username.getParameters().get(1).getName());
		assertEquals(Type.TEXT, username.getParameters().get(1).getType());
		assertEquals("RSYST-BNAME", username.getParameters().get(1).getValue());
		assertEquals("objectType", username.getParameters().get(2).getName());
		assertEquals(Type.TEXT, username.getParameters().get(2).getType());
		assertEquals("GuiTextField", username.getParameters().get(2).getValue());
		assertEquals("value", username.getParameters().get(3).getName());
		assertEquals(Type.TEXT, username.getParameters().get(3).getType());
		assertEquals("{username}", username.getParameters().get(3).getValue());		
		
		final CustomAction password = (CustomAction) customAcions.get(1);
		assertEquals("SetText GuiTextField (password)", password.getName());
		assertEquals("SapSetText", password.getType());
		assertEquals(false, password.isHit());
		assertEquals(4, password.getParameters().size());
		assertEquals("objectId", password.getParameters().get(0).getName());
		assertEquals(Type.TEXT, password.getParameters().get(0).getType());
		assertEquals("${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/pwdRSYST-BCODE", password.getParameters().get(0).getValue());
		assertEquals("objectName", password.getParameters().get(1).getName());
		assertEquals(Type.TEXT, password.getParameters().get(1).getType());
		assertEquals("RSYST-BCODE", password.getParameters().get(1).getValue());
		assertEquals("objectType", password.getParameters().get(2).getName());
		assertEquals(Type.TEXT, password.getParameters().get(2).getType());
		assertEquals("GuiTextField", password.getParameters().get(2).getValue());
		assertEquals("value", password.getParameters().get(3).getName());
		assertEquals(Type.PASSWORD, password.getParameters().get(3).getType());
		assertEquals("{password}", password.getParameters().get(3).getValue());	

		final CustomAction clientnum = (CustomAction) customAcions.get(2);
		assertEquals("SetText GuiTextField (clientnum)", clientnum.getName());
		assertEquals("SapSetText", clientnum.getType());
		assertEquals(false, clientnum.isHit());
		assertEquals(4, clientnum.getParameters().size());
		assertEquals("objectId", clientnum.getParameters().get(0).getName());
		assertEquals(Type.TEXT, clientnum.getParameters().get(0).getType());
		assertEquals("${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-MANDT", clientnum.getParameters().get(0).getValue());
		assertEquals("objectName", clientnum.getParameters().get(1).getName());
		assertEquals(Type.TEXT, clientnum.getParameters().get(1).getType());
		assertEquals("RSYST-MANDT", clientnum.getParameters().get(1).getValue());
		assertEquals("objectType", clientnum.getParameters().get(2).getName());
		assertEquals(Type.TEXT, clientnum.getParameters().get(2).getType());
		assertEquals("GuiTextField", clientnum.getParameters().get(2).getValue());
		assertEquals("value", clientnum.getParameters().get(3).getName());
		assertEquals(Type.TEXT, clientnum.getParameters().get(3).getType());
		assertEquals("{client}", clientnum.getParameters().get(3).getValue());		
		
		final CustomAction language = (CustomAction) customAcions.get(3);
		assertEquals("SetText GuiTextField (language)", language.getName());
		assertEquals("SapSetText", language.getType());
		assertEquals(false, language.isHit());
		assertEquals(4, language.getParameters().size());
		assertEquals("objectId", language.getParameters().get(0).getName());
		assertEquals(Type.TEXT, language.getParameters().get(0).getType());
		assertEquals("${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-LANGU", language.getParameters().get(0).getValue());
		assertEquals("objectName", language.getParameters().get(1).getName());
		assertEquals(Type.TEXT, language.getParameters().get(1).getType());
		assertEquals("RSYST-LANGU", language.getParameters().get(1).getValue());
		assertEquals("objectType", language.getParameters().get(2).getName());
		assertEquals(Type.TEXT, language.getParameters().get(2).getType());
		assertEquals("GuiTextField", language.getParameters().get(2).getValue());
		assertEquals("value", language.getParameters().get(3).getName());
		assertEquals(Type.TEXT, language.getParameters().get(3).getType());
		assertEquals("EN", language.getParameters().get(3).getValue());		
		
		final CustomAction pressEnter = (CustomAction) customAcions.get(4);
		assertEquals("Press GuiMainWindow (SAP)", pressEnter.getName());
		assertEquals("SapPress", pressEnter.getType());
		assertEquals(true, pressEnter.isHit());
		assertEquals(4, pressEnter.getParameters().size());
		assertEquals("objectId", pressEnter.getParameters().get(0).getName());
		assertEquals(Type.TEXT, pressEnter.getParameters().get(0).getType());
		assertEquals("${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}", pressEnter.getParameters().get(0).getValue());
		assertEquals("objectName", pressEnter.getParameters().get(1).getName());
		assertEquals(Type.TEXT, pressEnter.getParameters().get(1).getType());
		assertEquals("${SAP_ACTIVE_WINDOW}", pressEnter.getParameters().get(1).getValue());
		assertEquals("objectType", pressEnter.getParameters().get(2).getName());
		assertEquals(Type.TEXT, pressEnter.getParameters().get(2).getType());
		assertEquals("GuiMainWindow", pressEnter.getParameters().get(2).getValue());
		assertEquals("key", pressEnter.getParameters().get(3).getName());
		assertEquals(Type.TEXT, pressEnter.getParameters().get(3).getType());
		assertEquals("Enter", pressEnter.getParameters().get(3).getValue());		
	}
}
