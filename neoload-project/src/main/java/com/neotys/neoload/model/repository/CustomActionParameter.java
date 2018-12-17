package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
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
