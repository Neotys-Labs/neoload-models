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
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Invalid key " + ACTION_TYPE_KEY + " for method " + method.getName());
			return null;
		}

		final Object isHitObject = mapping.get(IS_HIT_KEY);
		if (isHitObject instanceof Boolean) {
			builder.isHit((Boolean) isHitObject);
		} else {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Invalid key " + IS_HIT_KEY + " for method " + method.getName());
			return null;
		}

		final Object name = mapping.get(NAME_KEY);
		if (name instanceof String) {
			builder.name(name.toString());
		} else {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Invalid key " + NAME_KEY + " for method " + method.getName());
			return null;
		}

		visitor.readSupportedFunction(method.getName(), ctx);

		final Object parametersObject = mapping.get(PARAMETERS_KEY);
		if (parametersObject instanceof HashMap) {			
			for(final Entry<?, ?> parameter : ((HashMap<?, ?>) parametersObject).entrySet()){
				ImmutableCustomActionParameter.Builder paramBuilder = ImmutableCustomActionParameter.builder();				
				if (parameter.getKey() instanceof String) {
					paramBuilder.name(parameter.getKey().toString());
				} else {
					continue;
				}
				if (parameter.getValue() instanceof Map) {
					final Object typeParamObject = ((Map<?,?>)parameter.getValue()).get(PARAMETER_TYPE_KEY);
					if (typeParamObject instanceof String) {
						paramBuilder.type(Type.valueOf(typeParamObject.toString()));
					} else {
						continue;
					}
					final Object valueParamObject = ((Map<?,?>)parameter.getValue()).get(PARAMETER_VALUE_KEY);
					if (valueParamObject instanceof String) {
						paramBuilder.value(valueParamObject.toString());
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		}

		//final String arg0 = method.getParameters().get(0);						

		final CustomAction customAction = builder.build();
		visitor.addInCurrentContainer(customAction);
		return customAction;
	}
}
