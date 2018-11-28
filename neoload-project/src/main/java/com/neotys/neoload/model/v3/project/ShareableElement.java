package com.neotys.neoload.model.v3.project;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ShareableElement extends Element{
	@JsonIgnore
	@Value.Default
	default boolean isShared(){
		return false;
	}
}
