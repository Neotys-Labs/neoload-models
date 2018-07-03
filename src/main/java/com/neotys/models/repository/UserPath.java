package com.neotys.models.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.models.core.Element;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUserPath.class)
public interface UserPath extends Element {
	Container getInitContainer();
	Container getActionsContainer();
	Container getEndContainer();
}
