package com.neotys.neoload.model.v3.project.scenario;

import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public enum LoadPolicyType {
    CONSTANT,
    CUSTOM,
    PEAKS,
    RAMPUP;

    private static final Set<String> NAME_SET = ImmutableSet.copyOf(Stream.of(values()).map(Enum::name).collect(Collectors.toSet()));

    public static LoadPolicyType checkedValueOf(final String name) {
        checkNotNull(name);
        checkArgument(NAME_SET.contains(name), "Invalid LoadPolicyType");
        return LoadPolicyType.valueOf(name);
    }
}