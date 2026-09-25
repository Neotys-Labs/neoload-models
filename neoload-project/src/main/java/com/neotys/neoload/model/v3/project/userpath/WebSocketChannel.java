package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * A WebSocket connection, opened through an HTTP upgrade and kept open for the rest of the
 * iteration. Apart from {@link #getMessagesMapping()} and {@link #getPushMessages()} it is defined
 * exactly like a {@link Request}: url, server, method, headers and extractors.
 *
 * <p>{@link #getId()} is the handle a {@link WebSocketRequest} points at through its
 * {@code channel} property; it must be unique within a {@link UserPath}.</p>
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, WebSocketChannel.ID, Element.DESCRIPTION, WebSocketChannel.URL, WebSocketChannel.SERVER, WebSocketChannel.HEADERS, WebSocketChannel.EXTRACTORS, WebSocketChannel.MESSAGES_MAPPING, WebSocketChannel.PUSH_MESSAGES})
@JsonSerialize(as = ImmutableWebSocketChannel.class)
@JsonDeserialize(as = ImmutableWebSocketChannel.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WebSocketChannel extends Step {
	String ID = "id";
	String URL = "url";
	String SERVER = "server";
	String HEADERS = "headers";
	String EXTRACTORS = "extractors";
	String MESSAGES_MAPPING = "messages_mapping";
	String PUSH_MESSAGES = "push_messages";

	/**
	 * Identifiers stay free of variables and whitespace so that a {@code channel} reference can be
	 * compared literally and written unquoted in YAML.
	 */
	String ID_PATTERN = "^[A-Za-z0-9][A-Za-z0-9._-]*$";

	/**
	 * The {@link Request#URL} pattern widened to the WebSocket schemes. A relative path is still
	 * accepted, for use together with {@link #getServer()}.
	 */
	String URL_PATTERN = "^((http[s]?|ws[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)";

	@JsonProperty(ID)
	@RequiredCheck(groups={NeoLoad.class})
	@Pattern(regexp=ID_PATTERN, groups={NeoLoad.class})
	String getId();

	@JsonProperty(URL)
	@RequiredCheck(groups={NeoLoad.class})
	@Pattern(regexp=URL_PATTERN, groups={NeoLoad.class})
	String getUrl();

	@JsonProperty(SERVER)
	Optional<String> getServer();

	@JsonProperty(HEADERS)
	@Valid
	List<Header> getHeaders();

	@JsonProperty(EXTRACTORS)
	@Valid
	List<VariableExtractor> getExtractors();

	@JsonProperty(MESSAGES_MAPPING)
	@Valid
	Optional<WebSocketMessagesMapping> getMessagesMapping();

	@JsonProperty(PUSH_MESSAGES)
	@Valid
	List<WebSocketPushMessage> getPushMessages();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getPushMessages().stream().map(Element.class::cast));
	}

	class Builder extends ImmutableWebSocketChannel.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
