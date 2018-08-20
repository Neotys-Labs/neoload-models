package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableDelay;

public class LRThinkTimeMethod  implements LoadRunnerMethod {
	
	public LRThinkTimeMethod() {
		super();
	}

	@Override
	public Element getElement(LoadRunnerVUVisitor visitor, MethodCall method, MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		Preconditions.checkNotNull(method.getParameters(), "A lr_think_time must have a parameter");
		Preconditions.checkArgument(!method.getParameters().isEmpty(),
				"A lr_think_time must have a parameter value");

		String delayInMs = method.getParameters().get(0);
		try {
			delayInMs = String.valueOf(Math.round(Double.parseDouble(method.getParameters().get(0)) * 1000));			
			visitor.readSupportedFunction(method.getName(), ctx);
		} catch (final NumberFormatException nfe) {
			final String warning = "A think time cannot be converted in milli seconds";
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, warning);
		}
		return ImmutableDelay.builder().name("delay").delay(delayInMs).isThinkTime(true).build();
	}

}
