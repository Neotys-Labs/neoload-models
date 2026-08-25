package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface ChangePolicyVariable extends Variable {

	String CHANGE_POLICY = "change_policy";

	enum ChangePolicy {
		@JsonProperty("each_use")
		EACH_USE,
		@JsonProperty("each_request")
		EACH_REQUEST,
		@JsonProperty("each_page")
		EACH_PAGE,
		@JsonProperty("each_user")
		EACH_USER,
		@JsonProperty("each_iteration")
		EACH_ITERATION;

		// NeoLoad legacy XML "policy" attribute code for this change policy.
		public int getPolicyCode() {
			switch (this) {
				case EACH_USE : return 1;
				case EACH_REQUEST : return 2;
				case EACH_PAGE : return 3;
				case EACH_USER : return 4;
				case EACH_ITERATION : return 5;
				default : return 1;
			}
		}
	}

	// Written only when it differs from its default value.
	@JsonProperty(CHANGE_POLICY)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultChangePolicyFilter.class)
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return ChangePolicy.EACH_ITERATION;
	}

	class DefaultChangePolicyFilter {
		@Override
		public boolean equals(final Object value) {
			return ChangePolicy.EACH_ITERATION.equals(value);
		}

		@Override
		public int hashCode() {
			return ChangePolicy.EACH_ITERATION.hashCode();
		}
	}
}
