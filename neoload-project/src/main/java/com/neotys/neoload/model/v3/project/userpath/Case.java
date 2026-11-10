package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.List;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.immutables.value.Value;

// A case has no name in the as-code: NeoLoad names it after its value (see displayName()). The name that the
// Element contract requires is a constant, and a name written in a file is ignored.
@JsonIgnoreProperties(Element.NAME)
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, Case.VALUE, Case.BREAK, PacingElement.PACING, Case.STEPS, AssertionsElement.ASSERTIONS})
@JsonSerialize(as = ImmutableCase.class)
@JsonDeserialize(as = ImmutableCase.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Case extends Element, SlaElement, PacingElement, AssertionsElement {

    String DEFAULT_NAME = "case";
    String NAME_PREFIX = "Case ";
    String VALUE = "value";
    String BREAK = "break";
    String STEPS = "steps";

    /**
     * A constant, only here because {@link Element} requires a name. It is neither read nor written in the
     * as-code file, and a value given to the builder or to {@code withName} is not used by anything: use
     * {@link #displayName()} for the name NeoLoad gives to the case.
     */
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

    /**
     * The name under which NeoLoad shows the case: {@code Case <value>}.
     *
     * <p>This is not {@link #getName()}. NeoLoad does not let the user name a case: it always names it after
     * its value and renames it each time the value changes (NLGui's {@code CaseStatement#getGUILabel}), so a
     * case has no name in the as-code file, and {@link #getName()} is only the constant that {@link Element}
     * requires. Anything that must designate a case the way NeoLoad does, such as a websocket channel path,
     * therefore has to use this name and not {@link #getName()}.</p>
     *
     * <p>It is not a bean property (no {@code get} prefix): it is neither read nor written in the as-code file.</p>
     */
    default String displayName() {
        return NAME_PREFIX + getValue();
    }

    class Builder extends ImmutableCase.Builder{}
    static Case.Builder builder() { return new Case.Builder();}
}
