package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.AssertionsFieldCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredContentAssertionCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionPathCheck;
import java.util.List;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates whichever of {@code assertions} (deprecated) or {@code content_assertions} is the
 * effective source of content assertions for the element, and reports violations under that
 * field's own name. The two accessors are mutually exclusive in practice ({@code content_assertions}
 * falls back to {@code assertions} when absent), so exactly one of them is ever validated.
 */
public final class AssertionsFieldValidator extends AbstractConstraintValidator<AssertionsFieldCheck, AssertionsElement> {

	private static final UniqueContentAssertionNameValidator NAME_VALIDATOR = new UniqueContentAssertionNameValidator();
	private static final RequiredContentAssertionValidator REQUIRED_VALIDATOR = new RequiredContentAssertionValidator();
	private static final UniqueContentAssertionPathValidator PATH_VALIDATOR = new UniqueContentAssertionPathValidator();

	@Override
	public boolean isValid(final AssertionsElement element, final ConstraintValidatorContext context) {
		if (element == null) {
			return true;
		}

		final List<Assertion> assertions = element.getAssertions();
		final List<Assertion> contentAssertions = element.getContentAssertions();
		final boolean explicitContentAssertions = assertions != contentAssertions;
		final String fieldName = explicitContentAssertions ? AssertionsElement.CONTENT_ASSERTIONS : AssertionsElement.ASSERTIONS;
		final List<Assertion> effective = explicitContentAssertions ? contentAssertions : assertions;

		boolean valid = true;
		context.disableDefaultConstraintViolation();

		if (!NAME_VALIDATOR.isValid(effective, null)) {
			context.buildConstraintViolationWithTemplate(messageTemplate(UniqueContentAssertionNameCheck.class))
					.addPropertyNode(fieldName)
					.addConstraintViolation();
			valid = false;
		}

		for (int i = 0; i < effective.size(); i++) {
			final Assertion assertion = effective.get(i);
			if (!(assertion instanceof ContentAssertion)) {
				continue;
			}
			final ContentAssertion contentAssertion = (ContentAssertion) assertion;

			if (!REQUIRED_VALIDATOR.isValid(contentAssertion, null)) {
				context.buildConstraintViolationWithTemplate(messageTemplate(RequiredContentAssertionCheck.class))
						.addPropertyNode(fieldName)
							.addBeanNode()
								.inContainer(List.class, 0)
								.inIterable().atIndex(i)
						.addConstraintViolation();
				valid = false;
			}

			if (!PATH_VALIDATOR.isValid(contentAssertion, null)) {
				context.buildConstraintViolationWithTemplate(messageTemplate(UniqueContentAssertionPathCheck.class))
						.addPropertyNode(fieldName)
							.addBeanNode()
								.inContainer(List.class, 0)
								.inIterable().atIndex(i)
						.addConstraintViolation();
				valid = false;
			}
		}

		return valid;
	}

	private static String messageTemplate(final Class<?> constraintClass) {
		return "{" + constraintClass.getName() + ".message}";
	}
}
