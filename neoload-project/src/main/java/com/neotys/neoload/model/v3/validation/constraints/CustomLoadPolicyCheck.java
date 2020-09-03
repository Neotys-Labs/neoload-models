package com.neotys.neoload.model.v3.validation.constraints;

import com.neotys.neoload.model.v3.validation.constraintvalidators.CustomPolicyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CustomPolicyValidator.class)
public @interface CustomLoadPolicyCheck {
    String message() default "{com.neotys.neoload.model.v3.validation.constraints.CustomLoadPolicyCheck.duration.type.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
