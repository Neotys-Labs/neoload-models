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
 * iteration. Apart from {@link #getMessagesMapping()} it is defined
 * like a {@link Request}: url, server, headers and extractors.
 *
 * <p>A channel has no identifier of its own: it is designated by its complete path, from
 * where it is declared down to its {@link #getName()}. Two channels may therefore share a
 * name as long as their paths differ.</p>
 *
 * <p>Its {@link #getPushMessages() push messages} handle the frames it receives that do not
 * answer a waiting synchronous {@link WebSocketRequest}.</p>
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, WebSocketChannel.URL, WebSocketChannel.SERVER, WebSocketChannel.HEADERS, WebSocketChannel.EXTRACTORS, WebSocketChannel.MESSAGES_MAPPING, WebSocketChannel.PUSH_MESSAGES})
@JsonSerialize(as = ImmutableWebSocketChannel.class)
@JsonDeserialize(as = ImmutableWebSocketChannel.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WebSocketChannel extends Step {
	String URL = "url";
	String SERVER = "server";
	String HEADERS = "headers";
	String EXTRACTORS = "extractors";
	String MESSAGES_MAPPING = "messages_mapping";
	String PUSH_MESSAGES = "push_messages";

	/**
	 * The {@link Request#URL} pattern: {@code http} or {@code https}, the schemes NeoLoad stores
	 * for a channel ({@code ws} and {@code wss} are not accepted), or a path relative to
	 * {@link #getServer()}.
	 */
	String URL_PATTERN = "^((http[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)";

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

	/** The channel, then its push messages and every element of their steps. */
	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getPushMessages().stream().flatMap(WebSocketPushMessage::flattened));
	}

	class Builder extends ImmutableWebSocketChannel.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
