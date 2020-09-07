package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomPolicyValidatorTest {

    @Test
    public void nullIsValid() {
        assertTrue(new CustomPolicyValidator().isValid(null, null));
    }

    @Test
    public void nullStepsIsValid() {
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder().build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }

    @Test
    public void noDurationTypeIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .duration(loadDuration)
                .build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }

    @Test
    public void emptyStepsIsValid() {
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder().steps(new ArrayList<>()).build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }

    @Test
    public void stepsDurationTypeIsIdenticalToPolicyDurationTypeIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .duration(loadDuration)
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep))
                .build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }

    @Test
    public void stepsDurationType_TIME_WithEmptyPolicyDurationIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep))
                .build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }

    @Test
    public void stepsDurationType_ITERATION_WithEmptyPolicyDurationIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.ITERATION)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep))
                .build();
        assertFalse(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }

    @Test
    public void stepsDurationTypeIsDifferentToPolicyDurationTypeIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDurationStep = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.ITERATION)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDurationStep)
                .users(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .duration(loadDuration)
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep))
                .build();
        assertFalse(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
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
                .when(loadDurationIteration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStepIteration = CustomPolicyStep.builder()
                .when(loadDurationTime)
                .users(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .duration(loadDurationTime)
                .steps(Arrays.asList(customPolicyStepTime, customPolicyStepIteration, customPolicyStepTime))
                .build();
        assertFalse(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
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
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .duration(loadDuration)
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDuration))
                .build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
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
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .duration(loadDuration)
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDurationType))
                .build();
        assertTrue(new CustomPolicyValidator().isValid(immutableCustomLoadPolicy, null));
    }


}
