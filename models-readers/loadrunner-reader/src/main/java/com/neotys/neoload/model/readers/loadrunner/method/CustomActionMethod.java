package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableSet;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.customaction.CustomActionMappingLoader;
import com.neotys.neoload.model.readers.loadrunner.customaction.ImmutableMappingMethod;
import com.neotys.neoload.model.readers.loadrunner.customaction.MappingValueUtil;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomAction.Builder;
import com.neotys.neoload.model.repository.ImmutableCustomActionParameter;

public class CustomActionMethod implements LoadRunnerMethod {

	private final AtomicInteger counter = new AtomicInteger(0);
	
	private static final Set<String> IGNORED_PARAMETER_VALUE = ImmutableSet.of("LAST", "");
	
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
		final Builder builder = ImmutableCustomAction.builder();
		builder.type(methodMapping.getType());
		builder.isHit(methodMapping.isHit());
		builder.name(methodMapping.getName());
		final Set<Integer> readIndex = new HashSet<>();
		methodMapping.getParameters().forEach(p ->  {
			final ImmutableCustomActionParameter.Builder paramBuilder = ImmutableCustomActionParameter.builder();	
			paramBuilder.name(p.getName());			
			paramBuilder.value(MappingValueUtil.parseMappingValue(method.getParameters(), p.getValue(), method.getName(), counter, readIndex));			
			paramBuilder.type(p.getType());	
			builder.addParameters(paramBuilder.build());
		});
		final CustomAction customAction = builder.build();
		visitor.addInCurrentContainer(customAction);
		final Set<String> unreadParameters = getUnreadParameters(method.getParameters(), readIndex);
		if(unreadParameters.isEmpty()){
			visitor.readSupportedFunction(method.getName(), ctx);
		} else {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Unread parameter: " + unreadParameters.toString());
		}		
		return customAction;
	}	
	
	private static Set<String> getUnreadParameters(final List<String> inputParameters, final Set<Integer> readIndex) {
		final Set<String> unreadParameters = new HashSet<>();
		for(int index = 0; index < inputParameters.size(); index++){
			if(!readIndex.contains(index)){
				final String unreadParameter = inputParameters.get(index);
				if(!IGNORED_PARAMETER_VALUE.contains(unreadParameter)){
					unreadParameters.add(inputParameters.get(index));
				}
			}
		}
		return unreadParameters;
	}	
}
