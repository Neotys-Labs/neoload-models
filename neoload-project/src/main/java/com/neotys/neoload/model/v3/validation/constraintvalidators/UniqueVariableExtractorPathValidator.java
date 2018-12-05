package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.validation.constraints.UniqueVariableExtractorPathCheck;

import javax.validation.ConstraintValidatorContext;

public final class UniqueVariableExtractorPathValidator extends AbstractConstraintValidator<UniqueVariableExtractorPathCheck, VariableExtractor> {
	@Override
	public boolean isValid(final VariableExtractor variableExtractor, final ConstraintValidatorContext context) {
		return !(variableExtractor.getJsonPath().isPresent() && variableExtractor.getXpath().isPresent());
	}
}
