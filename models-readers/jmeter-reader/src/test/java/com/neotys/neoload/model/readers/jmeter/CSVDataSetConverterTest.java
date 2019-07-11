package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CSVDataSetConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testApplyWhithShareAllAndOutofValueCycle() {
        CSVDataSetConverter csvDataSetConverter = new CSVDataSetConverter();
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setProperty("delimiter", ";");
        csvDataSet.setProperty("filename", "ThomasBestInternShip");
        csvDataSet.setProperty("ignoreFirstLine", true);
        csvDataSet.setProperty("recycle", true);
        csvDataSet.setProperty("shareMode", "shareMode.all");
        csvDataSet.setProperty("stopThread", true);
        csvDataSet.setProperty("TestElement.name", "NeotysBestCompany");
        List<Variable> result = csvDataSetConverter.apply(csvDataSet, mock(HashTree.class));

        Variable variable = FileVariable.builder()
                .name("NeotysBestCompany")
                .path("ThomasBestInternShip")
                .isFirstLineColumnNames(true)
                .delimiter(";")
                .scope(FileVariable.Scope.GLOBAL)
                .outOfValue(FileVariable.OutOfValue.CYCLE)
                .build();
        List<Variable> expected = new ArrayList<>();
        expected.add(variable);
        assertEquals(result, expected);

    }

    @Test
    public void testApplyWhithShareGroupAndOutofValueStop() {
        CSVDataSetConverter csvDataSetConverter = new CSVDataSetConverter();
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setProperty("delimiter", ";");
        csvDataSet.setProperty("filename", "ThomasBestInternShip");
        csvDataSet.setProperty("ignoreFirstLine", true);
        csvDataSet.setProperty("recycle", false);
        csvDataSet.setProperty("shareMode", "shareMode.group");
        csvDataSet.setProperty("stopThread", true);
        csvDataSet.setProperty("TestElement.name", "NeotysBestCompany");
        List<Variable> result = csvDataSetConverter.apply(csvDataSet, mock(HashTree.class));

        Variable variable = FileVariable.builder()
                .name("NeotysBestCompany")
                .path("ThomasBestInternShip")
                .isFirstLineColumnNames(true)
                .delimiter(";")
                .scope(FileVariable.Scope.GLOBAL)
                .outOfValue(FileVariable.OutOfValue.STOP)
                .build();
        List<Variable> expected = new ArrayList<>();
        expected.add(variable);
        assertEquals(result, expected);
    }

    @Test
    public void testApplyWhithShareThreadAndOutofValueCycle(){
        CSVDataSetConverter csvDataSetConverter = new CSVDataSetConverter();
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setProperty("delimiter",";");
        csvDataSet.setProperty("filename","ThomasBestInternShip");
        csvDataSet.setProperty("ignoreFirstLine",true);
        csvDataSet.setProperty("recycle",false);
        csvDataSet.setProperty("shareMode","shareMode.thread");
        csvDataSet.setProperty("stopThread",false);
        csvDataSet.setProperty("TestElement.name","NeotysBestCompany");
        List<Variable> result = csvDataSetConverter.apply(csvDataSet,mock(HashTree.class));

        Variable variable = FileVariable.builder()
                .name("NeotysBestCompany")
                .path("ThomasBestInternShip")
                .isFirstLineColumnNames(true)
                .delimiter(";")
                .scope(FileVariable.Scope.LOCAL)
                .outOfValue(FileVariable.OutOfValue.CYCLE)
                .build();
        List<Variable> expected = new ArrayList<>();
        expected.add(variable);
        assertEquals(result,expected);

    }

    @Test
    public void testApplyUnsupportedParameter(){
        CSVDataSetConverter csvDataSetConverter = new CSVDataSetConverter();
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setProperty("delimiter",";");
        csvDataSet.setProperty("filename","ThomasBestInternShip");
        csvDataSet.setProperty("ignoreFirstLine",true);
        csvDataSet.setProperty("recycle",false);
        csvDataSet.setProperty("shareMode","shareMode.Identifier");
        csvDataSet.setProperty("stopThread",false);
        csvDataSet.setProperty("TestElement.name","NeotysBestCompany");
        csvDataSetConverter.apply(csvDataSet,mock(HashTree.class));

        verify(spy,times(1)).readUnsupportedParameter("CSVDataSet", "String ", "ShareMode Parameter");




    }
}