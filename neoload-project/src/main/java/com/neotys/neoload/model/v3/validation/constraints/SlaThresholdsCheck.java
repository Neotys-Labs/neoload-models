package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraintvalidators.SlaThresholdsValidator;



@Target({ METHOD })
@Retention(RUNTIME)
@Repeatable(SlaThresholdsCheck.List.class)
@Constraint(validatedBy = { SlaThresholdsValidator.class })
@ReportAsSingleViolation
public @interface SlaThresholdsCheck {
	UsageType usage();
	
	Class<? extends Element> from();
	
	String message();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Defines several {@code @ThresholdCheck} annotations on the same element.
	 */
	@Target({ METHOD })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		SlaThresholdsCheck[] value();
	}
	
	enum UsageType {
		CHECK_UNIQUE_KPI_AND_SCOPE,
		CHECK_LIST_OF_KPIS_FROM_ELEMENT;
	}
}
