package com.neotys.neoload.model.v3.binding.io;

import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Project;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Project.NAME, ProjectDescriptor.INCLUDES})
@JsonDeserialize(as = ImmutableProjectDescriptor.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ProjectDescriptor {
	String INCLUDES = "includes";

	Project DEFAULT_PROJECT = Project.builder().build();
	
	@JsonProperty(INCLUDES)
	List<String> getIncludes();
	
	@JsonUnwrapped	
	@Valid
	@Value.Default
	default Project getProject() {
		return DEFAULT_PROJECT;
	}
	
	class Builder extends ImmutableProjectDescriptor.Builder {}
	static Builder builder() {
		return new Builder();
	}
}

