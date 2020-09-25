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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.StepsSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, SlaElement.SLA_PROFILE, Container.STEPS, AssertionsElement.ASSERTIONS})
@JsonSerialize(as = ImmutableContainer.class)
@JsonDeserialize(as = ImmutableContainer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Container extends Step, SlaElement, AssertionsElement {
	String DEFAULT_NAME = "container";
	String STEPS = "steps";

	@JsonProperty(NAME)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonSerialize(using = StepsSerializer.class)
	@JsonDeserialize(using = StepsDeserializer.class)
	@JsonProperty(STEPS)
	List<Step> getSteps();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
	}

	class Builder extends ImmutableContainer.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
