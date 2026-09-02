package com.neotys.neoload.model.v3.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.ElementNameCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import java.util.stream.Stream;

public interface Element {
	String NAME = "name";
	String DESCRIPTION = "description";

	@JsonProperty(NAME)
	@RequiredCheck(groups={NeoLoad.class})
	@ElementNameCheck(groups={NeoLoad.class})
	String getName();

	@JsonProperty(DESCRIPTION)
	Optional<String> getDescription();
    
    Element withName(String of);

    default Stream<Element> flattened() {
        return Stream.of(this);
    }
}
