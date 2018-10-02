package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.serializer.PopulationPolicyDeserializer;
import com.neotys.neoload.model.serializer.PopulationPolicySerializer;

@JsonInclude(value=Include.NON_ABSENT)
@JsonSerialize(using=PopulationPolicySerializer.class)
@JsonDeserialize(using=PopulationPolicyDeserializer.class)
@Value.Immutable
public interface PopulationPolicy {
	String getName();
    LoadPolicy getLoadPolicy();
}
