package com.neotys.neoload.model.readers.jmeter.extractor;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.XPath2Extractor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class XPathExtractorConverter2Test {

    private TestEventListener spy;
    private static final String XPath_EXTRACTOR = "XPathExtractor";

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCheckApplyTo(){
        XPath2Extractor xPathExtractor = new XPath2Extractor();
        xPathExtractor.setProperty("Sample.scope","all");
        XPathExtractorConverter2.checkApplyTo(xPathExtractor);
        verify(spy, times(1)).readSupportedParameterWithWarn(XPath_EXTRACTOR, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");

        xPathExtractor.setProperty("Sample.scope","children");
        XPathExtractorConverter2.checkApplyTo(xPathExtractor);
        verify(spy, times(1)).readUnsupportedParameter(XPath_EXTRACTOR, "ApplyTo", "Sub-Sample or Jmeter Variable");
    }

    @Test
    public void testCheckUnsupported(){
        XPath2Extractor xPathExtractor = new XPath2Extractor();
        xPathExtractor.setFragment(true);
        XPathExtractorConverter2.checkUnsupported(xPathExtractor);
        verify(spy, times(1)).readUnsupportedParameter(XPath_EXTRACTOR,"Fragment Xpath", " textContent");

        xPathExtractor.setFragment(false); xPathExtractor.setNamespaces("not null");
        XPathExtractorConverter2.checkUnsupported(xPathExtractor);
        verify(spy, times(1)).readUnsupportedParameter(XPath_EXTRACTOR,"Namespace Xpath", " textContent");

    }

    @Test
    public void testApply(){
        XPath2Extractor xPathExtractor = new XPath2Extractor();
        xPathExtractor.setComment("test");
        xPathExtractor.setRefName("Xpath");
        xPathExtractor.setMatchNumber(0);
        xPathExtractor.setDefaultValue("none");
        xPathExtractor.setProperty("Sample.scope","parent");

        List<VariableExtractor> result = new XPathExtractorConverter2().apply(xPathExtractor,null);
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(xPathExtractor.getComment())
                .name(xPathExtractor.getRefName())
                .xpath(xPathExtractor.getXPathQuery())
                .matchNumber(xPathExtractor.getMatchNumber())
                .getDefault(xPathExtractor.getDefaultValue())
                .build();
        List<VariableExtractor> expected = new ArrayList<>();
        expected.add(variableExtractor);
        assertEquals(expected, result);
    }
}
