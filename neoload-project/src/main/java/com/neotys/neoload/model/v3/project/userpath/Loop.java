package com.neotys.neoload.model.v3.project.userpath;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

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
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Loop.COUNT, Loop.STEPS })
@JsonSerialize(as = ImmutableLoop.class)
@JsonDeserialize(as = ImmutableLoop.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Loop extends Step {

	String DEFAULT_NAME = "loop";
	String STEPS = "steps";
	String COUNT = "count";

	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@Value.Default
	@RequiredCheck(groups = {NeoLoad.class})
	default String getName() {
		return DEFAULT_NAME;
	}


	@JsonProperty(COUNT)
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	String getCount();

	@JsonProperty(STEPS)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonSerialize(using = StepsSerializer.class)
	@JsonDeserialize(using = StepsDeserializer.class)
	List<Step> getSteps();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
	}

	// Excludes the default name from serialization: the property is omitted when the filter's
	// equals(value) returns true.
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

	class Builder extends ImmutableLoop.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
