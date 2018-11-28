package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.ContainerElement;
import com.neotys.neoload.model.v3.project.Element;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Container.DO})
@JsonDeserialize(as = ImmutableTransaction.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Transaction extends ContainerElement {
	class Builder extends ImmutableTransaction.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
