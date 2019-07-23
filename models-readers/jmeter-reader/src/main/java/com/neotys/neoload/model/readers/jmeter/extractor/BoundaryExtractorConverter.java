package com.neotys.neoload.model.readers.jmeter.extractor;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.writers.RegExpUtils;
import org.apache.jmeter.extractor.BoundaryExtractor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

class BoundaryExtractorConverter implements BiFunction<BoundaryExtractor, HashTree, List<VariableExtractor>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryExtractorConverter.class);
    private static final String BOUNDARY_EXTRACTOR = "BoundaryExtractor";

    BoundaryExtractorConverter() {}

    @SuppressWarnings("Duplicates")
    public List<VariableExtractor> apply(BoundaryExtractor boundaryExtractor, HashTree subTree) {
        List<VariableExtractor> extractorList = new ArrayList<>();
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder();
        variableExtractor.description(boundaryExtractor.getComment());
        variableExtractor.name(boundaryExtractor.getRefName());
        variableExtractor.template("$1$");
        variableExtractor.matchNumber(boundaryExtractor.getMatchNumber());
        regexConverter(variableExtractor, boundaryExtractor);
        checkApplyTo(boundaryExtractor);
        variableExtractor = convertBody(boundaryExtractor, variableExtractor);
        LOGGER.info("Header on the BoundaryExtractor is a success");
        EventListenerUtils.readSupportedFunction(BOUNDARY_EXTRACTOR,"BoundaryExtractorConverter");
        extractorList.add(variableExtractor.build());
        return extractorList;
    }

    @SuppressWarnings("Duplicates")
    private void regexConverter(VariableExtractor.Builder variableExtractor, BoundaryExtractor boundaryExtractor) {

        String left = boundaryExtractor.getLeftBoundary();
        String right = boundaryExtractor.getRightBoundary();
        StringBuilder regex = new StringBuilder();

        if(left != null) {
            regex.append(RegExpUtils.escape(left));
        }
        if (right == null || "".equals(right)){
            regex.append("(.*)");
        }
        else{
            regex.append("(.?*)");
            regex.append(RegExpUtils.escape(right));
        }
        variableExtractor.regexp(regex.toString());
    }

    @SuppressWarnings("Duplicates")
    private static VariableExtractor.Builder convertBody(BoundaryExtractor boundaryExtractor, VariableExtractor.Builder variableExtractor) {
        if (boundaryExtractor.useBody() || boundaryExtractor.useBodyAsDocument() || boundaryExtractor.useUnescapedBody()) {
            variableExtractor.from(VariableExtractor.From.BODY);
        } else if (boundaryExtractor.useHeaders()) {
            variableExtractor.from(VariableExtractor.From.HEADER);
        } else if (boundaryExtractor.useMessage()) {
            variableExtractor.template("$2$");
            variableExtractor.name(boundaryExtractor.getRefName());
            variableExtractor.regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)");
            variableExtractor.description(boundaryExtractor.getComment());
            variableExtractor.matchNumber(boundaryExtractor.getMatchNumber());
        } else if (boundaryExtractor.useCode()) {
            variableExtractor.template("$1$");
            variableExtractor.name(boundaryExtractor.getRefName());
            variableExtractor.regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)");
            variableExtractor.description(boundaryExtractor.getComment());
            variableExtractor.matchNumber(boundaryExtractor.getMatchNumber());
        } else {
            LOGGER.error("Not Supported for the convertion");
            EventListenerUtils.readUnsupportedParameter(BOUNDARY_EXTRACTOR, "Field to Check", "Request Header and URL");
        }
        return variableExtractor;
    }

    @SuppressWarnings("Duplicates")
    private static void checkApplyTo(BoundaryExtractor boundaryExtractor) {
        if ("all".equals(boundaryExtractor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(BOUNDARY_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(boundaryExtractor.fetchScope()) || "variable".equals(boundaryExtractor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(BOUNDARY_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }


}