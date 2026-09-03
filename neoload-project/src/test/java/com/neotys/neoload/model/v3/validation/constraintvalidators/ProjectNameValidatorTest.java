package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Strings;
import java.util.Optional;
import org.junit.Test;

public class ProjectNameValidatorTest {
	@Test
	public void isValid_absentIsValid() {
		assertTrue(new ProjectNameValidator().isValid(null, null));
		assertTrue(new ProjectNameValidator().isValid(Optional.empty(), null));
	}

	@Test
	public void isValid_identifierCharsAccepted() {
		assertTrue(new ProjectNameValidator().isValid(Optional.of("myProject123"), null));
		assertTrue(new ProjectNameValidator().isValid(Optional.of("my$project"), null));
		assertTrue(new ProjectNameValidator().isValid(Optional.of("my_project"), null));
	}

	@Test
	public void isValid_nonIdentifierCharsRejected() {
		assertFalse(new ProjectNameValidator().isValid(Optional.of("my project"), null));
		assertFalse(new ProjectNameValidator().isValid(Optional.of("my-project"), null));
		assertFalse(new ProjectNameValidator().isValid(Optional.of("my@project"), null));
	}

	@Test
	public void isValid_lengthBoundaries() {
		assertTrue(new ProjectNameValidator().isValid(Optional.of(Strings.repeat("a", 100)), null));
		assertFalse(new ProjectNameValidator().isValid(Optional.of(Strings.repeat("a", 101)), null));
	}
}
