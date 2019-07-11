package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StepConvertersTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }


    @Test
    public void testConvertStepWithoutObject() {
        HashTree hashTree = Mockito.mock(HashTree.class);
        List<Object> stringList = new ArrayList<>();
        stringList.add("lololol");
        when(hashTree.list()).thenReturn(stringList);
        List<Step> convert = new StepConverters().convertStep(hashTree);
        assertTrue(convert.isEmpty());

    }

    @Test
    public void testConvertStepWithObject() {
        HashTree hashTree = new HashTree();
        List<Object> objectList = new ArrayList<>();
        objectList.add(new ConstantTimer());
        objectList.add(new TransactionController());
        hashTree.add(objectList);
        List<Step> convert = new StepConverters().convertStep(hashTree);
        assertFalse(convert.isEmpty());
    }


}