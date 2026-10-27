package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.TimeLoadDurationWholeSecondsValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.WholeSecondsValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * A second-based duration cannot carry a milliseconds part, which only the public builders can produce. On a time load duration, the violation is reported on the field holding it.
 */
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { WholeSecondsValidator.class, TimeLoadDurationWholeSecondsValidator.class })
public @interface WholeSecondsCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
