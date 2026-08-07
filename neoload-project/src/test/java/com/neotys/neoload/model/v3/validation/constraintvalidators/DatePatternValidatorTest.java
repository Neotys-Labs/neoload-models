package com.neotys.neoload.model.v3.validation.constraintvalidators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DatePatternValidatorTest {

    private final DatePatternValidator validator = new DatePatternValidator();

    // --- null / blank: always valid (optional field, absence means use default) ---

    @Test
    public void nullIsValid() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    public void emptyStringIsValid() {
        assertTrue(validator.isValid("", null));
    }

    @Test
    public void blankStringIsValid() {
        assertTrue(validator.isValid("   ", null));
    }

    // --- special patterns from CurrentSimpleDateFormat ---

    @Test
    public void millisecondsSinceEpochPatternIsValid() {
        assertTrue(validator.isValid("milliseconds since the UNIX epoch", null));
    }

    @Test
    public void legacyCurrentTimeMillisPatternIsValid() {
        assertTrue(validator.isValid("currentTimeMillis", null));
    }

    // --- valid SimpleDateFormat patterns ---

    @Test
    public void defaultPatternIsValid() {
        assertTrue(validator.isValid("dd/MM/yyyy HH:mm:ss", null));
    }

    @Test
    public void isoDatePatternIsValid() {
        assertTrue(validator.isValid("yyyy-MM-dd", null));
    }

    @Test
    public void isoDateTimePatternWithLiteralIsValid() {
        assertTrue(validator.isValid("yyyy-MM-dd'T'HH:mm:ss", null));
    }

    @Test
    public void timestampPatternIsValid() {
        assertTrue(validator.isValid("yyyyMMddHHmmss", null));
    }

    // --- invalid SimpleDateFormat patterns ---

    @Test
    public void unterminatedLiteralIsInvalid() {
        // Single quote opens a literal section but is never closed
        assertFalse(validator.isValid("yyyy-MM-dd'", null));
    }

    @Test
    public void tooManyRepetitionsForTimezonePatternIsInvalid() {
        // X is a recognized SimpleDateFormat letter for ISO 8601 timezone offset, but is only valid with 1-3 repetitions: X=±HH, XX=±HHmm, XXX=±HH:mm
        assertFalse(validator.isValid("XXXX", null));
    }
}
