package com.neotys.neoload.model.v3.validation.validator;

import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.assertj.core.api.Condition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomLoadPolicyTest {

    @Test
    public void validateRequiredFields() {
        final Validator validator = new Validator();
        CustomLoadPolicy customLoadPolicy = CustomLoadPolicy.builder().build();
        Validation validation = validator.validate(customLoadPolicy, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(string -> string.contains("Violation Number: 1"), "Only one failure"))
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'steps': missing value or value is empty."),
                        "Step is Required")
                );
    }

    @Test
    public void timeDurationIsValid() {
        final Validator validator = new Validator();
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
        Validation validation = validator.validate(immutableCustomLoadPolicy, NeoLoad.class);
        assertTrue(validation.isValid());
    }

    @Test
    public void iterationDurationIsValid() {
        final Validator validator = new Validator();
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
        Validation validation = validator.validate(immutableCustomLoadPolicy, NeoLoad.class);
        assertTrue(validation.isValid());
    }

    @Test
    public void customPolicyStepsSameDurationTypeCheckIsExecuted() {
        final Validator validator = new Validator();
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.ITERATION)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(100)
                .build();
        ImmutableLoadDuration loadDurationTime = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStepTime = CustomPolicyStep.builder()
                .when(loadDurationTime)
                .users(100)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStepTime, customPolicyStep))
                .build();
        Validation validation = validator.validate(immutableCustomLoadPolicy, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(string -> string.contains("Violation Number: 1"), "Only one failure"))
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'steps': all durations must be of the same type (time or iteration)."),
                        "Custom policy validation is executed")
                );
    }

    @Test
    public void customPolicyStepsOrderedCheckIsExecuted() {
        final Validator validator = new Validator();
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
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStep3, customPolicyStep2))
                .build();
        Validation validation = validator.validate(immutableCustomLoadPolicy, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(string -> string.contains("Violation Number: 1"), "Only one failure"))
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'steps': all steps must be ordered chronologically"),
                        "Custom policy steps ordering validation is executed")
                );
    }

    @Test
    public void customPolicyStepsAtLeastOneGreaterThanZeroDurationCheckIsExecuted() {
        final Validator validator = new Validator();
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(0)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(0)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep))
                .build();
        Validation validation = validator.validate(immutableCustomLoadPolicy, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(string -> string.contains("Violation Number: 2"), "Only two failures"))
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'duration': must be greater than or equal to 1 second or 1 iteration."),
                        "If it no longer works, you can remove this assert, it is a deprecated validation for custom policy")
                )
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'steps': at least one step must be greater or equal to 1 second or 1 iteration."),
                        "We need at least 1 step with duration greater than 0")
                );
    }

    @Test
    public void stepsValidationsAreExecuted() {
        final Validator validator = new Validator();
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep))
                .build();
        Validation validation = validator.validate(immutableCustomLoadPolicy, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(string -> string.contains("Violation Number: 1"), "Only one failure"))
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'steps[0].users': missing value or value is empty."),
                        "Steps sub validation are executed")
                );
    }

    @Test
    public void durationIsGenerated() {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .build();
        ImmutableLoadDuration loadDuration2 = LoadDuration.builder()
                .value(200)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep2 = CustomPolicyStep.builder()
                .when(loadDuration2)
                .build();
        ImmutableCustomLoadPolicy immutableCustomLoadPolicy = CustomLoadPolicy.builder()
                .steps(Arrays.asList(customPolicyStep, customPolicyStep, customPolicyStep2))
                .build();
        Assert.assertEquals(immutableCustomLoadPolicy.getDuration(), loadDuration2);
    }
}
