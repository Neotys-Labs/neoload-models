package com.neotys.neoload.model.v3.project;

import java.util.Optional;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public interface Element {
	String NAME = "name";
	String DESCRIPTION = "description";

	@JsonProperty(NAME)
	@RequiredCheck(groups={NeoLoad.class})
	String getName();

	Optional<String> getDescription();
    
    Element withName(String of);

    default Stream<Element> flattened() {
        return Stream.of(this);
    }
}
