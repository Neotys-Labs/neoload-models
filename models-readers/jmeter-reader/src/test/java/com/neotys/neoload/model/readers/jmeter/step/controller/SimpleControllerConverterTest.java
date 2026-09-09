package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SimpleControllerConverterTest {

    private TestEventListener spy;

    @Before
    public void before() {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }


    @Test
    public void testAApplySteps() {

        SimpleControllerConverter testcontrol = new SimpleControllerConverter(new StepConverters());
        GenericController genericController = Mockito.mock(GenericController.class);
        when(genericController.getName()).thenReturn("my thread group");
        when(genericController.getComment()).thenReturn("My comment");
        HashTree hashTree = Mockito.mock(HashTree.class);
        ConstantTimer constantTimer = new ConstantTimer();
        List<Object> collections = List.of(constantTimer);
        HashTree subTree = Mockito.mock(HashTree.class);
        when(subTree.list()).thenReturn(collections);
        when(hashTree.get(genericController)).thenReturn(subTree);
        List<Step> result = testcontrol.apply(genericController, hashTree);
        List<Step> expected = new ArrayList<>();
        expected.add(Container.builder()
                .addSteps(DelayConstant.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build())
                .name("my thread group")
                .description("My comment")
                .build());
        assertEquals(result, expected);
    }
}