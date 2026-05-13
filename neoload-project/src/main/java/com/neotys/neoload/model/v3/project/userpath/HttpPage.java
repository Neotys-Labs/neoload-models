package com.neotys.neoload.model.v3.project.userpath;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.StepsSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, HttpPage.THINK_TIME, HttpPage.THINK_TIME_RANGE, HttpPage.THINK_TIME_MODE, HttpPage.SCREENSHOT, HttpPage.DYNAMIC_ACTION, HttpPage.FORCE_ENCODING_FOR_DYNAMIC_RESOURCES, HttpPage.STEPS})
@JsonSerialize(as = ImmutableHttpPage.class)
@JsonDeserialize(as = ImmutableHttpPage.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface HttpPage extends Step {
	String DEFAULT_NAME = "http_page";
	String THINK_TIME = "think_time";
	String THINK_TIME_RANGE = "think_time_range";
	String THINK_TIME_MODE = "think_time_mode";
	String SCREENSHOT = "screenshot";
	String DYNAMIC_ACTION = "dynamic_action";
	String FORCE_ENCODING_FOR_DYNAMIC_RESOURCES = "force_encoding_for_dynamic_resources";
	String STEPS = "steps";

	@JsonProperty(NAME)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(THINK_TIME)
	Optional<String> getThinkTime();

	@Valid
	@JsonProperty(THINK_TIME_RANGE)
	Optional<ThinkTimeRange> getThinkTimeRange();

	@JsonProperty(THINK_TIME_MODE)
	Optional<ThinkTimeMode> getThinkTimeMode();

	@JsonProperty(SCREENSHOT)
	Optional<Boolean> getScreenshot();

	@JsonProperty(DYNAMIC_ACTION)
	Optional<Boolean> getDynamicAction();

	@JsonProperty(FORCE_ENCODING_FOR_DYNAMIC_RESOURCES)
	Optional<Boolean> getForceEncodingForDynamicResources();

	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	@JsonSerialize(using = StepsSerializer.class)
	@JsonDeserialize(using = StepsDeserializer.class)
	@JsonProperty(STEPS)
	List<Step> getSteps();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
	}

	class Builder extends ImmutableHttpPage.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}