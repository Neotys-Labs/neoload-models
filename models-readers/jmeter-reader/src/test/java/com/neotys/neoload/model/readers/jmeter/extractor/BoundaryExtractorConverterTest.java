package com.neotys.neoload.model.readers.jmeter.extractor;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.BoundaryExtractor;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BoundaryExtractorConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCheckApplyForChildrenAndCode() {

        BoundaryExtractor boundaryExtractor = new BoundaryExtractor();
        boundaryExtractor.setMatchNumber(0);
        boundaryExtractor.setName("regex");
        boundaryExtractor.setRefName("ref");
        boundaryExtractor.setUseField("code");
        boundaryExtractor.setProperty("Sample.scope", "children");
        HashTree hashTree = new HashTree();
        List<BoundaryExtractor> list = new ArrayList<>();
        list.add(boundaryExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new BoundaryExtractorConverter().apply(boundaryExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        verify(spy, times(1)).readUnsupportedParameter("BoundaryExtractor", "ApplyTo", "Sub-Sample or Jmeter Variable");

        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(boundaryExtractor.getComment())
                .name(boundaryExtractor.getRefName())
                .template("$1$")
                .matchNumber(boundaryExtractor.getMatchNumber())
                .regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)")
                .build();
        expected.add(variableExtractor);

        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("BoundaryExtractorConverter");
    }

    @Test
    public void testCheckApplyForAllAndMessage() {

        BoundaryExtractor boundaryExtractor = new BoundaryExtractor();

        boundaryExtractor.setMatchNumber(0);
        boundaryExtractor.setName("regex");
        boundaryExtractor.setRefName("ref");
        boundaryExtractor.setUseField("message");
        boundaryExtractor.setProperty("Sample.scope", "all");
        HashTree hashTree = new HashTree();
        List<BoundaryExtractor> list = new ArrayList<>();
        list.add(boundaryExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new BoundaryExtractorConverter().apply(boundaryExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();

        verify(spy, times(1)).readSupportedParameterWithWarn("BoundaryExtractor", "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");

        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(boundaryExtractor.getComment())
                .name(boundaryExtractor.getRefName())
                .template("$2$")
                .matchNumber(boundaryExtractor.getMatchNumber())
                .regexp("HTTP/\\d\\.\\d (\\d{3}) (.*)")
                .build();

        expected.add(variableExtractor);
        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("BoundaryExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndUrlAndNoSpecialCaracter() {

        BoundaryExtractor boundaryExtractor = new BoundaryExtractor();
        boundaryExtractor.setMatchNumber(0);
        boundaryExtractor.setLeftBoundary("http=");
        boundaryExtractor.setRightBoundary("/");
        boundaryExtractor.setName("regex");
        boundaryExtractor.setRefName("ref");
        boundaryExtractor.setUseField("url");
        boundaryExtractor.setProperty("Sample.scope", "parent");
        HashTree hashTree = new HashTree();
        List<BoundaryExtractor> list = new ArrayList<>();
        list.add(boundaryExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new BoundaryExtractorConverter().apply(boundaryExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        verify(spy, times(1)).readUnsupportedParameter("BoundaryExtractor", "Field to Check", "Request Header and URL");
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(boundaryExtractor.getComment())
                .name(boundaryExtractor.getRefName())
                .template("$1$")
                .matchNumber(boundaryExtractor.getMatchNumber())
                .regexp("http=(.?*)/")
                .build();
        expected.add(variableExtractor);
        assertEquals(expected, result);

        verify(spy,times(1)).readSupportedAction("BoundaryExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndHeaderAndSpecialCaracterAndNoRight() {

        BoundaryExtractor boundaryExtractor = new BoundaryExtractor();

        boundaryExtractor.setLeftBoundary("[\\^$.|?*+(){}]");
        boundaryExtractor.setMatchNumber(0);
        boundaryExtractor.setName("regex");
        boundaryExtractor.setRefName("ref");
        boundaryExtractor.setUseField("true");
        boundaryExtractor.setProperty("Sample.scope", "parent");
        HashTree hashTree = new HashTree();
        List<BoundaryExtractor> list = new ArrayList<>();
        list.add(boundaryExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new BoundaryExtractorConverter().apply(boundaryExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(boundaryExtractor.getComment())
                .name(boundaryExtractor.getRefName())
                .from(VariableExtractor.From.HEADER)
                .template("$1$")
                .matchNumber(boundaryExtractor.getMatchNumber())
                .regexp("\\[\\\\\\^\\$\\.\\|\\?\\*\\+\\(\\)\\{\\}\\](.*)")
                .build();

        expected.add(variableExtractor);

        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("BoundaryExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndBodyAndCaracterSpecial() {

        BoundaryExtractor boundaryExtractor = new BoundaryExtractor();
        boundaryExtractor.setLeftBoundary("http=");
        boundaryExtractor.setRightBoundary("$");
        boundaryExtractor.setMatchNumber(0);
        boundaryExtractor.setName("regex");
        boundaryExtractor.setRefName("ref");
        boundaryExtractor.setUseField("false");
        boundaryExtractor.setProperty("Sample.scope", "parent");

        HashTree hashTree = new HashTree();
        List<BoundaryExtractor> list = new ArrayList<>();
        list.add(boundaryExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new BoundaryExtractorConverter().apply(boundaryExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(boundaryExtractor.getComment())
                .name(boundaryExtractor.getRefName())
                .from(VariableExtractor.From.BODY)
                .template("$1$")
                .matchNumber(boundaryExtractor.getMatchNumber())
                .regexp("http=(.?*)\\$")
                .build();

        expected.add(variableExtractor);
        assertEquals(expected, result);

        verify(spy,times(1)).readSupportedAction("BoundaryExtractorConverter");
    }

    @Test
    public void testCheckApplyForParentAndBodyUnescapedAndNoLeft() {

        BoundaryExtractor boundaryExtractor = new BoundaryExtractor();
        boundaryExtractor.setRightBoundary("/");
        boundaryExtractor.setMatchNumber(0);
        boundaryExtractor.setName("regex");
        boundaryExtractor.setRefName("ref");
        boundaryExtractor.setUseField("unescaped");
        boundaryExtractor.setProperty("Sample.scope", "parent");

        HashTree hashTree = new HashTree();
        List<BoundaryExtractor> list = new ArrayList<>();
        list.add(boundaryExtractor);
        hashTree.add(list);

        List<VariableExtractor> result = new BoundaryExtractorConverter().apply(boundaryExtractor, hashTree);
        List<VariableExtractor> expected = new ArrayList<>();
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(boundaryExtractor.getComment())
                .name(boundaryExtractor.getRefName())
                .from(VariableExtractor.From.BODY)
                .template("$1$")
                .matchNumber(boundaryExtractor.getMatchNumber())
                .regexp("(.?*)/")
                .build();

        expected.add(variableExtractor);
        assertEquals(expected, result);
        verify(spy,times(1)).readSupportedAction("BoundaryExtractorConverter");
    }


}