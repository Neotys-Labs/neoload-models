package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * Tells a {@link WebSocketChannel} where to find, in each inbound frame, the time the server sent it at.
 *
 * <p>The value this extracts, a number of milliseconds since the epoch, is used as the start time of the
 * push message that handles the frame, so that its response time is measured from the moment the server sent
 * it. Without a timestamp extractor, or when nothing is extracted, the push message is measured from the
 * moment the frame is received, which gives a time of zero.</p>
 *
 * <p>The clocks of the server and of the load generator have to be synchronized: a frame that appears to have
 * been sent in the future gets a time of zero.</p>
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({WebSocketTimestampExtractor.REGEXP, WebSocketTimestampExtractor.TEMPLATE})
@JsonSerialize(as = ImmutableWebSocketTimestampExtractor.class)
@JsonDeserialize(as = ImmutableWebSocketTimestampExtractor.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 (equals should check the class of its parameter) is suppressed: the nested class below is a
// Jackson @JsonInclude value filter, whose equals(value) is called by Jackson with the property value
// (a String), not with another filter instance. Checking the filter's own class would make equals always
// return false and defeat the default-value omission.
@SuppressWarnings("java:S2097")
public interface WebSocketTimestampExtractor {
	String REGEXP = "regexp";
	String TEMPLATE = "template";

	String DEFAULT_TEMPLATE_VALUE = VariableExtractor.DEFAULT_TEMPLATE_VALUE;

	/** Regular expression applied to the content of the frame. */
	@JsonProperty(REGEXP)
	@RequiredCheck(groups={NeoLoad.class})
	String getRegexp();

	/** Template applied to the extraction result, {@code $1$} when absent. */
	@JsonProperty(TEMPLATE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTemplateFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getTemplate() { return DEFAULT_TEMPLATE_VALUE; }

	// Jackson value filter excluding the default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultTemplateFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_TEMPLATE_VALUE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_TEMPLATE_VALUE.hashCode(); }
	}

	class Builder extends ImmutableWebSocketTimestampExtractor.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
