package com.neotys.neoload.model.v3.project.userpath;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwitchTest {

    @Test
    public void testFlattenSwitchWithoutDefault(){
        Switch aSwitch = Switch.builder()
                .name("Switcher3")
                .description("Hunt or be hunted")
                .value("Geralt")
                .addCases(Case.builder()
                        .value("Geralt")
                        .isBreak(true)
                        .addSteps(DelayConstant.builder().value("500").build(), DelayConstant.builder().value("2500").build())
                        .build())
                .build();
        assertEquals(4, aSwitch.flattened().count());
    }

    @Test
    public void testFlattenSwitchWithDefault(){
        Switch aSwitch = Switch.builder()
                .name("Switcher3")
                .description("Hunt or be hunted")
                .value("Geralt")
                .addCases(Case.builder()
                        .value("Geralt")
                        .isBreak(true)
                        .addSteps(DelayConstant.builder().value("500").build(), DelayConstant.builder().value("2500").build())
                        .build())
                .getDefault(Container.builder()
                        .addSteps(DelayConstant.builder().value("5000").build())
                        .build())
                .build();
        assertEquals(6, aSwitch.flattened().count());
    }
}
