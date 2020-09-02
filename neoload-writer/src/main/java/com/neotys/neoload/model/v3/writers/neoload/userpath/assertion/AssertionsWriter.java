package com.neotys.neoload.model.v3.writers.neoload.userpath.assertion;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.neotys.neoload.model.v3.util.AssertionUtils.normalyze;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

public final class AssertionsWriter {
	
	private static final String XML_TAG_NAME   = "assertions";
	
		
	private final List<ContentAssertion> assertions;

    private AssertionsWriter(final List<ContentAssertion> assertions) {
        this.assertions = checkNotNull(assertions);
    }

    public static AssertionsWriter of(final List<ContentAssertion> assertions) {
        return new AssertionsWriter(assertions);
    }

	public void writeXML(final Document document, final Element currentElement) {
		// Assertions tag
		final Element xmlAssertions = document.createElement(XML_TAG_NAME);
		currentElement.appendChild(xmlAssertions);
		
		// Computes the names if necessary
		final List<ContentAssertion> normalyzedAssertions = normalyze(assertions);
		
		// Assertion tag
		normalyzedAssertions.forEach(assertion -> ContentAssertionWriter.of(assertion).writeXML(document, xmlAssertions));
	}
}
