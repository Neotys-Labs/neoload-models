package com.neotys.neoload.model.readers.jmeter.extractor;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.XPathExtractor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert XpathExtractor of JMeter into a Variable Extractor of Neoload
 */
class XPathExtractorConverter implements BiFunction<XPathExtractor, HashTree, List<VariableExtractor>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(XPathExtractorConverter.class);

    private static final String XPathEXTRACTOR = "XPathExtractor";

    //Constructor
    XPathExtractorConverter() {
    }

    //Methods
    @Override
    public List<VariableExtractor> apply(final XPathExtractor xPathExtractor, final HashTree hashTree) {
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder()
                .description(xPathExtractor.getComment())
                .name(xPathExtractor.getRefName())
                .xpath(xPathExtractor.getXPathQuery())
                .matchNumber(xPathExtractor.getMatchNumber())
                .getDefault(xPathExtractor.getDefaultValue());
        checkUnsupported(xPathExtractor);
        checkApplyTo(xPathExtractor);
        LOGGER.info("Convertion of XpathExtractor");
        EventListenerUtils.readSupportedFunction(XPathEXTRACTOR, "Xpath Extractor Converter");
        return ImmutableList.of(variableExtractor.build());
    }

    /**
     * For JMeterExtractor, we can only manage the main sample option, we can apply this element in the same branch
     * This is why me only manage main sample
     * If you want sub sample, you have to manage the subtree too
     *
     * @param xPathExtractor
     */
    @SuppressWarnings("Duplicates")
    static void checkApplyTo(final XPathExtractor xPathExtractor) {
        if ("all".equals(xPathExtractor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(XPathEXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(xPathExtractor.fetchScope()) || "variable".equals(xPathExtractor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(XPathEXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }

    static void checkUnsupported(final XPathExtractor xPathExtractor) {
        if (!xPathExtractor.isQuiet() || !xPathExtractor.showWarnings() || !xPathExtractor.reportErrors()) {
            LOGGER.warn("We Use Tidy with isQuiet, ShowWarning and Reports Errors activate ");
            EventListenerUtils.readUnsupportedParameter(XPathEXTRACTOR, "Tidy Parameter", " All Options");
        } else if (xPathExtractor.useNameSpace() || xPathExtractor.isWhitespace() || xPathExtractor.isValidating() || xPathExtractor.isDownloadDTDs()) {
            LOGGER.warn("We support only Tidy Parameter ");
            EventListenerUtils.readUnsupportedParameter(XPathEXTRACTOR, "OtherParameters", " All Options");
        }

        if (xPathExtractor.getFragment()) {
            LOGGER.warn("We already use the fragment in own extractor");
            EventListenerUtils.readUnsupportedParameter(XPathEXTRACTOR, "Fragment Xpath", " textContent");
        }
    }
}
