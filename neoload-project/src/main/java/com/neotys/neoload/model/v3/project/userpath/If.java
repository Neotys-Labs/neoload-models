package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.MatchDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, If.CONDITIONS, Match.MATCH, If.THEN, If.ELSE})
@JsonDeserialize(as = ImmutableIf.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface If extends Step {

	String DEFAULT_NAME = "if";
	String CONDITIONS = "conditions";
	String THEN = "then";
	String ELSE = "else";

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CONDITIONS)
	@Valid
	List<Condition> getConditions();

	@JsonProperty(Match.MATCH)
	@RequiredCheck(groups={NeoLoad.class})
	@JsonDeserialize(using = MatchDeserializer.class)
	@Value.Default
	default Match getMatch() { return Match.ANY; }

	@RequiredCheck(groups={NeoLoad.class})
	@JsonProperty(THEN)
	@Valid
	Container getThen();

	@Valid
	@JsonProperty(ELSE)
	Optional<Container> getElse();

	@Override
	default Stream<Element> flattened() {
		if(getElse().isPresent()){
			return Stream.concat(Stream.of(this), Stream.concat(getThen().flattened(), getElse().get().flattened()));
		} else {
			return Stream.concat(Stream.of(this), getThen().flattened());
		}

	}

	class Builder extends ImmutableIf.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
