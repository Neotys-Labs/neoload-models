package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class VariableModifierTest {

    @Test
    public void constants() {
        assertEquals("name", VariableModifier.NAME);
        assertEquals("description", VariableModifier.DESCRIPTION);
        assertEquals("category", VariableModifier.CATEGORY);
        assertEquals("variable_name", VariableModifier.VARIABLE_NAME);
        assertEquals("mode", VariableModifier.MODE);
        assertEquals("value", VariableModifier.VALUE);
    }

}