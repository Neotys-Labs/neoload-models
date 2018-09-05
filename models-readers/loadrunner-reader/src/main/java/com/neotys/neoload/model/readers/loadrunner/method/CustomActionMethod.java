package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.customaction.CustomActionMappingLoader;
import com.neotys.neoload.model.readers.loadrunner.customaction.ImmutableMappingMethod;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomAction.Builder;
import com.neotys.neoload.model.repository.ImmutableCustomActionParameter;

public class CustomActionMethod implements LoadRunnerMethod {

	public CustomActionMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		final ImmutableMappingMethod methodMapping = CustomActionMappingLoader.getMapping().get(method.getName());
		if (methodMapping == null) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot find mapping for method " + method.getName());
			return null;
		}
		visitor.readSupportedFunction(method.getName(), ctx);
		final Builder builder = ImmutableCustomAction.builder();
		builder.type(methodMapping.getType());
		builder.isHit(methodMapping.isHit());
		builder.name(methodMapping.getName());
		methodMapping.getParameters().forEach(p ->  {
			final ImmutableCustomActionParameter.Builder paramBuilder = ImmutableCustomActionParameter.builder();	
			paramBuilder.name(p.getName());
			paramBuilder.value(p.getValue(method.getParameters()));			
			paramBuilder.type(p.getType());	
			builder.addParameters(paramBuilder.build());
		});
		final CustomAction customAction = builder.build();
		visitor.addInCurrentContainer(customAction);
		return customAction;
	}	
}
