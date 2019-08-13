package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, Switch.VALUE, Switch.CASE, Switch.DEFAULT})
@JsonDeserialize(as = ImmutableSwitch.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)

public interface Switch extends Step {

        String DEFAULT_NAME = "switch";
        String VALUE = "value";
        String CASE = "case";
        String DEFAULT = "default";

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
                return Stream.concat(Stream.of(this), Stream.concat(getCases().stream().flatMap(Case::flattened), getDefault().flattened()));
        }

        class Builder extends ImmutableSwitch.Builder{}
        static Builder builder() { return new Builder();}
}
