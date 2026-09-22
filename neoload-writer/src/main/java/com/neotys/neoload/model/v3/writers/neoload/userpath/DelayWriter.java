package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Delay;

public class DelayWriter extends AbstractDelayActionWriter {
    public static DelayWriter of(final Delay delay) {
        return new DelayWriter(delay);
    }

    public DelayWriter(Delay delay) {
        super(delay, delay.getValue(), false);
    }
}
