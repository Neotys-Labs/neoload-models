package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ElementsDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, Case.VALUE, Case.BREAK, Case.STEPS})
@JsonDeserialize(as = ImmutableCase.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Case extends Element, SlaElement {

    String DEFAULT_NAME = "case";
    String VALUE = "value";
    String BREAK = "break";
    String STEPS = "steps";

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
    @JsonDeserialize(using = ElementsDeserializer.class)
    @Valid
    List<Step> getSteps();


    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this),getSteps().stream().flatMap(Step::flattened));
    }

    class Builder extends ImmutableCase.Builder{}
    static Case.Builder builder() { return new Case.Builder();}
}