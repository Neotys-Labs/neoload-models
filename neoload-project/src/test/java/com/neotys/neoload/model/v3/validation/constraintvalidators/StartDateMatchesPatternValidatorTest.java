package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.DateVariable;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StartDateMatchesPatternValidatorTest {

    private final StartDateMatchesPatternValidator validator = new StartDateMatchesPatternValidator();

    private DateVariable createDateVariable(final String pattern, final String startDate) {
        return DateVariable.builder().name("test").pattern(pattern).startDate(startDate).build();
    }

    // --- null start_date: deferred to @RequiredCheck, validator returns true ---

    @Test
    public void nullStartDateIsNotValidatedHere() {
        assertTrue(validator.isValid(createDateVariable("yyyy-MM-dd", null), null));
    }

    // --- empty pattern: falls back to DEFAULT_PATTERN ---

    @Test
    public void emptyPatternFallsBackToDefaultPattern() {
        assertTrue(validator.isValid(createDateVariable("", "15/01/2026 10:30:00"), null));
    }

    @Test
    public void emptyPatternWithDateNotMatchingDefaultPatternIsInvalid() {
        assertFalse(validator.isValid(createDateVariable("", "2026-01-15"), null));
    }

    // --- valid dates ---

    @Test
    public void startDateMatchingPatternIsValid() {
        assertTrue(validator.isValid(createDateVariable("yyyy-MM-dd", "2026-01-15"), null));
    }

    @Test
    public void startDateWithTimeMatchingPatternIsValid() {
        assertTrue(validator.isValid(createDateVariable("dd/MM/yyyy HH:mm:ss", "15/01/2026 10:30:00"), null));
    }

    @Test
    public void millisecondsSinceEpochPatternWithLongValueIsValid() {
        assertTrue(validator.isValid(createDateVariable("milliseconds since the UNIX epoch", "1736942400000"), null));
    }

    @Test
    public void legacyCurrentTimeMillisPatternWithLongValueIsValid() {
        assertTrue(validator.isValid(createDateVariable("currentTimeMillis", "1736942400000"), null));
    }

    // --- invalid dates ---

    @Test
    public void startDateNotMatchingPatternIsInvalid() {
        assertFalse(validator.isValid(createDateVariable("yyyy-MM-dd", "15/01/2026"), null));
    }

    @Test
    public void startDateWithInvalidDayIsInvalid() {
        // month 13 does not exist — lenient mode must be disabled
        assertFalse(validator.isValid(createDateVariable("yyyy-MM-dd", "2026-13-01"), null));
    }

    @Test
    public void startDateWithTrailingTextIsInvalid() {
        // trailing characters after a valid date must be rejected
        assertFalse(validator.isValid(createDateVariable("yyyy-MM-dd", "2026-01-15T00:00:00"), null));
    }

    @Test
    public void millisecondsSinceEpochPatternWithNonLongValueIsInvalid() {
        assertFalse(validator.isValid(createDateVariable("milliseconds since the UNIX epoch", "not-a-number"), null));
    }

    // --- invalid pattern: @DatePatternCheck responsibility, skip start_date validation ---

    @Test
    public void invalidPatternSkipsStartDateValidation() {
        assertTrue(validator.isValid(createDateVariable("yyyy-MM-dd'", "2026-01-15"), null));
    }
}
