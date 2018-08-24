package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableEvalString;

public class LREvalStringMethod implements LoadRunnerMethod {

	public LREvalStringMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		Preconditions.checkNotNull(method.getParameters(), method.getName() + " method must have a parameter");
		if(method.getParameters().isEmpty()){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have a parameter");
			return null;
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);	
		final String returnValue = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		return ImmutableEvalString.builder().name(MethodUtils.unquote(method.getName())).returnValue(returnValue).build();
	}	
}	
