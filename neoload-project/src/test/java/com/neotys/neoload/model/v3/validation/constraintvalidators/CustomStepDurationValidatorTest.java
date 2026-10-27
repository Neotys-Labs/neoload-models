package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import java.time.Duration;
import org.junit.Test;

public class CustomStepDurationValidatorTest {

    @Test
    public void nullIsValid() {
        assertTrue(new CustomStepDurationValidator().isValid(null, null));
    }

    @Test
    public void negativeDurationValueIsInvalid() {
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(-1)).build();
        assertFalse(new CustomStepDurationValidator().isValid(loadDuration, null));
    }

    @Test
    public void isValid() {
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(0)).build();
        assertTrue(new CustomStepDurationValidator().isValid(loadDuration, null));
    }
}
