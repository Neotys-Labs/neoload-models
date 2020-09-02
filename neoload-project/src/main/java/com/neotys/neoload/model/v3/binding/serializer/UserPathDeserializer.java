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
import static com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertionElement.ASSERT_CONTENT;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

public final class UserPathDeserializer extends StdDeserializer<UserPath> {
	private static final long serialVersionUID = -9100000271338565024L;

	public UserPathDeserializer() {
		super(UserPath.class);
	}

	protected static UserPath.UserSession asUserSession(final ObjectCodec codec, final JsonNode node) throws JsonProcessingException {
		UserPath.UserSession userSession = asObject(codec, node, USER_SESSION, UserPath.UserSession.class);
		if (userSession == null) {
			userSession = DEFAULT_USER_SESSION;
		}
		return userSession;
	}

	protected static Container asContainer(final ObjectCodec codec, final JsonNode node, final String fieldName) throws JsonProcessingException {
		Container container = asObject(codec, node, fieldName, Container.class);
		if (container != null) {
			container = Container.builder()
					.from(container)
					.name(fieldName)
					.build();
		}
		return container;
	}
	
	protected static List<ContentAssertion> asContentAssertions(final ObjectCodec codec, final JsonNode node, final String fieldName) throws JsonProcessingException {
		final ImmutableList.Builder<ContentAssertion> assertions = new ImmutableList.Builder<>();
			
		final JsonNode nodeObject = node.get(fieldName);
		if ((nodeObject != null) && (nodeObject.isArray())) {
	    	final ArrayNode array = (ArrayNode) nodeObject;
	    	for (int i = 0, ilength = array.size(); i < ilength; i++) {
				final JsonNode arrayElement = array.get(i);
				final ContentAssertion assertion = codec.treeToValue(arrayElement, ContentAssertion.class);
				assertions.add(assertion);
			}
	    }
		
		return assertions.build();
	}


	@Override
	public UserPath deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		final String name = asText(node, NAME);
		final String description = asText(node, DESCRIPTION);
		final UserPath.UserSession userSession = asUserSession(codec, node);
		final Container init = asContainer(codec, node, INIT);
		final Container actions = asContainer(codec, node, ACTIONS);
		final Container end = asContainer(codec, node, END);
		final List<ContentAssertion> assertions = asContentAssertions(codec, node, ASSERT_CONTENT);

		return UserPath.builder()
				.name(name)
				.description(Optional.ofNullable(description))
				.userSession(userSession)
				.init(Optional.ofNullable(init))
				.actions(actions)
				.end(Optional.ofNullable(end))
				.addAllContentAssertions(assertions)
				.build();
	}
}