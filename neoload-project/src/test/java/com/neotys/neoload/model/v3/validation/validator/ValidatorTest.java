package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.validation.validator.Validator;


public class ValidatorTest {
	@Test
	public void normalizePath() {
		assertEquals("N/A", new Validator().normalizePath(null));
	}	
}
