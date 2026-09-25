package com.neotys.neoload.model.v3.project.userpath;

import java.util.Optional;
import org.immutables.value.Value;

/**
 * A step that references a {@code shared_elements} entry by name, in place of inlining its definition.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SharedElementRef extends Step {
	/**
	 * Always empty: {@link com.neotys.neoload.model.v3.binding.serializer.StepsSerializer} writes a
	 * {@code SharedElementRef} as a bare {@code {shared_element: name}}, with no slot for a description,
	 * so one is never settable rather than being silently dropped on serialization.
	 */
	@Value.Derived
	default Optional<String> getDescription() {
		return Optional.empty();
	}

	class Builder extends ImmutableSharedElementRef.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
