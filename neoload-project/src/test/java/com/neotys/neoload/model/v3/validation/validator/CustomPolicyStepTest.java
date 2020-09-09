package com.neotys.neoload.model.v3.validation.validator;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.assertj.core.api.Condition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomPolicyStepTest {

    @Test
    public void isValid() {
        final Validator validator = new Validator();
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .value(100)
                .build();
        CustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(300)
                .build();
        Validation validation = validator.validate(customPolicyStep, NeoLoad.class);
        assertTrue(validation.isValid());
    }

    @Test
    public void validateRequiredFields() {
        final Validator validator = new Validator();
        CustomPolicyStep customPolicyStep = CustomPolicyStep.builder().build();
        Validation validation = validator.validate(customPolicyStep, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(string -> string.contains("Violation Number: 2"), "Two failures"))
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'when': missing value or value is empty."),
                        "When is Required")
                )
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'users': missing value or value is empty."),
                        "Users is Required")
                );
    }

    @Test
    public void usersPositiveOrZero() {
        final Validator validator = new Validator();
        CustomPolicyStep customPolicyStep = CustomPolicyStep.builder().users(-1).build();
        Validation validation = validator.validate(customPolicyStep, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'users': must be greater than or equal to 0."),
                        "Users field is a positive number or equal to zero")
                );
    }

    @Test
    public void CustomStepDurationCheckIsExecuted() {
        final Validator validator = new Validator();
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(100)
                .build();
        CustomPolicyStep customPolicyStep = CustomPolicyStep.builder().when(loadDuration).users(0).build();
        Validation validation = validator.validate(customPolicyStep, NeoLoad.class);
        assertFalse(validation.isValid());
        assertThat(validation.getMessage())
                .hasValueSatisfying(new Condition<>(
                        string -> string.contains("Incorrect value for 'when': must be greater than or equal to 0 second or 0 iteration."),
                        "Sub validation on field when are executed")
                );
    }
}
