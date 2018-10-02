package com.neotys.neoload.model.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.repository.ImmutableContainer;
import com.neotys.neoload.model.repository.ImmutableDelay;
import com.neotys.neoload.model.repository.ImmutableUserPath;


import java.util.Optional;
import java.util.stream.Stream;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = ImmutableProject.class, name = "Project"),
//        @JsonSubTypes.Type(value = ImmutableUserPath.class, name = "UserPath"),
//        @JsonSubTypes.Type(value = ImmutableDelay.class, name = "Delay"),
//        @JsonSubTypes.Type(value = ImmutableContainer.class, name = "Container") }
//)
public interface Element {
    String getName();
    Optional<String> getDescription();
    Element withName(String of);

    default Stream<Element> flattened() {
        return Stream.of(this);
    }
}
