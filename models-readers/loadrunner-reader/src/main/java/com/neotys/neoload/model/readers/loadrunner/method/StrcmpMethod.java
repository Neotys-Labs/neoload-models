package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.function.ImmutableStrcmp;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;

public class StrcmpMethod implements LoadRunnerMethod {
	
	private static AtomicInteger counter = new AtomicInteger(0); 

	public StrcmpMethod() {
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
		final String s0 = method.getParameters().get(0);
		final String s1 = method.getParameters().get(1);
		final List<String> args = ImmutableList.of(s0, s1);
		final String name = "strcmp_" + counter.incrementAndGet();
		return ImmutableList.of(ImmutableStrcmp.builder()
				.name(name)
				.args(args)
				.returnValue(MethodUtils.getVariableSyntax(name))
				.build());
	}	
}	
