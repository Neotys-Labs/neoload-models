package com.neotys.neoload.model.scenario;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(as = ImmutablePeakLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
public interface PeakLoadPolicy {
	@PositiveCheck(unit="user", groups={NeoLoad.class})
    int getUsers();
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
    Duration getDuration();
	
	class Builder extends ImmutablePeakLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
