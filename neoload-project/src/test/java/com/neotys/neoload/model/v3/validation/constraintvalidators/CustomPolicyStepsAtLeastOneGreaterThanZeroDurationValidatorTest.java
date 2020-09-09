package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidatorTest {

    @Test
    public void nullIsValid() {
        assertTrue(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(null, null));
    }

    @Test
    public void emptyIsValid() {
        assertTrue(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(Collections.emptyList(), null));
    }

    @Test
    public void stepsWithoutDurationIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStepWithoutDuration = CustomPolicyStep.builder()
                .users(100)
                .build();
        assertTrue(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDuration), null));
    }

    @Test
    public void stepsWithoutDurationValueIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDurationWithoutValue = LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStepWithoutDurationValue = CustomPolicyStep.builder()
                .when(loadDurationWithoutValue)
                .users(100)
                .build();
        assertTrue(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDurationValue), null));
    }

    @Test
    public void stepsWithoutDurationValueGreaterThanZeroIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(0)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        assertFalse(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep), null));
    }

    @Test
    public void isValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration2 = LoadDuration.builder()
                .value(0)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStep2 = CustomPolicyStep.builder()
                .when(loadDuration2)
                .users(100)
                .build();
        assertTrue(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep2), null));
    }
}
