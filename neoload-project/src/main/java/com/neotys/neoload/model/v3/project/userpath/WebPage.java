package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.StepsSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.WebPageStepsCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, SlaElement.SLA_PROFILE, WebPage.THINK_TIME, WebPage.PLAYBACK, WebPage.EXECUTE_RESOURCES, WebPage.STEPS})
@JsonSerialize(as = ImmutableWebPage.class)
@JsonDeserialize(as = ImmutableWebPage.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface WebPage extends Step, SlaElement {
	String DEFAULT_NAME = "web_page";
	String THINK_TIME = "think_time";
	String PLAYBACK = "playback";
	String EXECUTE_RESOURCES = "execute_resources";
	String STEPS = "steps";
	Playback DEFAULT_PLAYBACK = Playback.PARALLEL;
	ExecuteResources DEFAULT_EXECUTE_RESOURCES = ExecuteResources.STATIC;

	@JsonProperty(NAME)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(THINK_TIME)
	@Valid
	Optional<WebPageThinkTime> getThinkTime();

	@JsonProperty(PLAYBACK)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultPlaybackFilter.class)
	@Value.Default
	default Playback getPlayback() {
		return DEFAULT_PLAYBACK;
	}

	@JsonProperty(EXECUTE_RESOURCES)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultExecuteResourcesFilter.class)
	@Value.Default
	default ExecuteResources getExecuteResources() {
		return DEFAULT_EXECUTE_RESOURCES;
	}

	@RequiredCheck(groups={NeoLoad.class})
	@WebPageStepsCheck(groups={NeoLoad.class})
	@Valid
	@JsonSerialize(using = StepsSerializer.class)
	@JsonDeserialize(using = StepsDeserializer.class)
	@JsonProperty(STEPS)
	List<Step> getSteps();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
	}

	// Jackson value filters excluding the default name / playback / execute_resources from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	class DefaultPlaybackFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_PLAYBACK.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_PLAYBACK.hashCode();
		}
	}

	class DefaultExecuteResourcesFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_EXECUTE_RESOURCES.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_EXECUTE_RESOURCES.hashCode();
		}
	}

	class Builder extends ImmutableWebPage.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
