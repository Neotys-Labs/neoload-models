package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class VariableUtilsTest {
	@Test
	public void getVariableSyntax() {
		boolean throwException = false;
		try {
			VariableUtils.getVariableSyntax(null);
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}
		throwException = false;
		try {
			VariableUtils.getVariableSyntax("");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}
		
		throwException = false;
		try {
			VariableUtils.getVariableSyntax(" \t \r\n ");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be blank."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be blank.");
		}

		assertEquals("${var_name}", VariableUtils.getVariableSyntax("${var_name}"));

		assertEquals("${var_name}", VariableUtils.getVariableSyntax("var_name"));
		
		assertEquals("${${var_name}", VariableUtils.getVariableSyntax("${var_name"));
		assertEquals("${var_name}}", VariableUtils.getVariableSyntax("var_name}"));
	}
	
	@Test
	public void getVariableName() {
		assertNull(VariableUtils.getVariableName(null));
		assertNull(VariableUtils.getVariableName(""));
		assertNull(VariableUtils.getVariableName(" \t \r\n "));
		
		assertNull(VariableUtils.getVariableName("var_name"));
		
		assertNull(VariableUtils.getVariableName("${var_name"));
		assertNull(VariableUtils.getVariableName("var_name}"));
				
		assertEquals("var_name", VariableUtils.getVariableName("${var_name}"));
		assertEquals("var_name", VariableUtils.getVariableName(" \t \r\n ${ \t \r\n var_name \t \r\n } \t \r\n "));
		
		assertEquals("var_name{abc}", VariableUtils.getVariableName("${var_name{abc}}"));
	}

	@Test
	public void isVariableSyntax() {
		assertFalse(VariableUtils.isVariableSyntax(null));
		assertFalse(VariableUtils.isVariableSyntax(""));
		assertFalse(VariableUtils.isVariableSyntax(" \t \r\n "));
		
		assertFalse(VariableUtils.isVariableSyntax("var_name"));
		assertFalse(VariableUtils.isVariableSyntax("__encodeURL(${var_name})"));
		
		assertFalse(VariableUtils.isVariableSyntax("${var_name"));
		assertFalse(VariableUtils.isVariableSyntax("var_name}"));
		
		assertTrue(VariableUtils.isVariableSyntax("${var_name}"));
		assertTrue(VariableUtils.isVariableSyntax(" \t \r\n ${ \t \r\n var_name \t \r\n } \t \r\n "));
		
		assertTrue(VariableUtils.isVariableSyntax("${var_name{abc}}"));
	}

}