package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jorphan.collections.HashTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class VariableConvertersTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        ContainerUtils.clearAll();
    }

    @Test
    public void testConvertVariable(){

        HashTree hashTree = new HashTree();
        CSVDataSetConverter csvDataSetConverter = new CSVDataSetConverter();
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setProperty("delimiter", ";");
        csvDataSet.setProperty("filename", "ThomasBestInternShip");
        csvDataSet.setProperty("ignoreFirstLine", true);
        csvDataSet.setProperty("recycle", true);
        csvDataSet.setProperty("shareMode", "shareMode.all");
        csvDataSet.setProperty("stopThread", true);
        csvDataSet.setProperty("TestElement.name", "NeotysBestCompany");
        List<CSVDataSet> list = new ArrayList<>();
        list.add(csvDataSet);
        hashTree.add(list);
        VariableConverters.convertVariable(hashTree,csvDataSet);
        List<Variable> result = ContainerUtils.getVariableContainer();
        List<Variable> expected = csvDataSetConverter.apply(csvDataSet, mock(HashTree.class));
        Assert.assertEquals(result,expected);

    }

}