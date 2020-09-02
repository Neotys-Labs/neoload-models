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
import com.neotys.neoload.model.v3.validation.constraints.RequiredContentAssertionCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionPathCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@RequiredContentAssertionCheck(groups={NeoLoad.class})
@UniqueContentAssertionPathCheck(groups={NeoLoad.class})
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({ContentAssertion.NAME, ContentAssertion.XPATH, ContentAssertion.JSON_PATH, ContentAssertion.NOT, ContentAssertion.CONTAINS, ContentAssertion.REGEXP})
@JsonSerialize(as = ImmutableContentAssertion.class)
@JsonDeserialize(as = ImmutableContentAssertion.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ContentAssertion extends Assertion {
	String NAME = "name";
	String XPATH = "xpath";
	String JSON_PATH = "jsonpath";
	String NOT = "not";
	String CONTAINS = "contains";
	String REGEXP = "regexp";
		
	@JsonProperty(NAME)
	Optional<String> getName();
	
	@JsonProperty(XPATH)
	Optional<String> getXPath();

	@JsonProperty(JSON_PATH)
	Optional<String> getJsonPath();

	@JsonProperty(NOT)
	@JsonInclude(value=Include.NON_DEFAULT)
	@Value.Default
	default boolean getNot() { return false; }
	
	@JsonProperty(CONTAINS)
	Optional<String> getContains();
	
	@JsonProperty(REGEXP)
	@JsonInclude(value=Include.NON_DEFAULT)
	@Value.Default
	default boolean getRegexp() { return false; }

	class Builder extends ImmutableContentAssertion.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
