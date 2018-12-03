package com.neotys.neoload.model.v3.project;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ElementsDeserializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public interface ContainerElement extends Element {
	String DO = "do";

	@JsonProperty(DO)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonDeserialize(using = ElementsDeserializer.class)
	List<Element> getElements();
	
	@JsonIgnore
	@Value.Default
	default boolean isShared(){
		return false;
	}

    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this), getElements().stream().flatMap(Element::flattened));
    }
}