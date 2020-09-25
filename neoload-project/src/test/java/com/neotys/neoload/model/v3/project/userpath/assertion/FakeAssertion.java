package com.neotys.neoload.model.v3.project.userpath.assertion;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface FakeAssertion extends Assertion {
	class Builder extends ImmutableFakeAssertion.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
