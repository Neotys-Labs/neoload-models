package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
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
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		final ImmutableMappingMethod methodMapping = visitor.getCustomActionMappingMethod(method.getName());
		if (methodMapping == null) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot find mapping for method " + method.getName());
			return Collections.emptyList();
		}		
		final Set<Integer> readIndex = new HashSet<>();
		final Builder builder = ImmutableCustomAction.builder();
		builder.type(methodMapping.getType());
		builder.isHit(methodMapping.isHit());
		builder.name(MappingValueUtil.parseMappingValue(visitor, method.getParameters(), methodMapping.getName(), method.getName(), counter, readIndex));		
		methodMapping.getParameters().forEach(p ->  {
			final ImmutableCustomActionParameter.Builder paramBuilder = ImmutableCustomActionParameter.builder();	
			paramBuilder.name(p.getName());			
			paramBuilder.value(MappingValueUtil.parseMappingValue(visitor, method.getParameters(), p.getValue(), method.getName(), counter, readIndex));			
			paramBuilder.type(p.getType());	
			builder.addParameters(paramBuilder.build());
		});
		final CustomAction customAction = builder.build();		
		readIndex.addAll(methodMapping.getIgnoreArgs());
		final Set<String> unreadParameters = getUnreadParameters(method.getParameters(), readIndex);		
		if(unreadParameters.isEmpty()){
			visitor.readSupportedFunction(method.getName(), ctx);
		} else {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Unread parameter: " + unreadParameters.toString());
		}		
		return ImmutableList.of(customAction);		
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
