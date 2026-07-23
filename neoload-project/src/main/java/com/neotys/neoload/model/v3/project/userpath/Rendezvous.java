package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION})
@JsonDeserialize(as = ImmutableRendezvous.class)
@JsonSerialize(as = ImmutableRendezvous.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Rendezvous extends Step {

	//default values
	String DEFAULT_NAME = "rendezvous";

	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@JsonProperty(Element.NAME)
	@Value.Default
	default String getName() { return DEFAULT_NAME; }

	class Builder extends ImmutableRendezvous.Builder {}
	static Builder builder() {
		return new Builder();
	}

	// Jackson value filters excluding the default name / match from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			if (value instanceof String) {
				return DEFAULT_NAME.equals(value);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}
}