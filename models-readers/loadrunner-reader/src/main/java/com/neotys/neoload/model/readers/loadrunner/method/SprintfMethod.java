package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableSprintf;

public class SprintfMethod implements LoadRunnerMethod {

	public SprintfMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		if(method.getParameters() == null || method.getParameters().size()<2){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have at least 2 parameters");
			return null;
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);
		final String variableName = method.getParameters().get(0);
		final String format = method.getParameters().get(1);			
		final List<String> args = new ArrayList<>();
		for(int index = 2; index < method.getParameters().size(); index++){
			args.add(MethodUtils.getVariableName(method.getParameters().get(index)));
		}		
		final Element element = ImmutableSprintf.builder().name(MethodUtils.unquote(method.getName())).variableName(variableName).format(format).args(args).build();
		visitor.addInCurrentContainer(element);
		return element;		
	}
}
