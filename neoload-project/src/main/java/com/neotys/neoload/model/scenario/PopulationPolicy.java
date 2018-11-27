package com.neotys.neoload.model.scenario;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.serializer.PopulationPolicyDeserializer;
import com.neotys.neoload.model.serializer.PopulationPolicySerializer;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonSerialize(using=PopulationPolicySerializer.class)
@JsonDeserialize(using=PopulationPolicyDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface PopulationPolicy extends Element {
	public static final String LOAD_POLICY = "load_policy";
	public static final String CONSTANT_LOAD = "constant_load";
	public static final String RAMPUP_LOAD = "rampup_load";
	public static final String PEAKS_LOAD = "peaks_load";

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
    LoadPolicy getLoadPolicy();
	
	class Builder extends ImmutablePopulationPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
