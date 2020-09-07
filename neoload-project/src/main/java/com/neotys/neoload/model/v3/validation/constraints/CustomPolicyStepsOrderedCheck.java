package com.neotys.neoload.model.v3.validation.constraints;

import com.neotys.neoload.model.v3.validation.constraintvalidators.CustomPolicyStepsOrderedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = CustomPolicyStepsOrderedValidator.class)
public @interface CustomPolicyStepsOrderedCheck {
    String message() default "{com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsOrderedCheck.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
