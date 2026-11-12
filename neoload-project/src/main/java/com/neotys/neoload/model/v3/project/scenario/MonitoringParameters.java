package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.JavaDurationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.JavaDurationSerializer;
import com.neotys.neoload.model.v3.validation.constraints.MaxSecondsCheck;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
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

	@JsonProperty(BEFORE_FIRST)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@WholeSecondsCheck(groups={NeoLoad.class})
	@MaxSecondsCheck(groups={NeoLoad.class})
	@JsonSerialize(using=JavaDurationSerializer.class)
	@JsonDeserialize(using=JavaDurationDeserializer.class)
	Duration getBeforeFirstVu();


	@JsonProperty(AFTER_LAST)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@WholeSecondsCheck(groups={NeoLoad.class})
	@MaxSecondsCheck(groups={NeoLoad.class})
	@JsonSerialize(using=JavaDurationSerializer.class)
	@JsonDeserialize(using=JavaDurationDeserializer.class)
	Duration getAfterLastVus();


	static ImmutableMonitoringParameters.Builder builder() {
		return ImmutableMonitoringParameters.builder();
	}
}
