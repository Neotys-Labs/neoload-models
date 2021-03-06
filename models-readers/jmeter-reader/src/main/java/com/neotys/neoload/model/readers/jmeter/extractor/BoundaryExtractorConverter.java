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

/**
 * This Class convert the BoundaryExtractor of JMeter into a Variable Extractor of Neoload
 */
class BoundaryExtractorConverter implements BiFunction<BoundaryExtractor, HashTree, List<VariableExtractor>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryExtractorConverter.class);

    private static final String BOUNDARY_EXTRACTOR = "BoundaryExtractor";

    //Constructor
    BoundaryExtractorConverter() {
    }

    //Methods
    @SuppressWarnings("Duplicates")
    public List<VariableExtractor> apply(final BoundaryExtractor boundaryExtractor, final HashTree subTree) {
        final List<VariableExtractor> extractorList = new ArrayList<>();
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder();
        variableExtractor.description(boundaryExtractor.getComment());
        variableExtractor.name(boundaryExtractor.getRefName());
        variableExtractor.template("$1$");
        variableExtractor.matchNumber(boundaryExtractor.getMatchNumber());
        regexConverter(variableExtractor, boundaryExtractor);
        checkApplyTo(boundaryExtractor);
        variableExtractor = convertBody(boundaryExtractor, variableExtractor);
        LOGGER.info("Header on the BoundaryExtractor is a success");
        EventListenerUtils.readSupportedFunction(BOUNDARY_EXTRACTOR, "BoundaryExtractorConverter");
        extractorList.add(variableExtractor.build());
        return extractorList;
    }

    /**
     * This method convert the right and left value into a regular expression for the variable extractor
     *
     * @param variableExtractor
     * @param boundaryExtractor
     */
    @SuppressWarnings("Duplicates")
    private void regexConverter(final VariableExtractor.Builder variableExtractor, final BoundaryExtractor boundaryExtractor) {

        final String left = boundaryExtractor.getLeftBoundary();
        final String right = boundaryExtractor.getRightBoundary();
        final StringBuilder regex = new StringBuilder();

        if (left != null) {
            regex.append(RegExpUtils.escape(left));
        }
        if (right == null || "".equals(right)) {
            regex.append("(.*)");
        } else {
            regex.append("(.?*)");
            regex.append(RegExpUtils.escape(right));
        }
        variableExtractor.regexp(regex.toString());
    }

    /**
     * This method convert the source of the Jmeter Elecment
     * If the source is the message or just OK we have to change the variableExtractor.Builder
     * Because we know all the parameters for the message or OK
     *
     * @param boundaryExtractor
     * @param variableExtractor
     * @return
     */
    @SuppressWarnings("Duplicates")
    private static VariableExtractor.Builder convertBody(final BoundaryExtractor boundaryExtractor, final VariableExtractor.Builder variableExtractor) {
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

    /**
     * For JMeterExtractor, we can only manage the main sample option, we can apply this element in the same branch
     * This is why me only manage main sample
     * If you want sub sample, you have to manage the subtree too
     * 
     * @param boundaryExtractor
     */
    @SuppressWarnings("Duplicates")
    private static void checkApplyTo(final BoundaryExtractor boundaryExtractor) {
        if ("all".equals(boundaryExtractor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(BOUNDARY_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(boundaryExtractor.fetchScope()) || "variable".equals(boundaryExtractor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(BOUNDARY_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }


}