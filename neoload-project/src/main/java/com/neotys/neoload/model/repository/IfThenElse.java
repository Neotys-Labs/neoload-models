package com.neotys.neoload.model.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@Value.Immutable
@JsonDeserialize(as = ImmutableIfThenElse.class)
public interface  IfThenElse extends Element {
	Conditions getConditions();
	Container getThen();
	Optional<Container> getElse();

    @Override
    default Stream<Element> flattened() {
    	if(getElse().isPresent()){
    		return Stream.of(getThen(), getElse().get()).flatMap(Container::flattened);	
    	}
    	return getThen().flattened();    	
    }
}

