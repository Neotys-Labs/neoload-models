package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ElementsDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Stream;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, Loop.COUNT, Loop.STEPS })
@JsonDeserialize(as = ImmutableLoop.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Loop extends Step {

	String DEFAULT_NAME = "loop";
	String STEPS = "steps";
	String COUNT = "count";

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
	@JsonDeserialize(using = ElementsDeserializer.class)
	List<Step> getSteps();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
	}

	class Builder extends ImmutableLoop.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
