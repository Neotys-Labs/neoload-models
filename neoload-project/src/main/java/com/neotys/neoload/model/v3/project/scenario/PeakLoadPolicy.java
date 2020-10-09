package com.neotys.neoload.model.v3.project.scenario;

import javax.validation.Valid;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({PeakLoadPolicy.USERS, PeakLoadPolicy.DURATION})
@JsonSerialize(as = ImmutablePeakLoadPolicy.class)
@JsonDeserialize(as = ImmutablePeakLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Gson.TypeAdapters
public interface PeakLoadPolicy {
	String USERS = "users";
	String DURATION = "duration";
	
	@PositiveCheck(unit="user", groups={NeoLoad.class})
	@JsonProperty(USERS)
	int getUsers();
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonProperty(DURATION)
	LoadDuration getDuration();
	
	class Builder extends ImmutablePeakLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
