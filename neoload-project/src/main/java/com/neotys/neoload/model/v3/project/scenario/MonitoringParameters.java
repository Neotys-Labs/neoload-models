package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.Durations;
import com.neotys.neoload.model.v3.binding.converter.TimeDurationToStringConverter;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.SecondsDurationCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({MonitoringParameters.BEFORE_FIRST, MonitoringParameters.AFTER_LAST})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@JsonSerialize(as = ImmutableMonitoringParameters.class)
@JsonDeserialize(as = ImmutableMonitoringParameters.class)
@Gson.TypeAdapters
public interface MonitoringParameters {

	String BEFORE_FIRST = "before";
	String AFTER_LAST = "after";

	@JsonProperty(value = BEFORE_FIRST, access = JsonProperty.Access.READ_ONLY)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter= TimeDurationToStringConverter.class)
	@Value.Default
	default Integer getBeforeFirstVu() {
		return getBeforeFirstVuText().flatMap(Durations::tryParseSeconds).orElse(null);
	}

	@JsonProperty(value = BEFORE_FIRST, access = JsonProperty.Access.WRITE_ONLY)
	@Value.Auxiliary
	Optional<@SecondsDurationCheck(groups={NeoLoad.class}) String> getBeforeFirstVuText();

	@JsonProperty(value = AFTER_LAST, access = JsonProperty.Access.READ_ONLY)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter= TimeDurationToStringConverter.class)
	@Value.Default
	default Integer getAfterLastVus() {
		return getAfterLastVusText().flatMap(Durations::tryParseSeconds).orElse(null);
	}

	@JsonProperty(value = AFTER_LAST, access = JsonProperty.Access.WRITE_ONLY)
	@Value.Auxiliary
	Optional<@SecondsDurationCheck(groups={NeoLoad.class}) String> getAfterLastVusText();

	static ImmutableMonitoringParameters.Builder builder() {
		return ImmutableMonitoringParameters.builder();
	}
}
