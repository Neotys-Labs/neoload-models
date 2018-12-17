package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableJavascript.class)
@Deprecated
public interface Javascript extends Element {
	String getContent();
}
