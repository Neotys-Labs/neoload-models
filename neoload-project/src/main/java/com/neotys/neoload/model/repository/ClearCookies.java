package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableClearCookies.class)
@Deprecated
public interface ClearCookies extends Element {
	
}
