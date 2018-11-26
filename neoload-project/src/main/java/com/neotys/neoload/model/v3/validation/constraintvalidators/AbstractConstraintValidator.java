package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;


abstract class AbstractConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
	@Override
	public void initialize(final A constraintAnnotation) {
	}
}
