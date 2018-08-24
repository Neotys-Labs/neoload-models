package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.function.Atoi;
import com.neotys.neoload.model.function.ImmutableAtoi;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;

public class AtoiMethod implements LoadRunnerMethod {
	
	private static int counter = 1; 

	public AtoiMethod() {
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
		final String arg0 = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final List<String> args = ImmutableList.of(arg0);
		final String name = "atoi_" + counter++;
		final Atoi atoi = ImmutableAtoi.builder().name(name).args(args).returnValue(MethodUtils.getVariableSyntax(name)).build();
		visitor.addInCurrentContainer(atoi);
		return atoi;
	}	
}	
