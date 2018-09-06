package com.neotys.neoload.model.readers.loadrunner.method;

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

	public SapguilogonMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		if(method.getParameters() == null || method.getParameters().size() < 4){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have at least 4 parameters");
			return null;
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);
		visitor.addInCurrentContainer(createUsername(method));
		visitor.addInCurrentContainer(createPassword(method));
		visitor.addInCurrentContainer(createClientNum(method));		
		visitor.addInCurrentContainer(createLanguage(method));
		visitor.addInCurrentContainer(createPressEnter());
		return null;		
	}

	private static ImmutableCustomAction createUsername(final MethodCall method) {
		return createSetTextCustomAction(
				"username", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-BNAME", 
				"RSYST-BNAME", 
				"GuiTextField", 
				method.getParameters().get(0), 
				Type.TEXT
			);		
	}
	
	private static ImmutableCustomAction createPassword(final MethodCall method) {
		return createSetTextCustomAction(
				"password", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/pwdRSYST-BCODE", 
				"RSYST-BCODE", 
				"GuiTextField", 
				method.getParameters().get(1), 
				Type.PASSWORD
			);		
	}
	
	private static ImmutableCustomAction createClientNum(final MethodCall method) {
		return createSetTextCustomAction(
				"clientnum", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-MANDT", 
				"RSYST-MANDT", 
				"GuiTextField", 
				method.getParameters().get(2), 
				Type.TEXT
			);		
	}
		
	private static ImmutableCustomAction createLanguage(final MethodCall method) {
		return createSetTextCustomAction(
				"language", 
				"${SAP_ACTIVE_SESSION}/${SAP_ACTIVE_WINDOW}/usr/txtRSYST-LANGU", 
				"RSYST-LANGU", 
				"GuiTextField", 
				method.getParameters().get(3), 
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
