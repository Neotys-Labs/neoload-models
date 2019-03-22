package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

import java.io.IOException;
import java.util.Optional;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asObject;
import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;
import static com.neotys.neoload.model.v3.project.Element.DESCRIPTION;
import static com.neotys.neoload.model.v3.project.Element.NAME;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.*;

public final class IfDeserializer extends StdDeserializer<If> {
	private static final long serialVersionUID = -9010027300013658542L;

	public IfDeserializer() {
		super(If.class);
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
	public If deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);

		final String name = asText(node, NAME);
		final String description = asText(node, DESCRIPTION);
		final Container thenContainer = asContainer(codec, node, If.THEN);
		final Container elseContainer = asContainer(codec, node, If.ELSE);

		return If.builder()
				.name(name)
				.description(Optional.ofNullable(description))
				.then(thenContainer)
				.getElse(Optional.ofNullable(elseContainer))
				.build();
	}
}