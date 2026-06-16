package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.v3.validation.constraintvalidators.ValidSchemaVersionValidator;

/**
 * Validates that a declared {@code schemaVersion} is known to the embedded
 * {@code neoload-models} build (matches an entry in {@code supported-schemas.json}).
 *
 * Delegates to {@link com.neotys.neoload.model.v3.compatibility.SchemaSupport#isSupported(String)}.
 * A {@code null} value is considered valid here — apply {@code @NotNull}
 * separately if needed.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidSchemaVersionValidator.class)
@ReportAsSingleViolation
public @interface ValidSchemaVersion {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.ValidSchemaVersion.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
