package com.neotys.neoload.model.readers.jmeter.extractor;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.json.jsonpath.JSONPostProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JSONExtractorConverterTest {

    private TestEventListener spy;
    private static final String JSON_Extractor = "JsonExtractor";

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCheckApplyTo(){
        JSONPostProcessor jsonPostProcessor = new JSONPostProcessor();
        jsonPostProcessor.setProperty("Sample.scope","all");
        JSONExtractorConverter.checkApplyTo(jsonPostProcessor);
        verify(spy, times(1)).readSupportedParameterWithWarn(JSON_Extractor, "ApplyTo", "Main Sample & Sub-Sample", "Can't check Sub-Sample");

        jsonPostProcessor.setProperty("Sample.scope","children");
        JSONExtractorConverter.checkApplyTo(jsonPostProcessor);
        verify(spy, times(1)).readUnsupportedParameter(JSON_Extractor, "ApplyTo", "Sub-Sample or Jmeter Variable");
    }

    @Test
    public void testCheckConcatenation(){
        JSONPostProcessor jsonPostProcessor = new JSONPostProcessor();
        jsonPostProcessor.setComputeConcatenation(true);
        JSONExtractorConverter.checkConcatenation(jsonPostProcessor);
        verify(spy, times(1)).readSupportedParameterWithWarn(JSON_Extractor, "Concatenation", "send result", "Can't send multiple results");
    }

    @Test
    public void testApply(){
        JSONPostProcessor jsonPostProcessor = new JSONPostProcessor();
        jsonPostProcessor.setComment("test");
        jsonPostProcessor.setRefNames("Json");
        jsonPostProcessor.setMatchNumbers("0");
        jsonPostProcessor.setDefaultValues("none");
        jsonPostProcessor.setProperty("Sample.scope","parent");

        List<VariableExtractor> result = new JSONExtractorConverter().apply(jsonPostProcessor,null);
        VariableExtractor variableExtractor = VariableExtractor.builder()
                .description(jsonPostProcessor.getComment())
                .name(jsonPostProcessor.getRefNames())
                .jsonPath(jsonPostProcessor.getJsonPathExpressions())
                .matchNumber(Integer.parseInt(jsonPostProcessor.getMatchNumbers()))
                .getDefault(jsonPostProcessor.getDefaultValues())
                .build();
        List<VariableExtractor> expected = new ArrayList<>();
        expected.add(variableExtractor);
        assertEquals(expected, result);
    }

}