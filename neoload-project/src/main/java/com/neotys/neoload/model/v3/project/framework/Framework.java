package com.neotys.neoload.model.v3.project.framework;

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
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Framework.ENABLED, Framework.PARAMETERS})
@JsonDeserialize(as = ImmutableFramework.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Framework extends Element {
	String ENABLED = "enabled";
	String PARAMETERS = "parameters";

	@JsonProperty(ENABLED)
	@Value.Default
	default boolean isEnabled() {
		return true;
	}

	@JsonProperty(PARAMETERS)
	@UniqueElementNameCheck(groups = {NeoLoad.class})
	@Valid
	List<DynamicParameter> getParameters();

	class Builder extends ImmutableFramework.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}