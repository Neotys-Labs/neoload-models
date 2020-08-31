package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.validation.Valid;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonSerialize(as = ImmutableRendezvousPolicy.class)
@JsonDeserialize(as = ImmutableRendezvousPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface RendezvousPolicy {

	String TIMEOUT = "timeout";
	String NAME = "name";
	String WHEN = "when";

	@JsonProperty(NAME)
	String getName();

	@JsonProperty(WHEN)
	@Value.Default
	@Valid
	default WhenRelease getWhen(){return WhenRelease.builder().type(WhenRelease.Type.PERCENTAGE).value("100%").build();}

	@JsonProperty(TIMEOUT)
	@Value.Default
	default Integer getTimeout() {return 300;}

    class Builder extends ImmutableRendezvousPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
