package com.neotys.neoload.model.v3.project.scenario;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(as = ImmutablePeakLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface PeakLoadPolicy {
	@PositiveCheck(unit="user", groups={NeoLoad.class})
    int getUsers();
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
    LoadDuration getDuration();
	
	class Builder extends ImmutablePeakLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
