package com.neotys.neoload.model.v3.project.population;

import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Population.USER_PATHS})
@JsonDeserialize(as = ImmutablePopulation.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Population extends Element {
	String USER_PATHS = "user_paths";

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonProperty(USER_PATHS)
	List<UserPathPolicy> getUserPaths();

	class Builder extends ImmutablePopulation.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
