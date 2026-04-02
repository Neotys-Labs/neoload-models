package com.neotys.neoload.model.v3.project.userpath;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Fork.COPY_VARIABLES, Fork.STEPS})
@JsonDeserialize(as = ImmutableFork.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Fork extends Step {

	String DEFAULT_NAME = "fork";
	String COPY_VARIABLES = "copy_variables";
	String STEPS = "steps";

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(COPY_VARIABLES)
	@Value.Default
	default boolean getCopyVariables() {
		return true;
	}

	@JsonProperty(STEPS)
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	@JsonDeserialize(using = StepsDeserializer.class)
	List<Step> getSteps();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
	}

	class Builder extends ImmutableFork.Builder {}
	static Builder builder() {
		return new Builder();
	}
}