package com.neotys.neoload.model.readers.jmeter.extractor;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.RegexExtractor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the RegularExtractor of JMeter into VariableExtractor of Neoload
 */
final class RegularExtractorConverter implements BiFunction<RegexExtractor, HashTree, List<VariableExtractor>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(RegularExtractorConverter.class);

    private static final String REGEX_EXTRACTOR = "RegexExtractor";

    //Constructor
    RegularExtractorConverter() {
    }

    //Methods
    @SuppressWarnings("Duplicates")
    public List<VariableExtractor> apply(final RegexExtractor regexExtractor, final HashTree subTree) {
        List<VariableExtractor> extractorList = new ArrayList<>();
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder();
        variableExtractor.description(regexExtractor.getComment());
        variableExtractor.name(regexExtractor.getRefName());
        variableExtractor.template(regexExtractor.getTemplate());
        variableExtractor.matchNumber(regexExtractor.getMatchNumber());
        variableExtractor.regexp(regexExtractor.getRegex());
        checkApplyTo(regexExtractor);
        variableExtractor = convertBody(regexExtractor, variableExtractor);
        LOGGER.info("Header on the RegexExtractor is a success");
        EventListenerUtils.readSupportedAction("RegexExtractorConverter");
        extractorList.add(variableExtractor.build());
        return extractorList;
    }

    /**
     * This Method convert the source of the Jmeter Elecment
     * If the source is the message or just OK we have to change the variableExtractor.Builder
     * Because we know all the parameters for the message or OK
     *
     * @param regexExtractor
     * @param variableExtractor
     * @return
     */
    @SuppressWarnings("Duplicates")
    private static VariableExtractor.Builder convertBody(final RegexExtractor regexExtractor, final VariableExtractor.Builder variableExtractor) {
        if (regexExtractor.useBody() || regexExtractor.useBodyAsDocument() || regexExtractor.useUnescapedBody()) {
            variableExtractor.from(VariableExtractor.From.BODY);
        } else if (regexExtractor.useHeaders()) {
            variableExtractor.from(VariableExtractor.From.HEADER);
        } else if (regexExtractor.useMessage()) {
            variableExtractor.template("$2$");
            variableExtractor.name(regexExtractor.getRefName());
            variableExtractor.regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)");
            variableExtractor.description(regexExtractor.getComment());
            variableExtractor.matchNumber(regexExtractor.getMatchNumber());
        } else if (regexExtractor.useCode()) {
            variableExtractor.template("$1$");
            variableExtractor.name(regexExtractor.getRefName());
            variableExtractor.regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)");
            variableExtractor.description(regexExtractor.getComment());
            variableExtractor.matchNumber(regexExtractor.getMatchNumber());
        } else {
            LOGGER.error("Not Supported for the convertion");
            EventListenerUtils.readUnsupportedParameter(REGEX_EXTRACTOR, "Field to Check", "Request Header and URL");
        }
        return variableExtractor;
    }

    /**
     * This Method convert the source of the Jmeter Elecment
     * If the source is the message or just OK we have to change the variableExtractor.Builder
     * Because we know all the parameters for the message or OK
     *
     * @param regexExtractor
     */
    @SuppressWarnings("Duplicates")
    private static void checkApplyTo(final RegexExtractor regexExtractor) {
        if ("all".equals(regexExtractor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(REGEX_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(regexExtractor.fetchScope()) || "variable".equals(regexExtractor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(REGEX_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }


}
