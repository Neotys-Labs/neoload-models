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
import com.neotys.neoload.model.repository.ImmutableSaveString;

public class SapguiselectactivewindowMethod implements LoadRunnerMethod {

	private static final String SAP_ACTIVE_WINDOW = "SAP_ACTIVE_WINDOW";
	
	public SapguiselectactivewindowMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		if(method.getParameters() == null || method.getParameters().isEmpty()){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have at least 1 parameter");
			return Collections.emptyList();
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);		
		return ImmutableList.of(ImmutableSaveString.builder()
				.name(MethodUtils.unquote(method.getName()))
				.variableName(SAP_ACTIVE_WINDOW)
				.variableValue(MethodUtils.unquote(method.getParameters().get(0)))
				.build());
	}
}
