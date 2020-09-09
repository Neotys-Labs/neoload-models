package com.neotys.neoload.model.v3.validation.constraints;

import com.neotys.neoload.model.v3.validation.constraintvalidators.CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator.class)
public @interface CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck {
    String message() default "{com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
