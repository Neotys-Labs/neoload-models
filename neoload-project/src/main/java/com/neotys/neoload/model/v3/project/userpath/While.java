package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.MatchDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, While.CONDITIONS, Match.MATCH, While.STEPS})
@JsonDeserialize(as = ImmutableWhile.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface While extends Step {

    String DEFAULT_NAME = "while";
    String CONDITIONS = "conditions";
    String STEPS = "steps";

    @Value.Default
    default String getName() {
        return DEFAULT_NAME;
    }

    @JsonProperty(CONDITIONS)
    @Valid
    List<Condition> getConditions();

    @JsonProperty(Match.MATCH)
    @RequiredCheck(groups = {NeoLoad.class})
    @JsonDeserialize(using = MatchDeserializer.class)
    @Value.Default
    default Match getMatch() {
        return Match.ANY;
    }

    @JsonProperty(STEPS)
    @RequiredCheck(groups = {NeoLoad.class})
    @Valid
    @JsonDeserialize(using = StepsDeserializer.class)
    List<Step> getSteps();

    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
    }

    class Builder extends ImmutableWhile.Builder {
    }

    static Builder builder() {
        return new Builder();
    }
}
