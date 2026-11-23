package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.JavaDurationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.JavaDurationSerializer;
import com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
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
	Duration DEFAULT_TIME_TO_LIVE = Duration.ofSeconds(300);
	boolean DEFAULT_MILLISECONDS = true;

	@JsonProperty(ID)
	Optional<String> getId();

	@JsonProperty(TIME_TO_LIVE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTimeToLiveFilter.class)
	@WholeSecondsCheck(groups = {NeoLoad.class})
	@JsonSerialize(using = JavaDurationSerializer.class)
	@JsonDeserialize(using = JavaDurationDeserializer.class)
	@Value.Default
	default Duration getTimeToLive() {
		return DEFAULT_TIME_TO_LIVE;
	}

	@JsonProperty(MILLISECONDS)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMillisecondsFilter.class)
	@Value.Default
	default boolean getMilliseconds() {
		return DEFAULT_MILLISECONDS;
	}

	/**
	 * Jackson value filter omitting {@code time_to_live} from serialization when it equals the default, the value being the raw {@link Duration}.
	 */
	class DefaultTimeToLiveFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_TIME_TO_LIVE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_TIME_TO_LIVE.hashCode(); }
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
