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

public class LrsavestringMethod implements LoadRunnerMethod {

	public LrsavestringMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		if(method.getParameters() == null || method.getParameters().size()!=2){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have 2 parameters");
			return Collections.emptyList();
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);
		final String variableValue = MethodUtils.unquote(method.getParameters().get(0));
		final String variableName = MethodUtils.unquote(method.getParameters().get(1));			
		final String name = "Set variable " + MethodUtils.normalizeName(variableName);
		return ImmutableList.of(ImmutableSaveString.builder().name(name).variableName(variableName).variableValue(variableValue).build());		
	}
}
