package com.neotys.models.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.converters.datamodel.ImmutableProject;
import com.neotys.converters.datamodel.repository.ImmutableContainer;
import com.neotys.converters.datamodel.repository.ImmutableDelay;
import com.neotys.converters.datamodel.repository.ImmutableUserPath;

import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type", visible=true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImmutableProject.class, name = "Project"),
        @JsonSubTypes.Type(value = ImmutableUserPath.class, name = "UserPath"),
        @JsonSubTypes.Type(value = ImmutableDelay.class, name = "Delay"),
        @JsonSubTypes.Type(value = ImmutableContainer.class, name = "Container") }
)
public interface Element {
    String getName();
    Optional<String> getDescription();
    Element withName(String of);
}
