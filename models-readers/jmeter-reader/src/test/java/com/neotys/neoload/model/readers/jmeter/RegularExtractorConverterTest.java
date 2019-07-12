package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.RegexExtractor;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RegularExtractorConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCheckApplyForChildrenAndCode() {

        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("code");
        regexExtractor.setProperty("Sample.scope", "children");
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new RegularExtractorConverter().apply(regexExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        verify(spy, times(1)).readUnsupportedParameter("RegexExtractor", "ApplyTo", "Sub-Sample or Jmeter Variable");

        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)")
                .build();
        expected.add(variableExtractor);

        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForAllAndMessage() {

        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$2$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("message");
        regexExtractor.setProperty("Sample.scope", "all");
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new RegularExtractorConverter().apply(regexExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();

        verify(spy, times(1)).readSupportedParameterWithWarn("RegexExtractor", "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");

        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .template("$2$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)")
                .build();

        expected.add(variableExtractor);
        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndUrl() {

        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("url");
        regexExtractor.setProperty("Sample.scope", "parent");
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new RegularExtractorConverter().apply(regexExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        verify(spy, times(1)).readUnsupportedParameter("RegexExtractor", "Field to Check", "Request Header and URL");
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();
        expected.add(variableExtractor);
        assertEquals(expected, result);

        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndHeader() {

        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("true");
        regexExtractor.setProperty("Sample.scope", "parent");
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new RegularExtractorConverter().apply(regexExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .from(VariableExtractor.From.HEADER)
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();

        expected.add(variableExtractor);

        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndBody() {

        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("false");
        regexExtractor.setProperty("Sample.scope", "parent");

        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new RegularExtractorConverter().apply(regexExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .from(VariableExtractor.From.BODY)
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();

        expected.add(variableExtractor);
        assertEquals(expected, result);

        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndBodyUnescaped() {

        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("unescaped");
        regexExtractor.setProperty("Sample.scope", "parent");

        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new RegularExtractorConverter().apply(regexExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .from(VariableExtractor.From.BODY)
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();

        expected.add(variableExtractor);
        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

}