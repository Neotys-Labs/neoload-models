package com.neotys.neoload.model.v3.project;

import org.immutables.value.Value;

public interface ShareableElement extends Element{
	@Value.Default()
	default boolean isShared(){
		return false;
	}
}
