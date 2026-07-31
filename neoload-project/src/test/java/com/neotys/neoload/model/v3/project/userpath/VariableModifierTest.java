package com.neotys.neoload.model.v3.project.userpath;

import org.junit.Test;

import static org.junit.Assert.*;

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