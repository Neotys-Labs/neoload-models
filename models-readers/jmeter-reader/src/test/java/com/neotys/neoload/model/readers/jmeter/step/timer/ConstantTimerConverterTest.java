package com.neotys.neoload.model.readers.jmeter.step.timer;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.timer.ConstantTimerConverter;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.timers.ConstantTimer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class ConstantTimerConverterTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testApply(){
        ConstantTimer constantTimer = new ConstantTimer();
        constantTimer.setDelay("25");
        constantTimer.setName("Test");
        ConstantTimerConverter constantTimerConverter = new ConstantTimerConverter();
        List<Step> result = constantTimerConverter.apply(constantTimer, null);
        List<Step> expected = new ArrayList<>();
        expected.add(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build());
        assertEquals(result,expected);

    }

}