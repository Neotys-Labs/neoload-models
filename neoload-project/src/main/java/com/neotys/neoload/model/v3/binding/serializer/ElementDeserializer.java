package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Delay;

import java.io.IOException;

public class ElementDeserializer extends StdDeserializer<Element> {
	private static final long serialVersionUID = -5696608939252369276L;

	public ElementDeserializer() {
		super(Element.class);
	}

	@Override
	public Element deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		if (jsonNode.has(Delay.DELAY)) {
			final String delayValue = jsonNode.get(Delay.DELAY).asText();
			final StringToTimeDurationConverter stringToTimeDurationConverter = new StringToTimeDurationConverter();
			final Integer delayInteger = stringToTimeDurationConverter.convert(delayValue);
			return Delay.builder().delay(String.valueOf(delayInteger)).build();
		}
		return null;
	}
}
