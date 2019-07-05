package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.CmdEventListener;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.timers.ConstantTimer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConstantTimerConverterTest {

    @Test
    public void testApply(){
        ConstantTimer constantTimer = new ConstantTimer();
        constantTimer.setDelay("25");
        constantTimer.setName("Test");
        ConstantTimerConverter constantTimerConverter = new ConstantTimerConverter(new CmdEventListener(null,null,null));
        List<Step> teststep = constantTimerConverter.apply(constantTimer, null);
        assertEquals(teststep.size(),1);
        System.out.println(teststep);

    }

}