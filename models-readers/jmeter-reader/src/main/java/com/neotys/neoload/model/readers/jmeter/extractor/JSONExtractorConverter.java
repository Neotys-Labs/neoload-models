package com.neotys.neoload.model.readers.jmeter.extractor;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.json.jsonpath.JSONPostProcessor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This Class convert JSONExtractor of JMeter into a Varaible Extractor of Neoload
 */
public class JSONExtractorConverter implements BiFunction<JSONPostProcessor, HashTree, List<VariableExtractor>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(XPathExtractorConverter2.class);

    private static final String JSON_EXTRACTOR = "JsonExtractor";

    //Constructor
    JSONExtractorConverter() {
    }

    //Methods
    @Override
    public List<VariableExtractor> apply(JSONPostProcessor jsonPostProcessor, HashTree hashTree) {

        VariableExtractor.Builder variableExtractor = VariableExtractor.builder()
                .description(jsonPostProcessor.getComment())
                .name(jsonPostProcessor.getRefNames())
                .jsonPath(jsonPostProcessor.getJsonPathExpressions())
                .getDefault(jsonPostProcessor.getDefaultValues());
        /*
        First, we try to take the value of the matchNumber,
        If there is an error, we try to check the variables and take the good one
        Finally, if there is an error again,
        Maybe the user put a variable with a function or fill the form with a wrong value
         */
        try{
            variableExtractor.matchNumber(Integer.parseInt(jsonPostProcessor.getMatchNumbers()));
        }catch(Exception e){
            try{
                variableExtractor.matchNumber(Integer.parseInt(ContainerUtils.getValue(jsonPostProcessor.getMatchNumbers())));
            }catch (Exception e1){
                LOGGER.warn("We can't manage the variable into the Match Number \n"
                        + "So We put 0 in value of Match Number", e1);

                EventListenerUtils.readUnsupportedParameter(JSON_EXTRACTOR, "Variable String","MatchNumber");
                variableExtractor.matchNumber(0);
            }
        }
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

    /**
     * For JMeterExtractor, we can only manage the main sample option, we can apply this element in the same branch
     * This is why me only manage main sample
     * If you want sub sample, you have to manage the subtree too
     * 
     * @param jsonPostProcessor
     */
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
