package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomPolicyStepsSameDurationTypeValidatorTest {

    @Test
    public void nullIsValid() {
        assertTrue(new CustomPolicyStepsSameDurationTypeValidator().isValid(null, null));
    }

    @Test
    public void emptyStepsIsValid() {
        assertTrue(new CustomPolicyStepsSameDurationTypeValidator().isValid(new ArrayList<>(), null));
    }

    @Test
    public void someStepsDurationTypeAreEmptyIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDurationWithoutType = LoadDuration.builder()
                .value(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStepWithoutDurationType = CustomPolicyStep.builder()
                .when(loadDurationWithoutType)
                .users(100)
                .build();
        assertTrue(new CustomPolicyStepsSameDurationTypeValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDurationType), null));
    }

    @Test
    public void someStepsDurationAreEmptyIsValid() {
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
        assertTrue(new CustomPolicyStepsSameDurationTypeValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDuration), null));
    }

    @Test
    public void someStepsDurationTypeAreDifferentFromOthersIsInvalid() {
        ImmutableLoadDuration loadDurationTime = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDurationIteration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.ITERATION)
                .build();
        ImmutableCustomPolicyStep customPolicyStepTime = CustomPolicyStep.builder()
                .when(loadDurationTime)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStepIteration = CustomPolicyStep.builder()
                .when(loadDurationIteration)
                .users(100)
                .build();
        assertFalse(new CustomPolicyStepsSameDurationTypeValidator().isValid(Arrays.asList(customPolicyStepTime, customPolicyStepIteration, customPolicyStepTime), null));
    }

    @Test
    public void isValid() {
        ImmutableLoadDuration loadDurationTime = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStepTime = CustomPolicyStep.builder()
                .when(loadDurationTime)
                .users(100)
                .build();
        assertTrue(new CustomPolicyStepsSameDurationTypeValidator().isValid(Arrays.asList(customPolicyStepTime, customPolicyStepTime, customPolicyStepTime), null));
    }
}
