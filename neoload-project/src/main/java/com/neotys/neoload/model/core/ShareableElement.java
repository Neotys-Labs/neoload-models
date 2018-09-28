package com.neotys.neoload.model.core;

public interface ShareableElement extends Element{
	default boolean isShared(){
		return true;
	}
}
