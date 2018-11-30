package com.neotys.neoload.model.v3.project;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ElementsDeserializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public interface ContainerElement extends ShareableElement {
	String DO = "do";

	@JsonProperty(DO)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonDeserialize(using = ElementsDeserializer.class)
	List<Element> getElements();
	
    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this), getElements().stream().flatMap(Element::flattened));
    }
}