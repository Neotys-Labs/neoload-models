package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.RequiredContentAssertionCheck;

public final class RequiredContentAssertionValidator extends AbstractConstraintValidator<RequiredContentAssertionCheck, ContentAssertion> {
	@Override
	public boolean isValid(final ContentAssertion assertion, final ConstraintValidatorContext context) {
		// null value is valid
		if (assertion == null) {
			return true;
		}
		
		// Check if XPath is present and not empty
		final Optional<String> optionalXPath = assertion.getXPath();
		if (optionalXPath.isPresent() && Strings.isNullOrEmpty(optionalXPath.get())) {
			return false;
		}		
		
		// Check if JsonPath is present and not empty
		final Optional<String> optionalJsonPath = assertion.getJsonPath();
		if (optionalJsonPath.isPresent() && Strings.isNullOrEmpty(optionalJsonPath.get())) {
			return false;
		}		
		
		// Check if Contains is present and not empty
		final Optional<String> optionalContains = assertion.getContains();
		if (optionalContains.isPresent() && Strings.isNullOrEmpty(optionalContains.get())) {
			return false;
		}
		
		return optionalXPath.isPresent() || optionalJsonPath.isPresent() || optionalContains.isPresent();
	}
}
