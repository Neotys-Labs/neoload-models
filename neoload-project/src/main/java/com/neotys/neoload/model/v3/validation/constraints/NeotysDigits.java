package com.neotys.neoload.model.v3.validation.constraints;

/*
 * Bean Validation API
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated element must be a number within accepted range
 * Supported types are:
 * <ul>
 * <li>{@code BigDecimal}</li>
 * <li>{@code BigInteger}</li>
 * <li>{@code CharSequence}</li>
 * <li>{@code byte}, {@code short}, {@code int}, {@code long}, and their respective
 * wrapper types</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid.
 *
 * @author Emmanuel Bernard
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Repeatable(NeotysDigits.List.class)
@Documented
@Constraint(validatedBy = {})
public @interface NeotysDigits {

	String message() default "{javax.validation.constraints.Digits.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return maximum number of integral digits accepted for this number
	 */
	int integer();

	/**
	 * @return maximum number of fractional digits accepted for this number
	 */
	int fraction();

	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
	@Retention(RUNTIME)
	@Documented
	@interface List {

		NeotysDigits[] value();
	}
}

