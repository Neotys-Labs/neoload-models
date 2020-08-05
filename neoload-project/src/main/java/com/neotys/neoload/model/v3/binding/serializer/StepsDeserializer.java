package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.DELAY;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.IF;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.JAVASCRIPT;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.LOOP;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.REQUEST;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.SWITCH;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.THINK_TIME;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.TRANSACTION;
import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.WHILE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.JavaScript;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.Switch;
import com.neotys.neoload.model.v3.project.userpath.ThinkTime;
import com.neotys.neoload.model.v3.project.userpath.While;

public class StepsDeserializer extends StdDeserializer<List<Step>> {
    private static final long serialVersionUID = -5696608939252369276L;

    public StepsDeserializer() {
        super(List.class);
    }

    @Override
    public List<Step> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        final List<Step> actions = new ArrayList<>();

        final ObjectCodec codec = jsonParser.getCodec();
        final JsonNode jsonNode = codec.readTree(jsonParser);

        final Iterator<JsonNode> iterator = jsonNode.elements();
        while (iterator.hasNext()) {
            final JsonNode actionNode = iterator.next();

            Step action = null;
            if (actionNode.has(TRANSACTION)) {
                final JsonNode transactionNode = actionNode.get(TRANSACTION);
                action = codec.treeToValue(transactionNode, Container.class);
            } else if (actionNode.has(REQUEST)) {
                final JsonNode requestNode = actionNode.get(REQUEST);
                action = codec.treeToValue(requestNode, Request.class);
            } else if (actionNode.has(DELAY)) {
                final String delayValue = actionNode.get(DELAY).asText();
                final String delay = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(delayValue);
                action = Delay.builder().value(String.valueOf(delay)).build();
            } else if (actionNode.has(THINK_TIME)) {
                final String thinkTimeValue = actionNode.get(THINK_TIME).asText();
                final String thinkTime = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(thinkTimeValue);
                action = ThinkTime.builder().value(String.valueOf(thinkTime)).build();
            } else if (actionNode.has(JAVASCRIPT)) {
                final JsonNode javascriptNode = actionNode.get(JAVASCRIPT);
                action = codec.treeToValue(javascriptNode, JavaScript.class);
            } else if (actionNode.has(IF)) {
                final JsonNode ifNode = actionNode.get(IF);
                action = codec.treeToValue(ifNode, If.class);
            } else if (actionNode.has(LOOP)) {
                final JsonNode loopNode = actionNode.get(LOOP);
                action = codec.treeToValue(loopNode, Loop.class);
            } else if (actionNode.has(WHILE)) {
                final JsonNode whileNode = actionNode.get(WHILE);
                action = codec.treeToValue(whileNode, While.class);
            } else if (actionNode.has(SWITCH)){
                final JsonNode switchNode = actionNode.get(SWITCH);
                action = codec.treeToValue(switchNode,Switch.class);
            }

            if (action != null) {
                actions.add(action);
            }
        }

        return actions;
    }
}
