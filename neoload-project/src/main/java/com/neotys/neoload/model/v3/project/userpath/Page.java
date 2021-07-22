package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutablePage.class)
@JsonDeserialize(as = ImmutablePage.class)
public interface Page extends Step, SlaElement {
    List<Request> getChildren();

    @Value.Default
    @RequiredCheck(groups = {NeoLoad.class})
    default boolean isDynamic(){
        return false;
    }

    @Value.Default
    @RequiredCheck(groups = {NeoLoad.class})
    default String getName() {
        return "#page#";
    }

    @Value.Default
    @RequiredCheck(groups = {NeoLoad.class})
    default boolean isSequential(){
        return false;
    }

    @Pattern(regexp = "(\\d+|\\$\\{\\w+\\})(-(\\d+|\\$\\{\\w+\\}))?", groups = {NeoLoad.class})
    Optional<String> getThinkTime();
}
