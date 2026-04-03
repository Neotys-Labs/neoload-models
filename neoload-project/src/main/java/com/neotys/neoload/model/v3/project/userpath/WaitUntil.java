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

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, WaitUntil.CONDITIONS, Match.MATCH, WaitUntil.TIMEOUT})
@JsonDeserialize(as = ImmutableWaitUntil.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WaitUntil extends Step {

	String DEFAULT_NAME = "wait_until";
	String DEFAULT_TIMEOUT = "60000";
	String CONDITIONS = "conditions";
	String TIMEOUT = "timeout";

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CONDITIONS)
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	List<Condition> getConditions();

	@JsonProperty(Match.MATCH)
	@JsonDeserialize(using = MatchDeserializer.class)
	@Value.Default
	default Match getMatch() {
		return Match.ANY;
	}

	@JsonProperty(TIMEOUT)
	@Value.Default
	default String getTimeout() {
		return DEFAULT_TIMEOUT;
	}

	class Builder extends ImmutableWaitUntil.Builder {}
	static Builder builder() {
		return new Builder();
	}
}