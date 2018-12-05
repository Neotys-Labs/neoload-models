package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueVariableExtractorPathCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.constraints.Min;
import java.util.Optional;

@UniqueVariableExtractorPathCheck(groups={NeoLoad.class})
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, VariableExtractor.FROM, VariableExtractor.XPATH, VariableExtractor.JSON_PATH, VariableExtractor.REGEXP, VariableExtractor.MATCH_NUMBER, VariableExtractor.TEMPLATE, VariableExtractor.DECODE, VariableExtractor.EXTRACT_ONCE, VariableExtractor.DEFAULT, VariableExtractor.THROW_ASSERTION_ERROR})
@JsonDeserialize(as = ImmutableVariableExtractor.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface VariableExtractor extends Element {
	String FROM = "from";
	String XPATH = "xpath";
	String JSON_PATH = "jsonpath";
	String REGEXP = "regexp";
	String MATCH_NUMBER = "match_number";
	String TEMPLATE = "template";
	String DECODE = "decode";
	String EXTRACT_ONCE = "extract_once";
	String DEFAULT = "default";
	String THROW_ASSERTION_ERROR = "throw_assertion_error";

	String HEADER_FROM_VALUE = "header";
	String BODY_FROM_VALUE = "body";
	String BOTH_FROM_VALUE = "both";

	String HTML_DECODE_VALUE = "html";
	String URL_DECODE_VALUE = "url";

	enum From {
		@JsonProperty(VariableExtractor.HEADER_FROM_VALUE)
		HEADER,
		@JsonProperty(VariableExtractor.BODY_FROM_VALUE)
		BODY,
		@JsonProperty(VariableExtractor.BOTH_FROM_VALUE)
		BOTH
	}

	enum Decode {
		@JsonProperty(VariableExtractor.HTML_DECODE_VALUE)
		HTML,
		@JsonProperty(VariableExtractor.URL_DECODE_VALUE)
		URL
	}

	@JsonProperty(FROM)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default From getFrom() { return From.BODY; }

	@JsonProperty(XPATH)
	Optional<String> getXpath();

	@JsonProperty(JSON_PATH)
	Optional<String> getJsonPath();

	@JsonProperty(REGEXP)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getRegexp() { return "(.*)";}

	@JsonProperty(MATCH_NUMBER)
	@RequiredCheck(groups={NeoLoad.class})
	@Min(value=-1, groups={NeoLoad.class})
	@Value.Default
	default int getMatchNumber() { return 1; }

	@JsonProperty(TEMPLATE)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getTemplate() { return "$1$";}

	@JsonProperty(DECODE)
	@Value.Default
	default Optional<Decode> getDecode() { return Optional.empty();}

	@JsonProperty(EXTRACT_ONCE)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default boolean getExtractOnce() { return false;}

	@JsonProperty(DEFAULT)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getDefault() { return "<NOT FOUND>";}

	@JsonProperty(THROW_ASSERTION_ERROR)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default boolean getThrowAssertionError() { return true;}

	class Builder extends ImmutableVariableExtractor.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
