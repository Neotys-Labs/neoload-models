package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Switch.VALUE, Switch.CASE, Switch.DEFAULT})
@JsonSerialize(as = ImmutableSwitch.class)
@JsonDeserialize(as = ImmutableSwitch.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)

// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Switch extends Step {

        String DEFAULT_NAME = "switch";
        String VALUE = "value";
        String CASE = "case";
        String DEFAULT = "default";

        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultNameFilter.class)
        @Value.Default
        default String getName() {
            return DEFAULT_NAME;
        }

        @JsonProperty(VALUE)
        @Valid
        String getValue();

        @RequiredCheck(groups={NeoLoad.class})
        @JsonProperty(CASE)
        @Valid
        List<Case> getCases();

        @Valid
        @JsonProperty(DEFAULT)
        Container getDefault();

        @Override
        default Stream<Element> flattened() {
                final Stream<Element> casesStream = getCases().stream().flatMap(Case::flattened);
                final Stream<Element> childrenStream = getDefault() != null ? Stream.concat(casesStream, getDefault().flattened()) : casesStream;
                return Stream.concat(Stream.of(this), childrenStream);
        }

        // Excludes the default name from serialization: the property is omitted when the filter's
        // equals(value) returns true.
        class DefaultNameFilter {
            @Override
            public boolean equals(final Object value) {
                return DEFAULT_NAME.equals(value);
            }

            @Override
            public int hashCode() {
                return DEFAULT_NAME.hashCode();
            }
        }

        class Builder extends ImmutableSwitch.Builder{}
        static Builder builder() { return new Builder();}
}
