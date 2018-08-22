package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.UUID;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableEvalString;

public class LREvalStringMethod implements LoadRunnerMethod {

	public LREvalStringMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		Preconditions.checkNotNull(method.getParameters(), method.getName() + " method must have a parameter");
		Preconditions.checkArgument(!method.getParameters().isEmpty(), method.getName() + " method must have a parameter");		
		visitor.readSupportedFunction(method.getName(), ctx);		
		final String content = method.getParameters().get(0);		
		return ImmutableEvalString.builder().name(method.getName() + "_" + UUID.randomUUID().toString()).content(content).build();
	}
}
