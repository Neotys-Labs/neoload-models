package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
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
	
	private static final Set<String> INGORED_PARAMETER_VALUE = ImmutableSet.of("LAST", "");

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
			paramBuilder.value(MethodUtils.unquote(p.getValue().map(s -> s, a -> readParameter(method, a.getIndex(), readIndex))));			
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
				if(!INGORED_PARAMETER_VALUE.contains(unreadParameter)){
					unreadParameters.add(inputParameters.get(index));
				}
			}
		}
		return unreadParameters;
	}

	private static String readParameter(final MethodCall method, final int index, final Set<Integer> readIndex){
		readIndex.add(index);
		return method.getParameters().get(index);
	}
}
