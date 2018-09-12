package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableDelay;

public class LrthinktimeMethod  implements LoadRunnerMethod {
	
	public LrthinktimeMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		Preconditions.checkNotNull(method.getParameters(), method.getName() + " method must have a parameter");
		Preconditions.checkArgument(!method.getParameters().isEmpty(), method.getName() + " method must have a parameter");		
		final String delayInS = method.getParameters().get(0);
		final String delayInMs = parseDelay(visitor, method, ctx, delayInS);		
		return ImmutableList.of(ImmutableDelay.builder().name("delay").delay(delayInMs).isThinkTime(true).build());
	}

	private static String parseDelay(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx, final String delayInS) {
		final Pattern pattern = Pattern.compile("[a-z-_]", Pattern.CASE_INSENSITIVE);
		if(pattern.matcher(delayInS).find() && !delayInS.contains("{") && !delayInS.contains("}")){
			visitor.readSupportedFunction(method.getName(), ctx);
			return MethodUtils.getVariableSyntax(delayInS)+"000";
		}		
		try {
			final String delayInMs = String.valueOf(Math.round(Double.parseDouble(delayInS) * 1000));			
			visitor.readSupportedFunction(method.getName(), ctx);
			return delayInMs;
		} catch (final NumberFormatException nfe) {
			final String warning = "A think time cannot be converted in milli seconds";
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, warning);
			return delayInS + "000";
		}
	}

}
