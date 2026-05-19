package com.neotys.neoload.model.v3.project.userpath.assertion;

import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({SizeAssertion.NAME, SizeAssertion.OPERATOR, SizeAssertion.VALUE})
@JsonSerialize(as = ImmutableSizeAssertion.class)
@JsonDeserialize(as = ImmutableSizeAssertion.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface SizeAssertion extends Assertion {
	String NAME = "name";
	String OPERATOR = "operator";
	String VALUE = "value";

	@JsonProperty(NAME)
	Optional<String> getName();

	@JsonProperty(OPERATOR)
	@RequiredCheck(groups = {NeoLoad.class})
	SizeOperator getOperator();

	@JsonProperty(VALUE)
	@RequiredCheck(groups = {NeoLoad.class})
	long getValue();

	class Builder extends ImmutableSizeAssertion.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}