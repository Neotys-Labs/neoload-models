package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.neotys.neoload.model.v3.validation.constraintvalidators.RequiredValidator;


public class RequiredValidatorTest {
	@Test
	public void isValid() {
		assertFalse(new RequiredValidator().isValid(null, null));
		
		assertFalse(new RequiredValidator().isValid("", null));
		assertFalse(new RequiredValidator().isValid(" \t\r\n", null));
		assertTrue(new RequiredValidator().isValid("value", null));
		
		assertFalse(new RequiredValidator().isValid(new ArrayList<>(), null));
		assertTrue(new RequiredValidator().isValid(Arrays.asList("value"), null));
		
		assertFalse(new RequiredValidator().isValid(new HashMap<>(), null));
		final Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		assertTrue(new RequiredValidator().isValid(map, null));
	}
}
