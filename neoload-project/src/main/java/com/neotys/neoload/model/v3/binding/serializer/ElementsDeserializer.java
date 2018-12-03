package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Transaction;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationWithMsConverter.STRING_TO_TIME_DURATION_WITH_MS;

public class ElementsDeserializer extends StdDeserializer<List<Element>> {
	private static final long serialVersionUID = -5696608939252369276L;

	public ElementsDeserializer() {
		super(List.class);
	}

	@Override
	public List<Element> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		final List<Element> elements = new ArrayList<>();
		
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		final Iterator<JsonNode> iterator = jsonNode.elements();
		for (;iterator.hasNext();) {
			final JsonNode elementNode = iterator.next();
			
			Element element = null; 
			if (elementNode.has(Transaction.TRANSACTION)) {
				final JsonNode transactionNode = elementNode.get(Transaction.TRANSACTION);
				element = codec.treeToValue(transactionNode, Transaction.class);
			}
			else if (elementNode.has(Delay.DELAY)) {
				final String delayValue = elementNode.get(Delay.DELAY).asText();
				final Long delay = STRING_TO_TIME_DURATION_WITH_MS.convert(delayValue);
				element = Delay.builder().delay(String.valueOf(delay)).build();
			}
			
			if (element != null) {
				elements.add(element);
			}
		}
	
		return elements;
	}
}
