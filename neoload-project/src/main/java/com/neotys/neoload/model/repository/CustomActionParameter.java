package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface CustomActionParameter {
	String getName();	
	String getValue();
	Type getType();
	
	public enum Type {
		TEXT,
		PASSWORD
	}	
}
