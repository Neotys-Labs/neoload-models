package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueVariableExtractorPathCheck;
import com.neotys.neoload.model.v3.validation.constraints.VariableExtractorFromPathCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.constraints.Min;
import java.util.Optional;

@UniqueVariableExtractorPathCheck(groups={NeoLoad.class})
@VariableExtractorFromPathCheck(groups={NeoLoad.class})
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, VariableExtractor.FROM, VariableExtractor.XPATH, VariableExtractor.JSON_PATH, VariableExtractor.REGEXP, VariableExtractor.MATCH_NUMBER, VariableExtractor.TEMPLATE, VariableExtractor.DECODE, VariableExtractor.EXTRACT_ONCE, VariableExtractor.DEFAULT, VariableExtractor.THROW_ASSERTION_ERROR})
@JsonSerialize(as = ImmutableVariableExtractor.class)
@JsonDeserialize(as = ImmutableVariableExtractor.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 (equals should check the class of its parameter) is suppressed: the nested classes below
// are Jackson @JsonInclude value filters, whose equals(value) is called by Jackson with the
// property value (a From/String/Integer/Boolean...), not with another filter instance. Checking the
// filter's own class would make equals always return false and defeat the default-value omission.
@SuppressWarnings("java:S2097")
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

	String DEFAULT_REGEXP_VALUE = "(.*)";
	String DEFAULT_TEMPLATE_VALUE = "$1$";
	String DEFAULT_DEFAULT_VALUE = "<NOT FOUND>";

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
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultFromFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default From getFrom() { return From.BODY; }

	@JsonProperty(XPATH)
	Optional<String> getXpath();

	@JsonProperty(JSON_PATH)
	Optional<String> getJsonPath();

	@JsonProperty(REGEXP)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultRegexpFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getRegexp() { return DEFAULT_REGEXP_VALUE;}

	@JsonProperty(MATCH_NUMBER)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMatchNumberFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Min(value=-1, groups={NeoLoad.class})
	@Value.Default
	default int getMatchNumber() { return 1; }

	@JsonProperty(TEMPLATE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTemplateFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getTemplate() { return DEFAULT_TEMPLATE_VALUE;}

	@JsonProperty(DECODE)
	@Value.Default
	default Optional<Decode> getDecode() { return Optional.empty();}

	@JsonProperty(EXTRACT_ONCE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultExtractOnceFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default boolean getExtractOnce() { return false;}

	@JsonProperty(DEFAULT)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultDefaultValueFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getDefault() { return DEFAULT_DEFAULT_VALUE;}

	@JsonProperty(THROW_ASSERTION_ERROR)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultThrowAssertionErrorFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default boolean getThrowAssertionError() { return true;}

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	// (getDecode() is an Optional and is already suppressed when empty by @JsonInclude(NON_EMPTY).)
	class DefaultFromFilter {
		@Override
		public boolean equals(final Object value) { return From.BODY.equals(value); }
		@Override
		public int hashCode() { return From.BODY.hashCode(); }
	}

	class DefaultRegexpFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_REGEXP_VALUE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_REGEXP_VALUE.hashCode(); }
	}

	class DefaultMatchNumberFilter {
		@Override
		public boolean equals(final Object value) { return Integer.valueOf(1).equals(value); }
		@Override
		public int hashCode() { return Integer.valueOf(1).hashCode(); }
	}

	class DefaultTemplateFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_TEMPLATE_VALUE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_TEMPLATE_VALUE.hashCode(); }
	}

	class DefaultExtractOnceFilter {
		@Override
		public boolean equals(final Object value) { return Boolean.FALSE.equals(value); }
		@Override
		public int hashCode() { return Boolean.FALSE.hashCode(); }
	}

	class DefaultDefaultValueFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_DEFAULT_VALUE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_DEFAULT_VALUE.hashCode(); }
	}

	class DefaultThrowAssertionErrorFilter {
		@Override
		public boolean equals(final Object value) { return Boolean.TRUE.equals(value); }
		@Override
		public int hashCode() { return Boolean.TRUE.hashCode(); }
	}

	class Builder extends ImmutableVariableExtractor.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
