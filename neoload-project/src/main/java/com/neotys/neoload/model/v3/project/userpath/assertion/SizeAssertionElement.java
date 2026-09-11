package com.neotys.neoload.model.v3.project.userpath.assertion;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SizeAssertionElement {
	String SIZE_ASSERTION = "size_assertion";

	@JsonProperty(SIZE_ASSERTION)
	@Valid
	Optional<SizeAssertion> getSizeAssertion();
}
