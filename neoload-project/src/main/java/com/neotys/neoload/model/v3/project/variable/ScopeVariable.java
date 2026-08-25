package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface ScopeVariable extends Variable {

	String SCOPE = "scope";

	enum Scope {
		@JsonProperty("unique")
		UNIQUE,
		@JsonProperty("global")
		GLOBAL,
		@JsonProperty("local")
		LOCAL;

		// NeoLoad legacy XML "range" attribute code for this scope.
		public int getScopeCode() {
			switch (this) {
				case UNIQUE : return 4;
				case GLOBAL : return 1;
				case LOCAL : return 2;
				default : return 1;
			}
		}
	}

	// Written only when it differs from its default value.
	@JsonProperty(SCOPE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultScopeFilter.class)
	@Value.Default
	default Scope getScope() {
		return Scope.GLOBAL;
	}

	class DefaultScopeFilter {
		@Override
		public boolean equals(final Object value) {
			return Scope.GLOBAL.equals(value);
		}

		@Override
		public int hashCode() {
			return Scope.GLOBAL.hashCode();
		}
	}
}
