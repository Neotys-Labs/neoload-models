package com.neotys.neoload.model.v3.project;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SlaElement {
	String SLA_PROFILE = "sla_profile";
	
	@JsonProperty(SLA_PROFILE)
	Optional<String> getSlaProfile();
}
