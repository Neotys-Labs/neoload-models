package com.neotys.neoload.model.v3.project.scenario;

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
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Scenario.SLA_PROFILE, Scenario.POPULATIONS})
@JsonDeserialize(as = ImmutableScenario.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Scenario extends Element {
	String SLA_PROFILE = "sla_profile";
	String POPULATIONS = "populations";

	@JsonProperty(SLA_PROFILE)
	String getSlaProfile();
	@JsonProperty(POPULATIONS)
	@RequiredCheck(groups={NeoLoad.class})
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<PopulationPolicy> getPopulations();
	
	class Builder extends ImmutableScenario.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
