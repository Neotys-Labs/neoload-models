package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.XPath2Extractor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

class XPathExtractorConverter2 implements BiFunction<XPath2Extractor, HashTree, List<VariableExtractor>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPathExtractorConverter2.class);
    private static final String XPath_EXTRACTOR = "XPathExtractor";

    XPathExtractorConverter2() {}


    public List<VariableExtractor> apply(XPath2Extractor xPathExtractor, HashTree hashTree) {
        VariableExtractor.Builder variableExtractor = VariableExtractor.builder()
                .description(xPathExtractor.getComment())
                .name(xPathExtractor.getRefName())
                .xpath(xPathExtractor.getXPathQuery())
                .matchNumber(xPathExtractor.getMatchNumber())
                .getDefault(xPathExtractor.getDefaultValue());
        checkUnsupported(xPathExtractor);
        checkApplyTo(xPathExtractor);
        return ImmutableList.of(variableExtractor.build());
    }

    @SuppressWarnings("Duplicates")
     static void checkApplyTo(XPath2Extractor xPathExtractor) {
        if ("all".equals(xPathExtractor.fetchScope())) {
            LOGGER.warn("We can't manage the sub-samples conditions");
            EventListenerUtils.readSupportedParameterWithWarn(XPath_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");
        } else if ("children".equals(xPathExtractor.fetchScope()) || "variable".equals(xPathExtractor.fetchScope())) {
            LOGGER.error("We can't manage the sub-sample and Jmeter Variable Use, so we convert like main sample only");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
        }
    }

     static void checkUnsupported(XPath2Extractor xPathExtractor) {
        if(xPathExtractor.getFragment()){
            LOGGER.warn("We already use the fragment in own Extractor");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"Fragment Xpath", " textContent");
        }
        else if (!xPathExtractor.getNamespaces().isEmpty()){
            LOGGER.warn("We don't manage the namespaces");
            EventListenerUtils.readUnsupportedParameter(XPath_EXTRACTOR,"Namespace Xpath", " textContent");
        }
    }
}
