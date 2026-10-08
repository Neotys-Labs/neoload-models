package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueWebSocketMessagesMappingPathCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * Tells a {@link WebSocketChannel} how to extract the correlation id from an inbound frame. The
 * value it extracts is compared against the {@code mapping_id} of a synchronous
 * {@code websocket_request} to pair a response with the request waiting for it.
 *
 * <p>A channel carries at most one mapping, and it becomes mandatory as soon as one synchronous
 * request uses the channel.</p>
 */
@UniqueWebSocketMessagesMappingPathCheck(groups={NeoLoad.class})
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({WebSocketMessagesMapping.XPATH, WebSocketMessagesMapping.JSON_PATH, WebSocketMessagesMapping.REGEXP, WebSocketMessagesMapping.TEMPLATE, WebSocketMessagesMapping.DECODE, WebSocketMessagesMapping.CUSTOM_DECODER, WebSocketMessagesMapping.ENCODING})
@JsonSerialize(as = ImmutableWebSocketMessagesMapping.class)
@JsonDeserialize(as = ImmutableWebSocketMessagesMapping.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 (equals should check the class of its parameter) is suppressed: the nested classes below
// are Jackson @JsonInclude value filters, whose equals(value) is called by Jackson with the
// property value (a String), not with another filter instance. Checking the filter's own class
// would make equals always return false and defeat the default-value omission.
@SuppressWarnings("java:S2097")
public interface WebSocketMessagesMapping {
	String XPATH = "xpath";
	String JSON_PATH = "jsonpath";
	String REGEXP = "regexp";
	String TEMPLATE = "template";
	String DECODE = "decode";
	String CUSTOM_DECODER = "custom_decoder";
	String ENCODING = "encoding";

	String HTML_DECODE_VALUE = "html";
	String URL_DECODE_VALUE = "url";
	String CUSTOM_DECODE_VALUE = "custom";

	String DEFAULT_REGEXP_VALUE = VariableExtractor.DEFAULT_REGEXP_VALUE;
	String DEFAULT_TEMPLATE_VALUE = VariableExtractor.DEFAULT_TEMPLATE_VALUE;

	enum Decode {
		@JsonProperty(WebSocketMessagesMapping.HTML_DECODE_VALUE)
		HTML,
		@JsonProperty(WebSocketMessagesMapping.URL_DECODE_VALUE)
		URL,
		@JsonProperty(WebSocketMessagesMapping.CUSTOM_DECODE_VALUE)
		CUSTOM
	}

	@JsonProperty(XPATH)
	Optional<String> getXpath();

	@JsonProperty(JSON_PATH)
	Optional<String> getJsonPath();

	@JsonProperty(REGEXP)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultRegexpFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getRegexp() { return DEFAULT_REGEXP_VALUE; }

	@JsonProperty(TEMPLATE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTemplateFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getTemplate() { return DEFAULT_TEMPLATE_VALUE; }

	@JsonProperty(DECODE)
	@Value.Default
	default Optional<Decode> getDecode() { return Optional.empty(); }

	/**
	 * Class name of the decoder to apply, used only when {@link #getDecode()} is
	 * {@link Decode#CUSTOM}.
	 */
	@JsonProperty(CUSTOM_DECODER)
	Optional<String> getCustomDecoder();

	@JsonProperty(ENCODING)
	Optional<String> getEncoding();

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	// (getDecode() is an Optional and is already suppressed when empty by @JsonInclude(NON_EMPTY).)
	class DefaultRegexpFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_REGEXP_VALUE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_REGEXP_VALUE.hashCode(); }
	}

	class DefaultTemplateFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_TEMPLATE_VALUE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_TEMPLATE_VALUE.hashCode(); }
	}

	class Builder extends ImmutableWebSocketMessagesMapping.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
