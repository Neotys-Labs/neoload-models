package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asObject;
import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;
import static com.neotys.neoload.model.v3.project.Element.DESCRIPTION;
import static com.neotys.neoload.model.v3.project.Element.NAME;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.ACTIONS;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.DEFAULT_USER_SESSION;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.END;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.GO_TO_NEXT_ITERATION;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.INIT;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.ON_ASSERTION_FAILURE;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.ON_ERROR;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.STOP_AND_START_NEW_VU;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.THINK_TIME;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.USER_SESSION;
import static com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime.FACTOR;
import static com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime.OVERRIDE;
import static com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime.RANDOM;
import static com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement.ASSERTIONS;
import static com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement.CONTENT_ASSERTIONS;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.ImmutableContainer;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class UserPathDeserializer extends StdDeserializer<UserPath> {
	private static final long serialVersionUID = -9100000271338565024L;

	private static final Map<String, UserPath.FailurePolicy> FAILURE_POLICIES = ImmutableMap.of(
			GO_TO_NEXT_ITERATION, UserPath.FailurePolicy.GO_TO_NEXT_ITERATION,
			STOP_AND_START_NEW_VU, UserPath.FailurePolicy.STOP_AND_START_NEW_VU);

	public UserPathDeserializer() {
		super(UserPath.class);
	}

	protected static UserPath.UserSession asUserSession(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		return asObject(codec, node, USER_SESSION, UserPath.UserSession.class).orElse(DEFAULT_USER_SESSION);
	}

	/**
	 * Reads a failure policy, rejecting anything but one of its names with a message pointing at the user path.
	 * Jackson alone would map a number to the policy at that position.
	 */
	protected static Optional<UserPath.FailurePolicy> asFailurePolicy(final JsonNode node, final String fieldName, final String userPathName) throws IOException {
		final JsonNode policyNode = node.get(fieldName);
		if (policyNode == null || policyNode.isNull()) {
			return Optional.empty();
		}
		final UserPath.FailurePolicy policy = FAILURE_POLICIES.get(policyNode.textValue());
		if (policy == null) {
			throw incorrectValue(fieldName, userPathName, "must be '" + String.join("' or '", FAILURE_POLICIES.keySet()) + "'", policyNode);
		}
		return Optional.of(policy);
	}

	protected static Optional<UserPathThinkTime> asThinkTime(final ObjectCodec codec, final JsonNode node, final String userPathName) throws IOException {
		final JsonNode thinkTimeNode = node.get(THINK_TIME);
		if (thinkTimeNode != null && !thinkTimeNode.isNull() && !thinkTimeNode.isObject()) {
			throw incorrectValue(THINK_TIME, userPathName, "must be an object with '" + OVERRIDE + "', '" + FACTOR + "' and/or '" + RANDOM + "'", thinkTimeNode);
		}
		final JsonNode overrideNode = thinkTimeNode == null ? null : thinkTimeNode.get(OVERRIDE);
		if (overrideNode != null && overrideNode.isContainerNode()) {
			throw incorrectValue(THINK_TIME + "." + OVERRIDE, userPathName, "must be a single duration", overrideNode);
		}
		return asObject(codec, node, THINK_TIME, UserPathThinkTime.class);
	}

	/**
	 * Builds a plain reading error: Jackson keeps it as the cause of the error it raises, and NeoLoad shows the
	 * message of that cause, free of Jackson details.
	 */
	private static IOException incorrectValue(final String fieldName, final String userPathName, final String expectation, final JsonNode found) {
		return new IOException("Incorrect value for '" + fieldName + "' in User Path '" + userPathName + "': " + expectation + " (found: " + found + ").");
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

		final Optional<String> name = asText(node, NAME);
		final UserPath.Builder builder = UserPath.builder()
				.description(asText(node, DESCRIPTION))
				.userSession(asUserSession(codec, node))
				.onError(asFailurePolicy(node, ON_ERROR, name.orElse(null)))
				.onAssertionFailure(asFailurePolicy(node, ON_ASSERTION_FAILURE, name.orElse(null)))
				.thinkTime(asThinkTime(codec, node, name.orElse(null)))
				.init(asContainer(codec, node, INIT))
				.end(asContainer(codec, node, END))
				.addAllLegacyAssertions(asLegacyAssertions(codec, node));
		name.ifPresent(builder::name);
		asContainer(codec, node, ACTIONS).ifPresent(builder::actions);
		asContentAssertions(codec, node).ifPresent(builder::addAllContentAssertions);
		return builder.build();
	}
}