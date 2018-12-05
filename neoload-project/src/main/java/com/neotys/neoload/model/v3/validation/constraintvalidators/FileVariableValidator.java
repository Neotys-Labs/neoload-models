package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.validation.constraints.FileVariableCheck;

import javax.validation.ConstraintValidatorContext;

public final class FileVariableValidator extends AbstractConstraintValidator<FileVariableCheck, FileVariable> {
	@Override
	public boolean isValid(final FileVariable fileVariable, final ConstraintValidatorContext context) {
		return !fileVariable.getColumnNames().isEmpty() || fileVariable.isFirstLineColumnNames();
	}
}
