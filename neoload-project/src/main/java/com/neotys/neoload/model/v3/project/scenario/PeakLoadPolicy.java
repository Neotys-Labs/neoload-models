package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

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
	
	@JsonProperty(USERS)
	Optional<@PositiveCheck(unit="user", groups={NeoLoad.class}) Integer> getUsers();
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonProperty(DURATION)
	LoadDuration getDuration();
	
	class Builder extends ImmutablePeakLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
