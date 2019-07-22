package com.neotys.neoload.model.readers.jmeter.step.timer;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.timer.UniformerRandomTimerConverter;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.timers.UniformRandomTimer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class UniformerRandomTimerConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCheckDelay(){
        UniformRandomTimer uniformRandomTimer = new UniformRandomTimer();
        uniformRandomTimer.setProperty("ConstantTimer.delay","69");
        uniformRandomTimer.setProperty("RandomTimer.range", "0");
        String result = UniformerRandomTimerConverter.checkDelay(uniformRandomTimer);
        assertEquals(result, "69");
    }

    @Test
    public void testApply(){
        UniformRandomTimer uniformRandomTimer = new UniformRandomTimer();
        uniformRandomTimer.setName("json");
        uniformRandomTimer.setComment("test");
        uniformRandomTimer.setProperty("ConstantTimer.delay","69");
        uniformRandomTimer.setProperty("RandomTimer.range", "0");

        List<Step> result = new UniformerRandomTimerConverter().apply(uniformRandomTimer,null);
        Delay delay = Delay.builder()
                .name(uniformRandomTimer.getName())
                .description(uniformRandomTimer.getComment())
                .value("69")
                .build();
        List<Step> expected = new ArrayList<>();
        expected.add(delay);
        assertEquals(result,expected);
    }

}