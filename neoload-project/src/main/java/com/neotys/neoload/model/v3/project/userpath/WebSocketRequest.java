package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketRequestCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * A frame sent on a {@link WebSocketChannel}, or the closing of that channel.
 *
 * <p>{@link #getChannel()} is the complete path of the channel, from where it is declared down to
 * its name: {@code actions>Login>chat_socket} for a channel declared in a {@link UserPath},
 * {@code shared_elements>OpenChat>chat_socket} for one declared inside a shared element. A request
 * can only use a channel opened by the user path running it.</p>
 *
 * <p>Only a synchronous request waits for a response: it is paired with the inbound frame whose
 * correlation id, extracted by the channel's messages mapping, equals {@link #getMappingId()}.
 * This is why {@link #getMappingId()}, {@link #getExtractors()} and {@link #getAssertions()} only
 * apply to a synchronous request, and {@link #getStatusCode()} only to a {@link MessageType#CLOSE}
 * one.</p>
 *
 * <p>{@link MessageType#BINARY} is not offered by the GUI, which only proposes text and close:
 * NeoLoad creates it when it records a non-text frame.</p>
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, WebSocketRequest.CHANNEL, WebSocketRequest.MESSAGE_TYPE, WebSocketRequest.SYNCHRONOUS, WebSocketRequest.MAPPING_ID, WebSocketRequest.STATUS_CODE, WebSocketRequest.BODY, WebSocketRequest.BODYBINARY, WebSocketRequest.EXTRACTORS, AssertionsElement.ASSERTIONS})
@JsonSerialize(as = ImmutableWebSocketRequest.class)
@JsonDeserialize(as = ImmutableWebSocketRequest.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@WebSocketRequestCheck(groups={NeoLoad.class})
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface WebSocketRequest extends Step, AssertionsElement {
	String CHANNEL = "channel";
	String MESSAGE_TYPE = "message_type";
	String SYNCHRONOUS = "synchronous";
	String MAPPING_ID = "mapping_id";
	String STATUS_CODE = "status_code";
	String BODY = "body";
	String BODYBINARY = "bodybinary";
	String EXTRACTORS = "extractors";

	String DEFAULT_NAME = "websocket_request";
	MessageType DEFAULT_MESSAGE_TYPE = MessageType.TEXT;
	/** The normal-closure code of RFC 6455 §7.4.1, which is also NeoLoad's default. */
	String DEFAULT_STATUS_CODE = "1000";

	enum MessageType {
		@JsonProperty("text")
		TEXT,
		@JsonProperty("binary")
		BINARY,
		@JsonProperty("close")
		CLOSE
	}

	@JsonProperty(NAME)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CHANNEL)
	Optional<String> getChannel();

	@JsonProperty(MESSAGE_TYPE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMessageTypeFilter.class)
	@Value.Default
	default MessageType getMessageType() {
		return DEFAULT_MESSAGE_TYPE;
	}

	@JsonProperty(SYNCHRONOUS)
	@JsonInclude(value = Include.NON_DEFAULT)
	@Value.Default
	default boolean isSynchronous() {
		return false;
	}

	@JsonProperty(MAPPING_ID)
	Optional<String> getMappingId();

	/** A {@code String} rather than an {@code int} so that it can hold a {@code ${variable}}. */
	@JsonProperty(STATUS_CODE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultStatusCodeFilter.class)
	@Value.Default
	default String getStatusCode() {
		return DEFAULT_STATUS_CODE;
	}

	@JsonProperty(BODY)
	Optional<String> getBody();

	@JsonProperty(BODYBINARY)
	Optional<byte[]> getBodyBinary();

	@JsonProperty(EXTRACTORS)
	@Valid
	List<VariableExtractor> getExtractors();

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	class DefaultMessageTypeFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_MESSAGE_TYPE.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_MESSAGE_TYPE.hashCode();
		}
	}

	class DefaultStatusCodeFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_STATUS_CODE.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_STATUS_CODE.hashCode();
		}
	}

	class Builder extends ImmutableWebSocketRequest.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
