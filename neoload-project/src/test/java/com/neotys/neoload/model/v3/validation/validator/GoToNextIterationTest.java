package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.GoToNextIteration;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Before;
import org.junit.Test;

public class GoToNextIterationTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String CONSTRAINTS_NAME_REQUIRED = "Data Model is invalid. Violation Number: 1." + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'name': missing value or value is empty." + LINE_SEPARATOR;

    Validator validator;
    
    @Before
    public void setUp() {
        validator = new Validator();
    }

    // GoToNextIteration is a special case that serializes as a bare scalar. name is never exposed in YAML/JSON so this
    // test covers the normal deserialization case.
    @Test
    public void validateName_noName() {
        //ACT:
        GoToNextIteration goToNextIteration = GoToNextIteration.builder().build();
        //ASSERT:
        Validation validation = validator.validate(goToNextIteration, NeoLoad.class);
        assertTrue(validation.isValid());
        assertFalse(validation.getMessage().isPresent());
    }

    // GoToNextIteration is a special case that serializes as a bare scalar. name is never exposed in YAML/JSON
    // so builder.name("something") is an artificial scenario that can never happen via deserialization but we keep
    // the test
    @Test
    public void validateName_emptyStringAsName() {
        //ACT:
        GoToNextIteration goToNextIteration = GoToNextIteration.builder().name("").build();
        //ASSERT:
        Validation validation = validator.validate(goToNextIteration, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_NAME_REQUIRED, validation.getMessage().get());
    }

    // Same rationale as validateName_emptyStringAsName — this cannot reach the validator in production.
    @Test
    public void validateName_blankCharactersAsName() {
        //ACT:
        GoToNextIteration goToNextIteration = GoToNextIteration.builder().name(" \t\r\n").build();
        //ASSERT:
        Validation validation = validator.validate(goToNextIteration, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_NAME_REQUIRED, validation.getMessage().get());
    }

    // Same rationale as validateName_emptyStringAsName — this cannot reach the validator in production.
    @Test
    public void validateName_validStringAsName() {
        //ACT:
        GoToNextIteration goToNextIteration = GoToNextIteration.builder().name("validName").build();
        //ASSERT:
        Validation validation = validator.validate(goToNextIteration, NeoLoad.class);
        assertTrue(validation.isValid());
        assertFalse(validation.getMessage().isPresent());
    }
}
