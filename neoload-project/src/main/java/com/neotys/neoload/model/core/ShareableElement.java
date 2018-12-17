package com.neotys.neoload.model.core;

import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface ShareableElement extends Element{
	@Value.Default()
	default boolean isShared(){
		return false;
	}
}
