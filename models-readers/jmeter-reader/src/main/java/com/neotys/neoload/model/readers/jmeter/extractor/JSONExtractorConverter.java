package com.neotys.neoload.model.readers.jmeter.extractor;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.json.jsonpath.JSONPostProcessor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class JSONExtractorConverter implements BiFunction<JSONPostProcessor, HashTree, List<VariableExtractor>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPathExtractorConverter2.class);
    private static final String JSON_EXTRACTOR = "JsonExtractor";

    JSONExtractorConverter(){}


    @Override
    public List<VariableExtractor> apply(JSONPostProcessor jsonPostProcessor, HashTree hashTree) {
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder()
                .description(jsonPostProcessor.getComment())
                .name(jsonPostProcessor.getRefNames())
                .jsonPath(jsonPostProcessor.getJsonPathExpressions())
                .matchNumber(Integer.parseInt(jsonPostProcessor.getMatchNumbers()))
                .getDefault(jsonPostProcessor.getDefaultValues());
        checkApplyTo(jsonPostProcessor);
        checkConcatenation(jsonPostProcessor);
        LOGGER.info("Convertion of JSOnExtractor");
        EventListenerUtils.readSupportedFunction(JSON_EXTRACTOR, "JSON Extractor Converter");
        return ImmutableList.of(variableExtractor.build());

    }

     static void checkConcatenation(JSONPostProcessor jsonPostProcessor) {
         if (jsonPostProcessor.getComputeConcatenation()) {
             LOGGER.warn("We can't manage the concatenation, we send only one result");
             EventListenerUtils.readSupportedParameterWithWarn(JSON_EXTRACTOR, "Concatenation", "send result", "Can't send multiple results");
         }

    }

    @SuppressWarnings("Duplicates")
    static void checkApplyTo(JSONPostProcessor jsonPostProcessor) {
        if ("all".equals(jsonPostProcessor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(JSON_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(jsonPostProcessor.fetchScope()) || "variable".equals(jsonPostProcessor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(JSON_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }
}
