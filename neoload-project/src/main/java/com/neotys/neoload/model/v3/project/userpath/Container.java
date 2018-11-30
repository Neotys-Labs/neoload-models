package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.ContainerElement;
import com.neotys.neoload.model.v3.project.Element;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, Container.DO})
@JsonDeserialize(as = ImmutableContainer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Container extends ContainerElement {
	String DEFAULT_NAME = "#container#";

	@JsonIgnore
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	class Builder extends ImmutableContainer.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
