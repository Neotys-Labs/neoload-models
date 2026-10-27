package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

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
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
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
    public void orderedListIsValid() {
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
        LoadDuration loadDuration2 = TimeLoadDuration.builder().value(Duration.ofSeconds(200)).build();
        LoadDuration loadDuration3 = TimeLoadDuration.builder().value(Duration.ofSeconds(300)).build();
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
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
        LoadDuration loadDuration2 = TimeLoadDuration.builder().value(Duration.ofSeconds(200)).build();
        LoadDuration loadDuration3 = TimeLoadDuration.builder().value(Duration.ofSeconds(300)).build();
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
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
        LoadDuration loadDuration2 = TimeLoadDuration.builder().value(Duration.ofSeconds(200)).build();
        LoadDuration loadDuration2bis = TimeLoadDuration.builder().value(Duration.ofSeconds(200)).build();
        LoadDuration loadDuration3 = TimeLoadDuration.builder().value(Duration.ofSeconds(300)).build();
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
