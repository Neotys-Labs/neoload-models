package com.neotys.neoload.model.repository;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableSprintf.class)
@Deprecated
public interface Sprintf extends Element {
	String getVariableName();
	String getFormat();
	List<String> getArgs();
}
