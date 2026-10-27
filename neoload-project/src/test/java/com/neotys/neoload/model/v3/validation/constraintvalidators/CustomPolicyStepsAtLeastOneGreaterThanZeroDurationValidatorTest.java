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
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
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
    public void stepsWithoutDurationValueGreaterThanZeroIsInvalid() {
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(0)).build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        assertFalse(new CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator().isValid(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep), null));
    }

    @Test
    public void isValid() {
        LoadDuration loadDuration = TimeLoadDuration.builder().value(Duration.ofSeconds(100)).build();
        LoadDuration loadDuration2 = TimeLoadDuration.builder().value(Duration.ofSeconds(0)).build();
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
