package com.neotys.neoload.model.validation.validator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ValidatorTest {
	@Test
	public void normalizePath() {
		assertEquals("N/A", new Validator().normalizePath(null));
	}	
}
