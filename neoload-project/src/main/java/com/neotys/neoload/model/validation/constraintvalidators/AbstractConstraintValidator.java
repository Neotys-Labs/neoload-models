package com.neotys.neoload.model.validation.constraintvalidators;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;


@Deprecated
abstract class AbstractConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
	@Override
	public void initialize(final A constraintAnnotation) {
	}
}
