package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@MinNotGreaterThanMaxCheck(element = "think_time", groups = {NeoLoad.class})
public interface WebPageThinkTimeRandom extends WebPageThinkTime, RandomDuration {
	String MIN = "min";
	String MAX = "max";
	String DEFAULT_MIN = "0";

	@JsonProperty(MIN)
	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = VALUE_PATTERN, groups = {NeoLoad.class})
	@Value.Default
	default String getMin() {
		return DEFAULT_MIN;
	}

	@JsonProperty(MAX)
	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = VALUE_PATTERN, groups = {NeoLoad.class})
	String getMax();

	@Override
	default TimeUnit plainNumberUnit() {
		return TimeUnit.SECONDS;
	}

	class Builder extends ImmutableWebPageThinkTimeRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
