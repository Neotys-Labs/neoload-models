package com.neotys.neoload.model.v3.project.scenario;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, SlaElement.SLA_PROFILE, Scenario.POPULATIONS, Scenario.EXCLUDED_URLS})
@JsonSerialize(as = ImmutableScenario.class)
@JsonDeserialize(as = ImmutableScenario.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Scenario extends Element, SlaElement {

	String POPULATIONS = "populations";
	String APM = "apm_configuration";
	String EXCLUDED_URLS = "excluded_urls";
	String RENDEZVOUS_POLICIES = "rendezvous_policies";
	String STORE_VARIABLES = "store_variables_for_raw_data";
	String MONITORING = "monitoring";

	@JsonProperty(POPULATIONS)
	@RequiredCheck(groups = {NeoLoad.class})
	@UniqueElementNameCheck(groups = {NeoLoad.class})
	@Valid
	List<PopulationPolicy> getPopulations();

	@JsonProperty(APM)
	@Valid
	Optional<Apm> getApm();
	
	@JsonProperty(EXCLUDED_URLS)
	@Valid
	List<String> getExcludedUrls();

	@RequiredCheck(groups = {NeoLoad.class})
	@JsonProperty(RENDEZVOUS_POLICIES)
	@Valid
	List<RendezvousPolicy> getRendezvousPolicies();

	@JsonProperty(STORE_VARIABLES)
	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default boolean isStoredVariables() {
		return false;
	}

	@JsonProperty(MONITORING)
	@RequiredCheck(groups = {NeoLoad.class})
	Optional<MonitoringParameters> getMonitoringParameters();

	class Builder extends ImmutableScenario.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
