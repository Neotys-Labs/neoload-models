package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.DeserializerHelper.asText;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.security.WsDerivedPasswordType;
import com.neotys.neoload.model.v3.project.security.WsPasswordType;
import com.neotys.neoload.model.v3.project.security.WsSimplePasswordType;
import java.io.IOException;

public final class WsPasswordTypeDeserializer extends StdDeserializer<WsPasswordType> {
	private static final long serialVersionUID = -2731904576182903315L;

	public WsPasswordTypeDeserializer() {
		super(WsPasswordType.class);
	}

	@Override
	public WsPasswordType deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		final JsonNode node = parser.getCodec().readTree(parser);
		if (node.isObject() && node.has(WsDerivedPasswordType.DERIVED_KEY)) {
			return deserializeDerived(node.get(WsDerivedPasswordType.DERIVED_KEY));
		}
		for (final WsSimplePasswordType type : WsSimplePasswordType.values()) {
			if (type.getValue().equals(node.asText())) {
				return type;
			}
		}
		return ctx.reportInputMismatch(WsPasswordType.class,
				"Incorrect value for 'password_type': must be 'plain_text', 'digest' or a 'derived_key' object with 'salt' and 'iteration'.");
	}

	private static WsPasswordType deserializeDerived(final JsonNode node) {
		final WsDerivedPasswordType.Builder builder = WsDerivedPasswordType.builder();
		if (node.has(WsDerivedPasswordType.SALT)) {
			builder.salt(asText(node, WsDerivedPasswordType.SALT));
		}
		if (node.has(WsDerivedPasswordType.ITERATION)) {
			builder.iteration(asText(node, WsDerivedPasswordType.ITERATION));
		}
		return builder.build();
	}
}
