package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

/**
 * A {@link Variable} holding a set of values, and therefore configurable on how those values are
 * consumed: when the value changes, how it is shared between virtual users, in which order the
 * values are picked and what happens once they are exhausted.
 * <p>
 * Variable types that do not expose these properties, such as {@code password} and
 * {@code shared_queue}, extend {@link Variable} directly so that they are neither accepted on read
 * nor emitted on write.
 */
// S2097 suppressed: the value filters inherited from Variable override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface PolicyVariable extends Variable {

	// Each of the four properties below is written only when it differs from its default value.
	@JsonProperty(CHANGE_POLICY)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultChangePolicyFilter.class)
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return ChangePolicy.EACH_ITERATION;
	}

	@JsonProperty(SCOPE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultScopeFilter.class)
	@Value.Default
	default Scope getScope() {
		return Scope.GLOBAL;
	}

	@JsonProperty(ORDER)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOrderFilter.class)
	@Value.Default
	default Order getOrder() {
		return Order.ANY;
	}

	@JsonProperty(OUT_OF_VALUE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOutOfValueFilter.class)
	@Value.Default
	default OutOfValue getOutOfValue() {
		return OutOfValue.CYCLE;
	}
}
