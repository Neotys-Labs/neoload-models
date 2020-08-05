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

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

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

		return UserPath.builder()
				.name(name)
				.description(Optional.ofNullable(description))
				.userSession(userSession)
				.init(Optional.ofNullable(init))
				.actions(actions)
				.end(Optional.ofNullable(end))
				.build();
	}
}