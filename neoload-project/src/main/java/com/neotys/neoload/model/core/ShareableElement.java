package com.neotys.neoload.model.core;

import org.immutables.value.Value;

public interface ShareableElement extends Element{
	@Value.Default()
	default boolean isShared(){
		return false;
	}
}
