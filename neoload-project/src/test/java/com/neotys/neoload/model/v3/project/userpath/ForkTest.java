package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class ForkTest {
    @Test
    public void constants() {
        assertEquals("name", Fork.NAME);
        assertEquals("description", Fork.DESCRIPTION);
        assertEquals("steps", Fork.STEPS);
    }
}