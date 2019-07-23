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

class XPathExtractorConverter implements BiFunction<XPathExtractor, HashTree, List<VariableExtractor>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPathExtractorConverter.class);
    private static final String XPath_EXTRACTOR = "XPathExtractor";

    XPathExtractorConverter() {}

    @Override
    public List<VariableExtractor> apply(XPathExtractor xPathExtractor, HashTree hashTree) {
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder()
                .description(xPathExtractor.getComment())
                .name(xPathExtractor.getRefName())
                .xpath(xPathExtractor.getXPathQuery())
                .matchNumber(xPathExtractor.getMatchNumber())
                .getDefault(xPathExtractor.getDefaultValue());
        checkUnsupported(xPathExtractor);
        checkApplyTo(xPathExtractor);
        LOGGER.info("Convertion of XpathExtractor");
        EventListenerUtils.readSupportedFunction(XPath_EXTRACTOR, "Xpath Extractor Converter");
        return ImmutableList.of(variableExtractor.build());
    }

    @SuppressWarnings("Duplicates")
     static void checkApplyTo(XPathExtractor xPathExtractor) {
        if ("all".equals(xPathExtractor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(XPath_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(xPathExtractor.fetchScope()) || "variable".equals(xPathExtractor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }

     static void checkUnsupported(XPathExtractor xPathExtractor) {
        if(!xPathExtractor.isQuiet() || !xPathExtractor.showWarnings()|| !xPathExtractor.reportErrors()){
            LOGGER.warn("We Use Tidy with isQuiet, ShowWarning and Reports Errors activate ");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"Tidy Parameter", " All Options");
        }
        else if(xPathExtractor.useNameSpace() || xPathExtractor.isWhitespace() || xPathExtractor.isValidating() || xPathExtractor.isDownloadDTDs()){
            LOGGER.warn("We support only Tidy Parameter ");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"OtherParameters", " All Options");
        }

        if(xPathExtractor.getFragment()){
            LOGGER.warn("We already use the fragment in own extractor");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"Fragment Xpath", " textContent");
        }
    }
}
