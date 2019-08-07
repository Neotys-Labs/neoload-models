package com.neotys.neoload.model.readers.jmeter.step;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Delay;
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
    public void testConvertStepWithObject() {
        HashTree hashTree = new HashTree();
        ConstantTimer constantTimer = new ConstantTimer();
        hashTree.add(constantTimer);
        List<Step> result = new StepConverters().convertStep(hashTree);
        List<Step> expected = new ArrayList<>();
        expected.add(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build());
        assertEquals(result,expected);
    }


}