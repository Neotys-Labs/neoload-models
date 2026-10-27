package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asObject;
import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;
import static com.neotys.neoload.model.v3.project.Element.DESCRIPTION;
import static com.neotys.neoload.model.v3.project.Element.NAME;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.ACTIONS;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.DEFAULT_USER_SESSION;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.END;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.INIT;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.USER_SESSION;
import static com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement.ASSERTIONS;
import static com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement.CONTENT_ASSERTIONS;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ImmutableContainer;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class UserPathDeserializer extends StdDeserializer<UserPath> {
	private static final long serialVersionUID = -9100000271338565024L;

	public UserPathDeserializer() {
		super(UserPath.class);
	}

	protected static UserPath.UserSession asUserSession(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		return asObject(codec, node, USER_SESSION, UserPath.UserSession.class).orElse(DEFAULT_USER_SESSION);
	}

	protected static Optional<Container> asContainer(final ObjectCodec codec, final JsonNode node, final String fieldName) throws JsonProcessingException {
		return asObject(codec, node, fieldName, Container.class).map(container -> renamed(container, fieldName));
	}
	
	/**
	 * Renames the container while keeping its assertion lists by reference. Rebuilding it through
	 * {@code builder.from()} would mark {@code content_assertions} as explicitly set, making a
	 * legacy-only container indistinguishable from one with both fields filled.
	 */
	private static Container renamed(final Container container, final String name) {
		return ImmutableContainer.copyOf(container).withName(name);
	}

	private static List<ContentAssertion> asContentAssertionList(final ObjectCodec codec, final JsonNode assertionsNode) throws JsonProcessingException {
		final ImmutableList.Builder<ContentAssertion> assertions = new ImmutableList.Builder<>();
		for (final JsonNode assertionNode : assertionsNode) {
			assertions.add(codec.treeToValue(assertionNode, ContentAssertion.class));
		}
		return assertions.build();
	}

	protected static List<ContentAssertion> asLegacyAssertions(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		final JsonNode assertionsNode = node.get(ASSERTIONS);
		if (assertionsNode != null) {
			return asContentAssertionList(codec, assertionsNode);
		}
		return ImmutableList.of();
	}

	protected static Optional<List<ContentAssertion>> asContentAssertions(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		final JsonNode contentAssertionsNode = node.get(CONTENT_ASSERTIONS);
		if (contentAssertionsNode != null) {
			return Optional.of(asContentAssertionList(codec, contentAssertionsNode));
		}
		return Optional.empty();
	}

	@Override
	public UserPath deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		final UserPath.Builder builder = UserPath.builder()
				.description(asText(node, DESCRIPTION))
				.userSession(asUserSession(codec, node))
				.init(asContainer(codec, node, INIT))
				.end(asContainer(codec, node, END))
				.addAllLegacyAssertions(asLegacyAssertions(codec, node));
		asText(node, NAME).ifPresent(builder::name);
		asContainer(codec, node, ACTIONS).ifPresent(builder::actions);
		asContentAssertions(codec, node).ifPresent(builder::addAllContentAssertions);
		return builder.build();
	}
}