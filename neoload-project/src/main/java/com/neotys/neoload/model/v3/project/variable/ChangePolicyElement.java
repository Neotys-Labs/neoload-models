package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

public interface ChangePolicyElement {

	String CHANGE_POLICY = "change_policy";

	ChangePolicy DEFAULT_CHANGE_POLICY = ChangePolicy.EACH_ITERATION;

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
		EACH_ITERATION
	}

	@JsonProperty(CHANGE_POLICY)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultChangePolicyFilter.class)
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return DEFAULT_CHANGE_POLICY;
	}

	class DefaultChangePolicyFilter {
		@Override
		public boolean equals(final Object o) {
			if  (o instanceof ChangePolicy) {
				return DEFAULT_CHANGE_POLICY.equals(o);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_CHANGE_POLICY.hashCode();
		}
	}
}
