package com.neotys.neoload.model.validation.constraintvalidators;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;


/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
abstract class AbstractConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
	@Override
	public void initialize(final A constraintAnnotation) {
	}
}
