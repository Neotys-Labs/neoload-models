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

public class CustomPolicyStepsOrderedValidatorTest {

    @Test
    public void nullIsValid() {
        assertTrue(new CustomPolicyStepsOrderedValidator().isValid(null, null));
    }

    @Test
    public void emptyIsValid() {
        assertTrue(new CustomPolicyStepsOrderedValidator().isValid(Collections.emptyList(), null));
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
        assertTrue(new CustomPolicyStepsOrderedValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDuration), null));
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
        assertTrue(new CustomPolicyStepsOrderedValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStepWithoutDurationValue), null));
    }

    @Test
    public void orderedListIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration2 = LoadDuration.builder()
                .value(200)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration3 = LoadDuration.builder()
                .value(300)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStep2 = CustomPolicyStep.builder()
                .when(loadDuration2)
                .users(200)
                .build();
        ImmutableCustomPolicyStep customPolicyStep3 = CustomPolicyStep.builder()
                .when(loadDuration3)
                .users(300)
                .build();
        assertTrue(new CustomPolicyStepsOrderedValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep2, customPolicyStep3), null));
    }

    @Test
    public void unorderedListIsInvalid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration2 = LoadDuration.builder()
                .value(200)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration3 = LoadDuration.builder()
                .value(300)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableCustomPolicyStep customPolicyStep2 = CustomPolicyStep.builder()
                .when(loadDuration2)
                .users(0)
                .build();
        ImmutableCustomPolicyStep customPolicyStep3 = CustomPolicyStep.builder()
                .when(loadDuration3)
                .users(600)
                .build();
        assertFalse(new CustomPolicyStepsOrderedValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep3, customPolicyStep2), null));
    }

    @Test
    public void orderedListWithSomeIdenticalDurationIsValid() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration2 = LoadDuration.builder()
                .value(200)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration2bis = LoadDuration.builder()
                .value(200)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableLoadDuration loadDuration3 = LoadDuration.builder()
                .value(300)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .build();
        ImmutableCustomPolicyStep customPolicyStep2 = CustomPolicyStep.builder()
                .when(loadDuration2)
                .users(300)
                .build();
        ImmutableCustomPolicyStep customPolicyStep2bis = CustomPolicyStep.builder()
                .when(loadDuration2bis)
                .users(200)
                .build();
        ImmutableCustomPolicyStep customPolicyStep3 = CustomPolicyStep.builder()
                .when(loadDuration3)
                .build();
        assertTrue(new CustomPolicyStepsOrderedValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep2, customPolicyStep2bis, customPolicyStep3), null));
    }
}
