package com.neotys.neoload.model.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

import java.util.stream.Stream;

@Value.Immutable
@JsonDeserialize(as = ImmutableUserPath.class)
@Deprecated
public interface UserPath extends Element {
	ContainerForMulti getInitContainer();
	ContainerForMulti getActionsContainer();
	ContainerForMulti getEndContainer();

	@Override
	default Stream<Element> flattened() {
		return Stream.of(getInitContainer(), getActionsContainer(), getEndContainer()).flatMap(ContainerForMulti::flattened);
	}
}
