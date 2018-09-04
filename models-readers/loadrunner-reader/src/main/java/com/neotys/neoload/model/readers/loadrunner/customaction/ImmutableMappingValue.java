package com.neotys.neoload.model.readers.loadrunner.customaction;

import com.neotys.neoload.model.repository.ArgumentNumber;
import com.neotys.neoload.model.repository.ImmutableArgumentNumber;

public class ImmutableMappingValue {
	
	private static final String ARG_PATTERN = "arg";
	
	private final Either<String,ArgumentNumber> value;	
	
	protected ImmutableMappingValue(final String stringValue) {
		this.value = Either.left(stringValue);		
	}
	
	protected ImmutableMappingValue(final int index) {
		this.value = Either.right(ImmutableArgumentNumber.builder().index(index).build());		
	}
	
	protected Either<String, ArgumentNumber> getValue() {
		return value;
	}

	public static ImmutableMappingValue build(final String string) {
		if(string.startsWith(ARG_PATTERN)){
			final String index = string.substring(ARG_PATTERN.length());
			try{
				return new ImmutableMappingValue(Integer.parseInt(index));
			} catch(final NumberFormatException e){				
			}
		}
		return new ImmutableMappingValue(string);
	}
}
