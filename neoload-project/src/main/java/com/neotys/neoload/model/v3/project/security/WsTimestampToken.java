package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsTimestampToken.ID, WsTimestampToken.TIME_TO_LIVE, WsTimestampToken.MILLISECONDS})
@JsonSerialize(as = ImmutableWsTimestampToken.class)
@JsonDeserialize(as = ImmutableWsTimestampToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsTimestampToken {
	String ID = "id";
	String TIME_TO_LIVE = "time_to_live";
	String MILLISECONDS = "milliseconds";

	@JsonProperty(ID)
	Optional<String> getId();

	@JsonProperty(TIME_TO_LIVE)
	Optional<Integer> getTimeToLive();

	@JsonProperty(MILLISECONDS)
	Optional<Boolean> getMilliseconds();

	class Builder extends ImmutableWsTimestampToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
