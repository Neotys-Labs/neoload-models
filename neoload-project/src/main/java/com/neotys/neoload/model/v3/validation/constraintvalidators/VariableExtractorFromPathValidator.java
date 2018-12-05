package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.validation.constraints.UniqueVariableExtractorPathCheck;
import com.neotys.neoload.model.v3.validation.constraints.VariableExtractorFromPathCheck;

import javax.validation.ConstraintValidatorContext;

import static com.neotys.neoload.model.v3.project.userpath.VariableExtractor.From.BODY;

public final class VariableExtractorFromPathValidator extends AbstractConstraintValidator<VariableExtractorFromPathCheck, VariableExtractor> {
	@Override
	public boolean isValid(final VariableExtractor variableExtractor, final ConstraintValidatorContext context) {
		if(variableExtractor.getJsonPath().isPresent() || variableExtractor.getXpath().isPresent()){
			return BODY.equals(variableExtractor.getFrom());
		}

		return true;
	}
}
