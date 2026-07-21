package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCustomActionParameter.class)
@JsonDeserialize(as = ImmutableCustomActionParameter.class)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface CustomActionParameter {
    String getName();

	String getValue();

	// The type is written only when it differs from the default (TEXT): PASSWORD is written, TEXT is omitted.
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultTypeFilter.class)
	@Value.Default
    default Type getType(){
        return Type.TEXT;
    }

    enum Type {
        TEXT,
        PASSWORD
    }

    /**
     * Jackson value filter used to exclude {@link Type#TEXT} (the default) from serialization:
     * a property is omitted when the filter's {@code equals(value)} returns {@code true}.
     */
    class DefaultTypeFilter {
        @Override
        public boolean equals(final Object type) {
            return Type.TEXT.equals(type);
        }

        @Override
        public int hashCode() {
            return Type.TEXT.hashCode();
        }
    }

    class Builder extends ImmutableCustomActionParameter.Builder {}
    static Builder builder() {
        return new Builder();
    }
}