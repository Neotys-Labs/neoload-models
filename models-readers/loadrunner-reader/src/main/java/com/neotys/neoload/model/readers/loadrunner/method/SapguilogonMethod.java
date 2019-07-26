package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.CustomActionParameter.Type;
import com.neotys.neoload.model.repository.ImmutableCustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomActionParameter;

public class SapguilogonMethod implements LoadRunnerMethod {

	private static final String GUI_TEXT_FIELD = "GuiTextField";
	public SapguilogonMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		if(method.getParameters() == null || method.getParameters().size() < 4){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have at least 4 parameters");
			return Collections.emptyList();
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);
		return ImmutableList.of(
				createUsername(visitor, method),
				createPassword(visitor, method),
				createClientNum(visitor, method),
				createLanguage(visitor, method),
				createPressEnter());				
	}

	private static ImmutableCustomAction createUsername(final LoadRunnerVUVisitor visitor, final MethodCall method) {
		return createSetTextCustomAction(
				"username", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-BNAME", 
				"RSYST-BNAME", 
				GUI_TEXT_FIELD,
				MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(),method.getParameters().get(0)),
				Type.TEXT
			);		
	}
	
	private static ImmutableCustomAction createPassword(final LoadRunnerVUVisitor visitor, final MethodCall method) {
		return createSetTextCustomAction(
				"password", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/pwdRSYST-BCODE", 
				"RSYST-BCODE", 
				GUI_TEXT_FIELD,
				MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(),method.getParameters().get(1)),
				Type.PASSWORD
			);		
	}
	
	private static ImmutableCustomAction createClientNum(final LoadRunnerVUVisitor visitor, final MethodCall method) {
		return createSetTextCustomAction(
				"clientnum", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-MANDT", 
				"RSYST-MANDT", 
				GUI_TEXT_FIELD,
				MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(),method.getParameters().get(2)),
				Type.TEXT
			);		
	}
		
	private static ImmutableCustomAction createLanguage(final LoadRunnerVUVisitor visitor, final MethodCall method) {
		return createSetTextCustomAction(
				"language", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-LANGU", 
				"RSYST-LANGU", 
				GUI_TEXT_FIELD,
				MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(),method.getParameters().get(3)),
				Type.TEXT
			);		
	}
	
	private static ImmutableCustomAction createPressEnter() {
		return createPressCustomAction(
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}", 
				"${SAP_ACTIVE_WINDOW}",
				"GuiMainWindow", 
				"Enter"
			);		
	}

	private static ImmutableCustomAction createSetTextCustomAction(final String name, final String objectId, final String objectName, final String objectType, final String value, final Type valueType) {
		return ImmutableCustomAction.builder()
				.name("SetText GuiTextField (" + name + ")")
				.type("SapSetText")
				.isHit(false)
				.parameters(
					ImmutableList.of(ImmutableCustomActionParameter.builder()
						.name("objectId").value(objectId).type(Type.TEXT).build(),
					ImmutableCustomActionParameter.builder()
						.name("objectName").value(objectName).type(Type.TEXT).build(),
					ImmutableCustomActionParameter.builder()
						.name("objectType").value(objectType).type(Type.TEXT).build(),
					ImmutableCustomActionParameter.builder()
						.name("value").value(MethodUtils.unquote(value)).type(valueType).build()
						))
				.build();
	}
	
	private static ImmutableCustomAction createPressCustomAction(final String objectId, final String objectName, final String objectType, final String key) {
		return ImmutableCustomAction.builder()
				.name("Press GuiMainWindow (SAP)")
				.type("SapPress")
				.isHit(true)
				.parameters(
					ImmutableList.of(ImmutableCustomActionParameter.builder()
						.name("objectId").value(objectId).type(Type.TEXT).build(),
					ImmutableCustomActionParameter.builder()
						.name("objectName").value(objectName).type(Type.TEXT).build(),
					ImmutableCustomActionParameter.builder()
						.name("objectType").value(objectType).type(Type.TEXT).build(),
					ImmutableCustomActionParameter.builder()
						.name("key").value(key).type(Type.TEXT).build()
						))
				.build();
	}
	
}
