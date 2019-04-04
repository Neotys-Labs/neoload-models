package com.neotys.neoload.model.v3.project.scenario;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.PopulationPolicyDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(using=PopulationPolicyDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface PopulationPolicy extends Element {
	String LOAD_POLICY = "load_policy";
	String CONSTANT_LOAD = "constant_load";
	String RAMPUP_LOAD = "rampup_load";
	String PEAKS_LOAD = "peaks_load";

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
    LoadPolicy getLoadPolicy();
	
	class Builder extends ImmutablePopulationPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
