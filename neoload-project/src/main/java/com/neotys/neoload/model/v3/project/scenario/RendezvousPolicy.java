package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.v3.binding.converter.TimeDurationToStringConverter;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import javax.validation.Valid;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonSerialize(as = ImmutableRendezvousPolicy.class)
@JsonDeserialize(as = ImmutableRendezvousPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@Gson.TypeAdapters
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface RendezvousPolicy {

	String TIMEOUT = "timeout";
	String NAME = "name";
	String WHEN = "when";

	@JsonProperty(NAME)
	@RequiredCheck(groups={NeoLoad.class})
	String getName();

	@JsonProperty(WHEN)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultWhenFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	@Valid
	default WhenRelease getWhen(){return WhenRelease.builder().type(WhenRelease.Type.PERCENTAGE).value(100).build();}

	@JsonProperty(TIMEOUT)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultTimeoutFilter.class)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter= TimeDurationToStringConverter.class)
	@JsonDeserialize(converter= StringToTimeDurationConverter.class)
	@Value.Default
	default Integer getTimeout() {return 300;}

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true. The value received is the
	// raw property value (WhenRelease / Integer), before any @JsonSerialize converter is applied.
	class DefaultWhenFilter {
		private static final WhenRelease DEFAULT_WHEN = WhenRelease.builder().type(WhenRelease.Type.PERCENTAGE).value(100).build();
		@Override
		public boolean equals(final Object value) { return DEFAULT_WHEN.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_WHEN.hashCode(); }
	}

	class DefaultTimeoutFilter {
		@Override
		public boolean equals(final Object value) { return Integer.valueOf(300).equals(value); }
		@Override
		public int hashCode() { return Integer.valueOf(300).hashCode(); }
	}

    class Builder extends ImmutableRendezvousPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
