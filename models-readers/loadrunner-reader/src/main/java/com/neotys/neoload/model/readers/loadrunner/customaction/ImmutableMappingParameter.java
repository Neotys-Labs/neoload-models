package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.util.Map;
import java.util.Map.Entry;

import com.neotys.neoload.model.repository.ArgumentNumber;
import com.neotys.neoload.model.repository.CustomActionParameter.Type;

public class ImmutableMappingParameter {
	
	private static final String TYPE_KEY = "type";
	private static final String VALUE_KEY = "value";
	
	private final String name;
	private final ImmutableMappingValue value;
	private final Type type;

	private ImmutableMappingParameter(final String name, final ImmutableMappingValue value, final Type type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Either<String, ArgumentNumber> getValue() {
		return value.getValue();
	}
	
	public Type getType() {
		return type;
	}
	
	public static ImmutableMappingParameter build(final Entry<?,?> parameterEntry){
		final Object nameObject = parameterEntry.getKey();
		if (!(nameObject instanceof String)) {
			return null;
		}
		final String name = nameObject.toString();
		final Object mapObject = parameterEntry.getValue();
		if (!(mapObject instanceof Map)) {
			return null;
		}		
		final Map<?,?> map = (Map<?,?>)mapObject;
		final Object typeObject = map.get(TYPE_KEY);
		if (!(typeObject instanceof String)) {			
			return null;
		}
		final Type type = Type.valueOf(typeObject.toString());
		final Object valueObject = map.get(VALUE_KEY);
		if (!(valueObject instanceof String)) {			
			return null;
		}
		final ImmutableMappingValue value = ImmutableMappingValue.build(valueObject.toString());
		return new ImmutableMappingParameter(name, value, type);		
	}
}
