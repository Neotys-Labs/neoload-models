package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
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
                .matchNumber(xPathExtractor.getMatchNumber());
        checkUnsupported(xPathExtractor);

        return ImmutableList.of(variableExtractor.build());
    }

    private void checkUnsupported(XPathExtractor xPathExtractor) {
        if(!xPathExtractor.isQuiet() || !xPathExtractor.showWarnings()|| !xPathExtractor.reportErrors()){
            LOGGER.warn("We Use Tidy with isQuiet, ShowWarning and Reports Errors activate ");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"Tidy Parameter", " All Options");
        }
        else if(xPathExtractor.useNameSpace() || xPathExtractor.isWhitespace() || xPathExtractor.isValidating() || xPathExtractor.isDownloadDTDs()){
            LOGGER.warn("We support only Tidy Parameter ");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"OtherParameters", " All Options");
        }

        if(xPathExtractor.getFragment()){
            LOGGER.warn("We already use the fragment in own Extractor");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"Fragment Xpath", " textContent");
        }
    }
}
