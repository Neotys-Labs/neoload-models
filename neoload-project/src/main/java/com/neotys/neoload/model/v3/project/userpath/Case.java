package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.StepsSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Case.VALUE, Case.BREAK, Case.STEPS, AssertionsElement.ASSERTIONS})
@JsonSerialize(as = ImmutableCase.class)
@JsonDeserialize(as = ImmutableCase.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Case extends Element, SlaElement, AssertionsElement {

    String DEFAULT_NAME = "case";
    String VALUE = "value";
    String BREAK = "break";
    String STEPS = "steps";

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultNameFilter.class)
    @Value.Default
    default String getName() {
        return DEFAULT_NAME;
    }

    @RequiredCheck(groups={NeoLoad.class})
    @JsonProperty(VALUE)
    @Valid
    String getValue();

    @RequiredCheck(groups={NeoLoad.class})
    @JsonProperty(BREAK)
    @Valid
    Boolean isBreak();

    @RequiredCheck(groups={NeoLoad.class})
    @JsonProperty(STEPS)
    @JsonSerialize(using = StepsSerializer.class)
    @JsonDeserialize(using = StepsDeserializer.class)
    @Valid
    List<Step> getSteps();


    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this),getSteps().stream().flatMap(Step::flattened));
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

    class Builder extends ImmutableCase.Builder{}
    static Case.Builder builder() { return new Case.Builder();}
}