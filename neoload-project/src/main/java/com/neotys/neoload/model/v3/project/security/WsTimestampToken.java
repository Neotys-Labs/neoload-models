package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsTimestampToken.ID, WsTimestampToken.TIME_TO_LIVE, WsTimestampToken.MILLISECONDS})
@JsonDeserialize(as = ImmutableWsTimestampToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsTimestampToken extends WsToken {
	String ID = "id";
	String TIME_TO_LIVE = "time_to_live";
	String MILLISECONDS = "milliseconds";
	int DEFAULT_TIME_TO_LIVE = 300;
	boolean DEFAULT_MILLISECONDS = true;

	@JsonProperty(ID)
	Optional<String> getId();

	@JsonProperty(TIME_TO_LIVE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTimeToLiveFilter.class)
	@Value.Default
	default int getTimeToLive() {
		return DEFAULT_TIME_TO_LIVE;
	}

	@JsonProperty(MILLISECONDS)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMillisecondsFilter.class)
	@Value.Default
	default boolean getMilliseconds() {
		return DEFAULT_MILLISECONDS;
	}

	/**
	 * Jackson value filter omitting {@code time_to_live} from serialization when it equals the default.
	 */
	class DefaultTimeToLiveFilter {
		@Override
		public boolean equals(final Object value) { return Integer.valueOf(DEFAULT_TIME_TO_LIVE).equals(value); }
		@Override
		public int hashCode() { return Integer.hashCode(DEFAULT_TIME_TO_LIVE); }
	}

	/**
	 * Jackson value filter omitting {@code milliseconds} from serialization when it equals the default.
	 */
	class DefaultMillisecondsFilter {
		@Override
		public boolean equals(final Object value) { return Boolean.valueOf(DEFAULT_MILLISECONDS).equals(value); }
		@Override
		public int hashCode() { return Boolean.hashCode(DEFAULT_MILLISECONDS); }
	}

	class Builder extends ImmutableWsTimestampToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
