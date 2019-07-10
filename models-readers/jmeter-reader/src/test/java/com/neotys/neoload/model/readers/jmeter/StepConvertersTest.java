package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StepConvertersTest {



    @Test
    public void testConvertStepWithoutObject() {
        HashTree hashTree = Mockito.mock(HashTree.class);
        List<Object> stringList = new ArrayList<>();
        stringList.add("lololol");
        when(hashTree.list()).thenReturn(stringList);
        List<Step> convert = new StepConverters(mock(EventListener.class)).convertStep(hashTree);
        assertTrue(convert.isEmpty());

    }

    @Test
    public void testConvertStepWithObject() {
        HashTree hashTree = new HashTree();
        List<Object> objectList = new ArrayList<>();
        objectList.add(new ConstantTimer());
        objectList.add(new TransactionController());
        hashTree.add(objectList);
        List<Step> convert = new StepConverters(mock(EventListener.class)).convertStep(hashTree);
        assertFalse(convert.isEmpty());
    }


}