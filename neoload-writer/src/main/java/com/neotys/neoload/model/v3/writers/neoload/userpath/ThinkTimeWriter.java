package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.ThinkTime;

public class ThinkTimeWriter extends AbstractDelayActionWriter {

    public ThinkTimeWriter(ThinkTime thinktime) {
        super(thinktime,thinktime.getValue(),true);
    }

    public static ThinkTimeWriter of(final ThinkTime thinktime) {
        return new ThinkTimeWriter(thinktime);
    }

}
