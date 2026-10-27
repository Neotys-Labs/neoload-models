package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.IterationLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

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
    public void someStepsDurationAreEmptyIsValid() {
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
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
        LoadDuration loadDurationTime = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
        LoadDuration loadDurationIteration = IterationLoadDuration.builder().count(100).build();
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
        LoadDuration loadDurationTime = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
        ImmutableCustomPolicyStep customPolicyStepTime = CustomPolicyStep.builder()
                .when(loadDurationTime)
                .users(100)
                .build();
        assertTrue(new CustomPolicyStepsSameDurationTypeValidator().isValid(Arrays.asList(customPolicyStepTime, customPolicyStepTime, customPolicyStepTime), null));
    }
}
