package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
                        .addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("500")).build()).build(), Delay.builder().duration(DurationConstant.builder().value(Durations.of("2500")).build()).build())
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
                        .addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("500")).build()).build(), Delay.builder().duration(DurationConstant.builder().value(Durations.of("2500")).build()).build())
                        .build())
                .getDefault(Container.builder()
                        .addSteps(Delay.builder().duration(DurationConstant.builder().value(Durations.of("5000")).build()).build())
                        .build())
                .build();
        assertEquals(6, aSwitch.flattened().count());
    }
}
