package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ImmutableMappingMethod {

	private static final String ACTION_TYPE_KEY = "type";	
	private static final String IS_HIT_KEY = "isHit";
	private static final String NAME_KEY = "name";	
	private static final String PARAMETERS_KEY = "parameters";
	
	private final String type;
	private final boolean isHit;
	private final String name;
	private final List<ImmutableMappingParameter> parameters;
	
	private ImmutableMappingMethod(final String type, final boolean isHit, final String name, final List<ImmutableMappingParameter> parameters) {
		this.type = type;
		this.isHit = isHit;
		this.name = name;
		this.parameters = parameters;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isHit() {
		return isHit;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ImmutableMappingParameter> getParameters() {
		return parameters;
	}
	
	public static ImmutableMappingMethod build(final Map<?,?> methodMapping){
		final Object typeObject = methodMapping.get(ACTION_TYPE_KEY);
		if (!(typeObject instanceof String)) {
			return null;
		}
		final String type = typeObject.toString();
		
		final Object isHitObject = methodMapping.get(IS_HIT_KEY);
		if (!(isHitObject instanceof Boolean)) {
			return null;
		}
		final boolean isHit = (Boolean) isHitObject;
		
		final Object nameObject = methodMapping.get(NAME_KEY);
		if (!(nameObject instanceof String)) {			
			return null;
		}
		final String name = nameObject.toString();
		
		final Object parametersObject = methodMapping.get(PARAMETERS_KEY);
		if (!(parametersObject instanceof Map)) {		
			return null;			
		}		
		final List<ImmutableMappingParameter> parameters = new ArrayList<>();
		for(final Entry<?, ?> entry : ((Map<?,?>)parametersObject).entrySet()){
			parameters.add(ImmutableMappingParameter.build(entry));
		}		
		return new ImmutableMappingMethod(type, isHit, name, parameters);
	}
}
