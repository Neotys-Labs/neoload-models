package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class TransactionConverterTest {

    @Before
    public void before() {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testAApplySteps() {

        TransactionControllerConverter testcontrol = new TransactionControllerConverter(new StepConverters());
        TransactionController transactionController = Mockito.mock(TransactionController.class);
        when(transactionController.getName()).thenReturn("my thread group");
        when(transactionController.getComment()).thenReturn("My comment");
        HashTree hashTree = Mockito.mock(HashTree.class);
        ConstantTimer constantTimer = new ConstantTimer();
        List<Object> collections = List.of(constantTimer);
        HashTree subTree = Mockito.mock(HashTree.class);
        when(subTree.list()).thenReturn(collections);
        when(hashTree.get(transactionController)).thenReturn(subTree);
        List<Step> result = testcontrol.apply(transactionController, hashTree);
        List<Step> expected = new ArrayList<>();
        expected.add(Container.builder()
                .addSteps(DelayConstant.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build())
                .name("my thread group")
                .description("My comment")
                .build());
        assertEquals(result, expected);
    }
}
