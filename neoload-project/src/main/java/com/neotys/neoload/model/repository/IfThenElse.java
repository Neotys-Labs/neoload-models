package com.neotys.neoload.model.repository;

import java.util.List;
import java.util.stream.Stream;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@Value.Immutable
@JsonDeserialize(as = ImmutableIfThenElse.class)
public interface  IfThenElse extends Element {
	Conditions getConditions();
	List<Element> getThen();
	List<Element> getElse();
	
	 @Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), Stream.concat(getThen().stream(), getElse().stream()));
	}
}

