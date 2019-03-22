package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.binding.serializer.IfDeserializer;
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
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, If.CONDITIONS, If.MATCH, If.THEN, If.ELSE})
@JsonDeserialize(using = IfDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface If extends Step {

	String DEFAULT_NAME = "if";
	String CONDITIONS = "conditions";
	String MATCH = "match";
	String THEN = "then";
	String ELSE = "else";

	enum Match {
		ANY, ALL;

		public static Match of(final String name) {
			if (!Strings.isNullOrEmpty(name)) {
				try {return Match.valueOf(name.toUpperCase());}catch (final IllegalArgumentException iae) {}
			}
			throw new IllegalArgumentException("The Match must be: 'any' or 'all'.");
		}

		public String getName() {
			return name().toLowerCase();
		}
	}

	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CONDITIONS)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	List<Condition> getConditions();

	@JsonProperty(MATCH)
	@RequiredCheck(groups={NeoLoad.class})
	@JsonDeserialize(using = MatchDeserializer.class)
	@Value.Default
	default Match getMatch() { return Match.ANY; }

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	Container getThen();

	@Valid
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
