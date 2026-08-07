package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

public interface ScopeElement {

	String SCOPE = "scope";

	Scope DEFAULT_SCOPE = Scope.GLOBAL;

	enum Scope {
		@JsonProperty("unique")
		UNIQUE,
		@JsonProperty("global")
		GLOBAL,
		@JsonProperty("local")
		LOCAL
	}

	@JsonProperty(SCOPE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultScopeFilter.class)
	@Value.Default
	default Scope getScope() {
		return DEFAULT_SCOPE;
	}

	class DefaultScopeFilter {
		@Override
		public boolean equals(final Object o) {
			if  (o instanceof Scope) {
				return DEFAULT_SCOPE.equals(o);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_SCOPE.hashCode();
		}
	}
}
