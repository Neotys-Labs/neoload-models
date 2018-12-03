package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.v3.validation.constraintvalidators.DigitsValidator;

/**
 * The annotated element must be a number within accepted range
 * Supported types are:
 * <ul>
 *     <li>{@code BigDecimal}</li>
 *     <li>{@code BigInteger}</li>
 *     <li>{@code CharSequence}</li>
 *     <li>{@code byte}, {@code short}, {@code int}, {@code long}, and their respective
 *     wrapper types</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid.
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(DigitsCheck.List.class)
@Constraint(validatedBy = { DigitsValidator.class })
@ReportAsSingleViolation
public @interface DigitsCheck {

	String message() default "{com.neotys.neoload.model.v3.validation.constraints.DigitsCheck.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return maximum number of integral digits accepted for this number
	 */
	int integer();

	/**
	 * @return maximum number of fractional digits accepted for this number
	 */
	int fraction();

	/**
	 * Defines several {@link Digits} annotations on the same element.
	 *
	 * @see Digits
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		DigitsCheck[] value();
	}
}
