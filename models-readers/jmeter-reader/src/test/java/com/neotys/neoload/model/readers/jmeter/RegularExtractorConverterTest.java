package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.RegexExtractor;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RegularExtractorConverterTest {

    @Test
    public void testCheckApplyForChildrenAndCode() {
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("code");
        regexExtractor.setProperty("Sample.scope", "children");
        Request.Builder result = Request.builder();
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        RegularExtractorConverter.extract(result, hashTree);

        verify(spy, times(1)).readUnsupportedParameter("RegexExtractor", "ApplyTo", "Sub-Sample or Jmeter Variable");

        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)")
                .build();
        Request expected = Request.builder().addExtractors(variableExtractor).build();
        assertEquals(expected, result.build());
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForAllAndMessage() {
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$2$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("message");
        regexExtractor.setProperty("Sample.scope", "all");
        Request.Builder result = Request.builder();
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        RegularExtractorConverter.extract(result, hashTree);

        verify(spy, times(1)).readSupportedParameterWithWarn("RegexExtractor", "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");

        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .template("$2$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)")
                .build();
        Request expected = Request.builder().addExtractors(variableExtractor).build();
        assertEquals(expected, result.build());
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndUrl() {
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("url");
        regexExtractor.setProperty("Sample.scope", "parent");
        Request.Builder result = Request.builder();
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        RegularExtractorConverter.extract(result, hashTree);
        verify(spy, times(1)).readUnsupportedParameter("RegexExtractor", "Field to Check", "Request Header and URL");
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();
        Request expected = Request.builder().addExtractors(variableExtractor).build();
        assertEquals(expected, result.build());

        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndHeader() {
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("true");
        regexExtractor.setProperty("Sample.scope", "parent");
        Request.Builder result = Request.builder();
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        RegularExtractorConverter.extract(result, hashTree);
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .from(VariableExtractor.From.HEADER)
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();
        Request expected = Request.builder().addExtractors(variableExtractor).build();
        assertEquals(expected, result.build());
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndBody() {
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("false");
        regexExtractor.setProperty("Sample.scope", "parent");
        Request.Builder result = Request.builder();
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        RegularExtractorConverter.extract(result, hashTree);
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .from(VariableExtractor.From.BODY)
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();
        Request expected = Request.builder().addExtractors(variableExtractor).build();
        assertEquals(expected, result.build());

        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndBodyUnescaped() {
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        RegexExtractor regexExtractor = new RegexExtractor();
        regexExtractor.setTemplate("$1$");
        regexExtractor.setRegex("http=");
        regexExtractor.setMatchNumber(0);
        regexExtractor.setName("regex");
        regexExtractor.setRefName("ref");
        regexExtractor.setUseField("unescaped");
        regexExtractor.setProperty("Sample.scope", "parent");
        Request.Builder result = Request.builder();
        HashTree hashTree = new HashTree();
        List<RegexExtractor> list = new ArrayList<>();
        list.add(regexExtractor);
        hashTree.add(list);

        RegularExtractorConverter.extract(result, hashTree);
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(regexExtractor.getComment())
                .name(regexExtractor.getRefName())
                .from(VariableExtractor.From.BODY)
                .template("$1$")
                .matchNumber(regexExtractor.getMatchNumber())
                .regexp(regexExtractor.getRegex())
                .build();
        Request expected = Request.builder().addExtractors(variableExtractor).build();
        assertEquals(expected, result.build());
        verify(spy,times(1)).readSupportedAction("RegexExtractorConverter");
    }

}