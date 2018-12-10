package com.neotys.neoload.model.scenario;

import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Scenario.SLA_PROFILE, Scenario.POPULATIONS})
@JsonDeserialize(as = ImmutableScenario.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
public interface Scenario extends Element {
	public static final String SLA_PROFILE = "sla_profile";
	public static final String POPULATIONS = "populations";

	@JsonProperty(SLA_PROFILE)
	String getSlaProfile();
	@RequiredCheck(groups={NeoLoad.class})
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<PopulationPolicy> getPopulations();
	
	class Builder extends ImmutableScenario.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
