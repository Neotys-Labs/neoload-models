package com.neotys.neoload.model.repository;

import java.util.stream.Stream;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@Value.Immutable
@JsonDeserialize(as = ImmutableIfThenElse.class)
public interface  IfThenElse extends Element {
	Conditions getConditions();
	Container getThen();
	Container getElse();
	
	 @Override
	default Stream<Element> flattened() {
		 return Stream.of(getThen(), getElse()).flatMap(Container::flattened);
	}
}

