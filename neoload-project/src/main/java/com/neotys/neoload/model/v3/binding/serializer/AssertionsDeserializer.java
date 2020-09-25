package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

public class AssertionsDeserializer extends StdDeserializer<List<Assertion>> {
    private static final long serialVersionUID = -1595736549631983689L;
        
    public AssertionsDeserializer() {
        super(List.class);
    }

    @Override
    public List<Assertion> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
    	final ObjectCodec codec = jsonParser.getCodec();
        final JsonNode assertionsNode = codec.readTree(jsonParser);

        return deserialize(codec, assertionsNode);
    }
    
    protected static List<Assertion> deserialize(final ObjectCodec codec, final JsonNode assertionsNode) throws JsonProcessingException {
    	final ImmutableList.Builder<Assertion> assertions = new ImmutableList.Builder<>();

        final Iterator<JsonNode> iterator = assertionsNode.elements();
        while (iterator.hasNext()) {
            final JsonNode assertionNode = iterator.next();
           
            final Assertion assertion = doDeserialize(codec, assertionNode);
            if (assertion != null) {
            	assertions.add(assertion);
            }
        }

        return assertions.build();
    }    

    private static Assertion doDeserialize(final ObjectCodec codec, final JsonNode assertionNode) throws JsonProcessingException {
    	Assertion assertion = null;
        if (assertionNode.has(ContentAssertion.JSON_PATH) || assertionNode.has(ContentAssertion.XPATH) || assertionNode.has(ContentAssertion.CONTAINS)) {
            assertion = codec.treeToValue(assertionNode, ContentAssertion.class);
        }
        return assertion;
    }    
}
