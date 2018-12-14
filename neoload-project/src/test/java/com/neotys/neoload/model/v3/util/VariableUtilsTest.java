package com.neotys.neoload.model.v3.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VariableUtilsTest {

    @Test
    public void isVariableSyntax() {
        assertTrue(VariableUtils.isVariableSyntax("${variableName}"));
        assertFalse(VariableUtils.isVariableSyntax("variableName}"));
        assertTrue(VariableUtils.isVariableSyntax("${variableName{abc}}"));
    }

    @Test
    public void getVariableName() {
        final String variableName = VariableUtils.getVariableName("${variable_name}");
        assertEquals("variable_name", variableName);

        final String notAVariable = VariableUtils.getVariableName("{variable_name}");
        assertEquals("{variable_name}", notAVariable);

        final String embeddedVariable = VariableUtils.getVariableName("${variableName${abc}}");
        assertEquals("variableName${abc}", embeddedVariable);
    }
}