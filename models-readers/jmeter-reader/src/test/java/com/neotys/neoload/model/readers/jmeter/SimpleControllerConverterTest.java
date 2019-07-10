package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.CmdEventListener;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class SimpleControllerConverterTest {

    @Test
    public void testApplyNoSteps() {
        CmdEventListener eventListener = new CmdEventListener("", "", "");
        SimpleControllerConverter testcontrol = new SimpleControllerConverter(new StepConverters(eventListener)
                , eventListener);

        GenericController simpleController = Mockito.mock(GenericController.class);
        when(simpleController.getName()).thenReturn("my thread group");
        when(simpleController.getComment()).thenReturn("My comment");
        HashTree hashTree = Mockito.mock(HashTree.class);

        HashTree subTree = Mockito.mock(HashTree.class);
        when(subTree.list()).thenReturn(Collections.emptyList());
        when(hashTree.get(eq(simpleController))).thenReturn(subTree);
        List<Step> teststep = testcontrol.apply(simpleController,hashTree);
        assertEquals(teststep.size(), 1);
        assertEquals(teststep.get(0).getName(), "my thread group");
    }

    @Test
    public void testAApplySteps() {
        CmdEventListener eventListener = new CmdEventListener("", "", "");
        SimpleControllerConverter testcontrol = new SimpleControllerConverter(new StepConverters(eventListener)
                , eventListener);
        GenericController transactionController = Mockito.mock(TransactionController.class);
        when(transactionController.getName()).thenReturn("my thread group");
        when(transactionController.getComment()).thenReturn("My comment");
        HashTree hashTree = Mockito.mock(HashTree.class);
        ConstantTimer constantTimer = new ConstantTimer();
        List collections = new ArrayList();
        collections.add(constantTimer);
        collections.add(constantTimer);
        HashTree subTree = Mockito.mock(HashTree.class);
        when(subTree.list()).thenReturn(collections);
        when(hashTree.get(eq(transactionController))).thenReturn(subTree);
        List<Step> teststep = testcontrol.apply(transactionController, hashTree);
        assertEquals(teststep.size(), 1);
        assertEquals(teststep.get(0).getName(), "my thread group");
        int result = (int) teststep.stream().flatMap(Element::flattened)
                .filter(s -> !(s instanceof Container))
                .count();
        assertEquals(result, collections.size());
        teststep.stream()
                .filter(s -> s instanceof Container)
                .flatMap(c -> ((Container) c).getSteps().stream());


    }

}