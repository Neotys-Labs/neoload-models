package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.ImmutableRequest;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;
import org.junit.Test;

public class AssertionsFieldValidatorTest {

	private static ConstraintValidatorContext newContext() {
		return new ConstraintValidatorContextImpl(
				null,
				PathImpl.createPathFromString("mock"),
				null,
				null,
				ExpressionLanguageFeatureLevel.DEFAULT,
				ExpressionLanguageFeatureLevel.DEFAULT);
	}

	@Test
	public void isValid() {
		final AssertionsFieldValidator validator = new AssertionsFieldValidator();

		assertTrue(validator.isValid(null, newContext()));

		final ImmutableRequest onlyLegacyAssertions = Request.builder()
				.url("http://neotys.com")
				.addLegacyAssertions(ContentAssertion.builder().contains("foo").build())
				.build();
		assertTrue(validator.isValid(onlyLegacyAssertions, newContext()));

		final ImmutableRequest onlyContentAssertions = Request.builder()
				.url("http://neotys.com")
				.addContentAssertions(ContentAssertion.builder().contains("foo").build())
				.build();
		assertTrue(validator.isValid(onlyContentAssertions, newContext()));

		final ImmutableRequest both = Request.builder()
				.url("http://neotys.com")
				.addLegacyAssertions(ContentAssertion.builder().contains("foo").build())
				.addContentAssertions(ContentAssertion.builder().contains("bar").build())
				.build();
		assertFalse(validator.isValid(both, newContext()));

		final ImmutableRequest duplicateNames = Request.builder()
				.url("http://neotys.com")
				.addContentAssertions(
						ContentAssertion.builder().name("dup").contains("foo").build(),
						ContentAssertion.builder().name("dup").contains("bar").build())
				.build();
		assertFalse(validator.isValid(duplicateNames, newContext()));
	}
}
