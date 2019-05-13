package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

@Value.Immutable
public interface CustomActionParameter {
    String getName();
    String getValue();
    Type getType();

    enum Type {
        TEXT,
        PASSWORD
    }

    class Builder extends ImmutableCustomActionParameter.Builder {}
    static Builder builder() {
        return new Builder();
    }
}