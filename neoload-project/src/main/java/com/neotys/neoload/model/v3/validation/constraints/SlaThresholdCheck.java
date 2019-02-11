package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.v3.validation.constraintvalidators.SlaThresholdValidator;



@Target({ TYPE_USE })
@Retention(RUNTIME)
@Repeatable(SlaThresholdCheck.List.class)
@Constraint(validatedBy = { SlaThresholdValidator.class })
@ReportAsSingleViolation
public @interface SlaThresholdCheck {
	UsageType usage();
	
	String message();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Defines several {@code @ThresholdCheck} annotations on the same element.
	 */
	@Target({ TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		SlaThresholdCheck[] value();
	}
	
	enum UsageType {
		CHECK_RELATIONSHIP_KPI_AND_SCOPE,
		CHECK_UNIQUE_CONDITION_SEVERITY;
	}
}
