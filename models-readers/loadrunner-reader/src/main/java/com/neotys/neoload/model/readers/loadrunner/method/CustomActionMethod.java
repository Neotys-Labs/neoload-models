package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.customaction.CustomActionMappingLoader;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.CustomActionParameter;
import com.neotys.neoload.model.repository.CustomActionParameter.Type;
import com.neotys.neoload.model.repository.ImmutableCustomAction;
import com.neotys.neoload.model.repository.ImmutableCustomAction.Builder;
import com.neotys.neoload.model.repository.ImmutableCustomActionParameter;

public class CustomActionMethod implements LoadRunnerMethod {

	private static final String ACTION_TYPE_KEY = "type";	
	private static final String IS_HIT_KEY = "isHit";
	private static final String NAME_KEY = "name";
	
	private static final String PARAMETERS_KEY = "parameters";
	private static final String PARAMETER_TYPE_KEY = "type";
	private static final String PARAMETER_VALUE_KEY = "value";

	public CustomActionMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		final Map<String, Object> mapping = CustomActionMappingLoader.getMapping().get(method.getName());
		if (mapping == null) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot find mapping for method " + method.getName());
			return null;
		}
		final Builder builder = ImmutableCustomAction.builder();

		final Object type = mapping.get(ACTION_TYPE_KEY);
		if (type instanceof String) {
			builder.type(type.toString());
		} else {
			visitorLogWarn(visitor, method.getName(), ctx, ACTION_TYPE_KEY);
			return null;
		}

		final Object isHitObject = mapping.get(IS_HIT_KEY);
		if (isHitObject instanceof Boolean) {
			builder.isHit((Boolean) isHitObject);
		} else {
			visitorLogWarn(visitor, method.getName(), ctx, IS_HIT_KEY);
			return null;
		}

		final Object name = mapping.get(NAME_KEY);
		if (name instanceof String) {
			builder.name(name.toString());
		} else {
			visitorLogWarn(visitor, method.getName(), ctx, NAME_KEY);
			return null;
		}

		visitor.readSupportedFunction(method.getName(), ctx);

		final Object parametersObject = mapping.get(PARAMETERS_KEY);
		if (parametersObject instanceof HashMap) {			
			handleParameters(builder, (HashMap<?,?>)parametersObject);
		}

		//final String arg0 = method.getParameters().get(0); TODO seb						

		final CustomAction customAction = builder.build();
		visitor.addInCurrentContainer(customAction);
		return customAction;
	}

	private static void handleParameters(final Builder builder, final HashMap<?,?> parametersObject) {
		for(final Entry<?, ?> parameter : parametersObject.entrySet()){
			final CustomActionParameter customActionParameter = getCustomActionParameter(parameter);
			if(customActionParameter != null){
				builder.addParameters(customActionParameter);
			}				
		}
	}

	private static void visitorLogWarn(final LoadRunnerVUVisitor visitor, final String methodName, final MethodcallContext ctx, final String actionTypeKey) {
		visitor.readSupportedFunctionWithWarn(methodName, ctx, "Invalid key " + actionTypeKey + " for method " + methodName);
	}

	private static CustomActionParameter getCustomActionParameter(final Entry<?, ?> parameter) {
		final ImmutableCustomActionParameter.Builder paramBuilder = ImmutableCustomActionParameter.builder();	
		
		final Object key = parameter.getKey();
		if (key instanceof String) {
			paramBuilder.name(key.toString());
		} else {
			return null;
		}
		
		final Object value = parameter.getValue();
		if (value instanceof Map) {
			final Object typeParamObject = ((Map<?,?>)value).get(PARAMETER_TYPE_KEY);
			if (typeParamObject instanceof String) {
				paramBuilder.type(Type.valueOf(typeParamObject.toString()));
			} else {
				return null;
			}
			final Object valueParamObject = ((Map<?,?>)parameter.getValue()).get(PARAMETER_VALUE_KEY);
			if (valueParamObject instanceof String) {
				paramBuilder.value(valueParamObject.toString());
			} else {
				return null;
			}					
		} else {
			return null;
		}
		return paramBuilder.build();
	}
}
