package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomStepDurationValidatorTest {

    @Test
    public void nullIsValid() {
        assertTrue(new CustomStepDurationValidator().isValid(null, null));
    }

    @Test
    public void emptyDurationValueIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .build();
        assertFalse(new CustomStepDurationValidator().isValid(loadDuration, null));
    }

    @Test
    public void emptyDurationTypeIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .build();
        assertFalse(new CustomStepDurationValidator().isValid(loadDuration, null));
    }

    @Test
    public void negativeDurationValueIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(-1)
                .type(LoadDuration.Type.TIME)
                .build();
        assertFalse(new CustomStepDurationValidator().isValid(loadDuration, null));
    }

    @Test
    public void isValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(0)
                .type(LoadDuration.Type.TIME)
                .build();
        assertTrue(new CustomStepDurationValidator().isValid(loadDuration, null));
    }
}
