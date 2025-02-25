package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableCustomActionParameter.class)
public interface CustomActionParameter {
    String getName();
    String getValue();
    @Value.Default
    default Type getType(){
        return Type.TEXT;
    }

    enum Type {
        TEXT,
        PASSWORD
    }

    class Builder extends ImmutableCustomActionParameter.Builder {}
    static Builder builder() {
        return new Builder();
    }
}