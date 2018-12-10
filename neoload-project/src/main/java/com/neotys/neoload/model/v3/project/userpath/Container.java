package com.neotys.neoload.model.v3.project.userpath;

import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ElementsDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Container.STEPS})
@JsonDeserialize(as = ImmutableContainer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Container extends Step {
	String STEPS = "steps";

	@JsonProperty(STEPS)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonDeserialize(using = ElementsDeserializer.class)
	List<Step> getSteps();

	class Builder extends ImmutableContainer.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
