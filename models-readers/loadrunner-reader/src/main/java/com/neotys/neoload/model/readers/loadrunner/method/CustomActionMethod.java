package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomAction;

public class CustomActionMethod implements LoadRunnerMethod {
	
	public CustomActionMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);			
		visitor.readSupportedFunction(method.getName(), ctx);	
		//final String arg0 = method.getParameters().get(0);						
		final CustomAction customAction = ImmutableCustomAction.builder().name("name").type("type").isHit(true).build();
		visitor.addInCurrentContainer(customAction);
		return customAction;
	}	
}	
